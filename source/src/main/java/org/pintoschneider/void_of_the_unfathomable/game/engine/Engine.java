package org.pintoschneider.void_of_the_unfathomable.game.engine;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp.Capability;
import org.jline.utils.NonBlockingReader;
import org.pintoschneider.void_of_the_unfathomable.core.Size;
import org.pintoschneider.void_of_the_unfathomable.ui.components.*;
import org.pintoschneider.void_of_the_unfathomable.ui.core.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.Objects;

public final class Engine implements AutoCloseable, Context {
    private static Engine context = null;
    private final Terminal terminal = TerminalBuilder.builder().system(true).build();
    private final PrintWriter writer = terminal.writer();
    private final SceneManager sceneManager;
    private final InputThread inputThread;
    private final UIThread uiThread;
    private Size terminalSize;
    private boolean running = true;

    public Engine(Scene initialScene) throws IOException {
        sceneManager = new SceneManager(Objects.requireNonNull(initialScene));
        terminal.enterRawMode();

        // Register a signal handler for window resize events
        terminal.handle(Terminal.Signal.WINCH, _ -> updateTerminalSize());

        terminal.puts(Capability.cursor_invisible);

        updateTerminalSize();

        // Start input thread using a dedicated class
        final NonBlockingReader reader = terminal.reader();

        inputThread = new InputThread(this, reader);
        inputThread.setDaemon(true);
        inputThread.start();

        // Start the UI thread using a dedicated class
        uiThread = new UIThread(this, 60);
        uiThread.setDaemon(true);
        uiThread.start();

        if (context != null) {
            throw new IllegalStateException("There can only be one Engine instance at a time.");
        }

        context = this;
    }

    static public Context context() {
        if (context == null) {
            throw new IllegalStateException("No Engine instance is currently running.");
        }

        return context;
    }

    void tick() {
        if (sceneManager.hasScene()) {
            refresh();

            synchronized (this) {
                notifyAll();
            }
        } else {
            stop();
        }
    }

    void processKey(Key key) {
        sceneManager.currentScene().onKeyPress(key);
    }

    private void refresh() {
        drawScene();
    }

    private void drawScene() {
        terminal.puts(Capability.cursor_address, 0, 0);
        final Component rootComponent = new Root(this);
        rootComponent.layout(Constraints.tight(terminalSize.width(), terminalSize.height()));

        final Canvas rootCanvas = new Canvas(rootComponent);
        rootComponent.draw(rootCanvas);
        rootCanvas.writeTo(writer);
        terminal.flush();
    }

    private void updateTerminalSize() {
        terminalSize = new Size(terminal.getWidth(), terminal.getHeight());
    }

    /**
     * Returns whether the engine is still running.
     */
    public synchronized void waitUntilStopped() throws InterruptedException {
        while (running) {
            wait();
        }
    }

    private void stop() {
        running = false;
        synchronized (this) {
            notifyAll();
        }
    }

    @Override
    public SceneManager sceneManager() {
        return sceneManager;
    }

    @Override
    public long deltaTime() {
        return uiThread.deltaTime();
    }

    @Override
    public long tickCount() {
        return uiThread.tickCount();
    }

    @Override
    public Size size() {
        return terminalSize;
    }

    @Override
    public long waitTick() throws InterruptedException {
        final long observed = uiThread.tickCount();
        synchronized (this) {
            while (uiThread.tickCount() == observed) {
                this.wait();
            }
        }

        return uiThread.deltaTime();
    }

    @Override
    public void close() throws IOException {
        inputThread.interrupt();
        terminal.close();
    }
}

final class Root extends Composent {
    static final Paint boldPaint = new Paint().withBold(true);
    final Engine engine;

    Root(Engine engine) {
        this.engine = engine;
    }

    @Override
    public Component build() {
        final Component[] stackChildren =
            Engine.context().sceneManager().scenes().stream().map(Scene::build).toList().reversed().toArray(Component[]::new);

        return new Column(
            new Flexible(
                new Stack(stackChildren)
            ),
            new Row(
                new IdleSpinner((int) ((System.nanoTime() / 100_000_000) % 6)),
                new SizedBox(new Size(1, 0), null),
                new Text("FPS: ", boldPaint),
                new Text(String.format("%.2f", 1_000_000_000.0 / engine.deltaTime()))
            )
        );
    }
}

/**
 * Reads input from the NonBlockingReader and puts key codes into a queue.
 */
final class InputThread extends Thread {
    private final NonBlockingReader reader;
    private final Engine engine;

    /**
     * Creates an InputThread.
     *
     * @param engine The game engine to send input to.
     * @param reader The NonBlockingReader to read input from.
     */
    InputThread(Engine engine, NonBlockingReader reader) {
        this.engine = Objects.requireNonNull(engine);
        this.reader = Objects.requireNonNull(reader);
    }

    @Override
    public void run() {
        try {
            final CharBuffer buffer = CharBuffer.allocate(3);
            while (!Thread.currentThread().isInterrupted()) {
                int result = reader.read(buffer);
                if (result > 0 && buffer.get(0) == 27) {
                    // Possible start of an escape sequence
                    result = reader.read(buffer);
                    if (result > 0 && buffer.get(1) == 91) {
                        result = reader.read(buffer);

                        if (result > 0) {
                            // CSI sequence
                            final Key key = Key.parse(buffer.array());
                            engine.processKey(key);
                            buffer.clear();
                            continue;
                        }
                    }
                }

                for (char ch : buffer.array()) {
                    if (ch != 0) {
                        final Key key = Key.parse(new char[]{ch});
                        engine.processKey(key);
                    }
                }

                buffer.clear();
            }
        } catch (IOException exception) {
            System.err.printf(
                "Exception caught in InputThread:%n%s%n%s%n",
                exception.getMessage(),
                Arrays.toString(exception.getStackTrace())
            );
        }
    }
}

/**
 * A thread that runs the game UI, updating the engine at a fixed frames per second (FPS).
 */
final class UIThread extends Thread {
    private final Engine engine;
    private final int fps;
    private long lastNanoTime;
    private volatile long tickCount = 0;
    private volatile long deltaTime = 0;

    /**
     * Creates a UIThread.
     *
     * @param engine The game engine to update.
     * @param fps    The target frames per second.
     */
    UIThread(Engine engine, int fps) {
        this.engine = Objects.requireNonNull(engine);
        this.fps = fps;
    }

    /**
     * Returns the number of ticks that have occurred since the thread started.
     *
     * @return The tick count.
     */
    public long tickCount() {
        return tickCount;
    }

    /**
     * Returns the time in nanoseconds since the last tick.
     *
     * @return The delta time in nanoseconds.
     */
    public long deltaTime() {
        return deltaTime;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            final long nanoSeconds = System.nanoTime();

            if (nanoSeconds - lastNanoTime >= 1_000_000_000L / fps) {
                deltaTime = nanoSeconds - lastNanoTime;
                lastNanoTime = nanoSeconds;

                synchronized (this) {
                    tickCount++;
                }

                engine.tick();
            }
        }
    }
}

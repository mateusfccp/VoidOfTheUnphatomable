package org.pintoschneider.void_of_the_unfathomable.game.engine;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp.Capability;
import org.jline.utils.NonBlockingReader;
import org.pintoschneider.void_of_the_unfathomable.core.Size;
import org.pintoschneider.void_of_the_unfathomable.ui.components.IdleSpinner;
import org.pintoschneider.void_of_the_unfathomable.ui.components.Text;
import org.pintoschneider.void_of_the_unfathomable.ui.core.*;

import java.io.IOException;
import java.io.PrintWriter;

public class Engine implements AutoCloseable, Context {
    private final Terminal terminal = TerminalBuilder.builder().system(true).build();
    private final PrintWriter writer = terminal.writer();
    private Size terminalSize;
    private final SceneManager sceneManager;
    private final InputThread inputThread;
    private final UIThread uiThread;
    private boolean running = true;

    public Engine(Scene initialScene) throws IOException {
        sceneManager = new SceneManager(initialScene);
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
        uiThread = new UIThread(this, 30);
        uiThread.setDaemon(true);
        uiThread.start();
    }

    void tick() {
        if (sceneManager.hasScene()) {
            refresh();
        } else {
            stop();
        }
    }

    void processKey(int key) {
        sceneManager.currentScene().onKeyPress(this, key);
    }

    private void clearScreen() {
        terminal.puts(Capability.clear_screen);
    }

    private void refresh() {
//        clearScreen();
        drawScene();
    }

    private void drawScene() {
        terminal.puts(Capability.cursor_address, 0, 0);
        final Component rootComponent = new DebuggingLine(this,
            sceneManager.currentScene().build(this)
        );
        rootComponent.layout(Constraints.tight(terminalSize.width(), terminalSize.height()));

        final Canvas rootCanvas = new Canvas(rootComponent.size());
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
    public void close() throws IOException {
        inputThread.interrupt();
        terminal.close();
    }
}

final class DebuggingLine extends Component {
    final Engine engine;
    final Component child;

    DebuggingLine(Engine engine, Component child) {
        this.engine = engine;
        this.child = child;
    }

    @Override
    public void layout(Constraints constraints) {
        size = constraints.biggest();
        child.layout(constraints.deflate(0, 1));
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.draw(child, 0, 0);

        final int y = size.height() - 1;

        // FPS
        final IdleSpinner spinner = new IdleSpinner((int) ((System.nanoTime() / 100_000_000) % 6));
        spinner.layout(Constraints.tight(new Size(1, 1)));
        canvas.draw(spinner, 0, y);

        final Paint boldPaint = new Paint();
        boldPaint.bold = true;

        final Text fpsText = new Text("FPS: ", boldPaint);
        fpsText.layout(Constraints.tight(new Size(4, 1)));
        canvas.draw(fpsText, 2, y);

        final double fps = 1_000_000_000.0 / engine.deltaTime();
        final Text fpsValueText = new Text(String.format("%.2f", fps));
        fpsValueText.layout(Constraints.tight(new Size(6, 1)));
        canvas.draw(fpsValueText, 7, y);
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
        this.engine = engine;
        this.reader = reader;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                int c = reader.read(1);
                if (c != NonBlockingReader.READ_EXPIRED && c != NonBlockingReader.EOF) {
                    engine.processKey(c);
                }
            }
        } catch (IOException e) {
            // Thread interrupted or IO error, exit thread
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
    private long tickCount = 0;
    private long deltaTime = 0;

    /**
     * Creates a UIThread.
     *
     * @param engine The game engine to update.
     * @param fps    The target frames per second.
     */
    UIThread(Engine engine, int fps) {
        this.engine = engine;
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
                tickCount++;
                engine.tick();
            }
        }
    }
}

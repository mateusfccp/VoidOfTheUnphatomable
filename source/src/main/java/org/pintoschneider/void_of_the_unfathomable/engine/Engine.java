package org.pintoschneider.void_of_the_unfathomable.engine;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.AttributedString;
import org.jline.utils.Display;
import org.jline.utils.InfoCmp.Capability;
import org.pintoschneider.void_of_the_unfathomable.Main;
import org.pintoschneider.void_of_the_unfathomable.core.Size;
import org.pintoschneider.void_of_the_unfathomable.ui.components.*;
import org.pintoschneider.void_of_the_unfathomable.ui.core.*;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public final class Engine implements AutoCloseable, Context {
    private static Engine context = null;
    private final Terminal terminal = TerminalBuilder.builder().build();
    private final Display display;
    private final AtomicReference<Key> lastKey = new AtomicReference<>(null);
    private final SceneManager sceneManager;
    private final InputThread inputThread;
    private final UIThread uiThread;
    private final List<_EngineTicker> tickers = new ArrayList<>();
    private boolean running = true;

    public Engine(Scene initialScene) throws IOException {
        terminal.enterRawMode();

        display = new Display(terminal, true);

        // Register a signal handler for window resize events
        terminal.handle(Terminal.Signal.WINCH, _ -> updateTerminalSize());

        // Register a signal handler for Ctrl+C (SIGINT)
        terminal.handle(Terminal.Signal.INT, _ -> stop());

        terminal.puts(Capability.cursor_invisible);
        terminal.puts(Capability.enter_ca_mode);
        terminal.puts(Capability.keypad_xmit);

        updateTerminalSize();

        if (context != null) {
            throw new IllegalStateException("There can only be one Engine instance at a time.");
        }

        context = this;
        sceneManager = new SceneManager(Objects.requireNonNull(initialScene), this::stop);

        inputThread = new InputThread(terminal, lastKey);
        inputThread.setDaemon(true);
        inputThread.start();

        uiThread = new UIThread(this, 60);
        uiThread.setDaemon(true);
        uiThread.start();

    }

    static public Context context() {
        if (context == null) {
            throw new IllegalStateException("No Engine instance is currently running.");
        }

        return context;
    }

    void tick() {
        for (_EngineTicker ticker : new ArrayList<>(tickers)) {
            if (ticker.active) {
                ticker.onTick.accept(Duration.ofNanos(uiThread.deltaTime()));
            }
        }

        sceneManager.currentScene().onUpdate(uiThread.deltaTime());
        handleInput();
        refreshUI();

        synchronized (this) {
            notifyAll();
        }
    }

    private void handleInput() {
        final Key keyToProcess = lastKey.getAndSet(null);

        if (keyToProcess != null) {
            sceneManager.currentScene().onKeyPress(keyToProcess);
        }
    }

    private void refreshUI() {
        final Component rootComponent = new Root(this);
        rootComponent.layout(Constraints.tight(terminal.getWidth(), terminal.getHeight()));

        final Canvas rootCanvas = new Canvas(rootComponent);
        rootComponent.draw(rootCanvas);

        final List<AttributedString> lines = rootCanvas.toAttributedStrings();
        display.update(lines, 0);
    }

    private void updateTerminalSize() {
        display.resize(terminal.getHeight(), terminal.getWidth());
        display.clear();
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
        return new Size(
            terminal.getWidth(),
            terminal.getHeight()
        );
    }

    @Override
    public Ticker createTicker(Consumer<Duration> onTick) {
        return new _EngineTicker(onTick);
    }

    @Override
    public void close() throws IOException {
        terminal.puts(Capability.keypad_local);
        terminal.puts(Capability.exit_ca_mode);
        terminal.puts(Capability.cursor_visible);
        uiThread.interrupt();
        inputThread.interrupt();
        display.clear();
        terminal.flush();
        terminal.close();
    }

    final class _EngineTicker implements Ticker {
        private final Consumer<Duration> onTick;
        private boolean active = true;

        _EngineTicker(Consumer<Duration> onTick) {
            this.onTick = onTick;
            tickers.add(this);
        }

        @Override
        public void dispose() {
            tickers.remove(this);
            active = false;
        }
    }
}

final class Root extends Composent {
    final Engine engine;

    Root(Engine engine) {
        this.engine = engine;
    }

    @Override
    public Component build() {
        final Component[] stackChildren =
            Engine.context().sceneManager().scenes().stream().map(Scene::build).toList().reversed().toArray(Component[]::new);

        final Component debugLine;

        if (Main.debugMode) {
            final String sceneInfo = "%s (%d)".formatted(
                engine.sceneManager().currentScene().getClass().getSimpleName(),
                engine.sceneManager().scenes().size()
            );
            debugLine = new Row(
                new Text("FPS: ", Paint.BOLD),
                new Text(String.format("%.2f", 1_000_000_000.0 / engine.deltaTime()), Paint.DIM),
                new SizedBox(1, 0),
                new Text("Scene: ", Paint.BOLD),
                new Text(sceneInfo, Paint.DIM)
            );
        } else {
            debugLine = null;
        }

        return new Column(
            new Flexible(
                new Stack(stackChildren)
            ),
            debugLine
        );
    }
}


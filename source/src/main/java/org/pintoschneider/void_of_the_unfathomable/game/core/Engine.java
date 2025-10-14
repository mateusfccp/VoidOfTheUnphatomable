package org.pintoschneider.void_of_the_unfathomable.game.core;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp.Capability;
import org.jline.utils.NonBlockingReader;
import org.pintoschneider.void_of_the_unfathomable.ui.components.IdleSpinner;
import org.pintoschneider.void_of_the_unfathomable.ui.components.Text;
import org.pintoschneider.void_of_the_unfathomable.ui.core.*;

import java.io.IOException;
import java.io.PrintWriter;

public class Engine implements AutoCloseable, Context {
    private final Terminal terminal = TerminalBuilder.builder().system(true).build();
    private final NonBlockingReader reader = terminal.reader();
    private final PrintWriter writer = terminal.writer();
    private Size terminalSize;
    private final SceneManager sceneManager;
    private long lastNanoTime;
    private long deltaTime;

    public Engine(Scene initialScene) throws IOException {
        sceneManager = new SceneManager(initialScene);
        terminal.enterRawMode();

        // Register a signal handler for window resize events
        terminal.handle(Terminal.Signal.WINCH, _ -> {
            updateTerminalSize();
            refresh();
        });

        terminal.puts(Capability.cursor_invisible);

        lastNanoTime = System.nanoTime();

        updateTerminalSize();
        refresh();
    }

    public boolean isAlive() {
        return sceneManager.hasScene();
    }

    public void tick() {
        try {
            final int c = reader.read(1);
            if (c != NonBlockingReader.READ_EXPIRED && c != NonBlockingReader.EOF) {
                sceneManager.currentScene().onKeyPress(this, c);
            }
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }

        if (!isAlive()) return;
        refresh();

        deltaTime = System.nanoTime() - lastNanoTime;
        lastNanoTime = System.nanoTime();
    }

    private void clearScreen() {
        terminal.puts(Capability.clear_screen);
    }

    private void refresh() {
        clearScreen();
        drawScene();
    }

    private void drawScene() {
        final Component rootComponent = new DebuggingLine(this,
                sceneManager.currentScene().build(this)
        );
        rootComponent.layout(Constraints.tight(terminalSize.width(), terminalSize.height()));

        final Canvas rootCanvas = new Canvas(rootComponent.size(), rootComponent);
        rootComponent.draw(rootCanvas);
        rootCanvas.writeTo(writer);
        writer.flush();
    }

    private void updateTerminalSize() {
        terminalSize = new Size(terminal.getWidth(), terminal.getHeight());
    }

    @Override
    public void close() throws IOException {
        terminal.close();
    }

    @Override
    public SceneManager sceneManager() {
        return sceneManager;
    }

    @Override
    public long deltaTime() {
        return deltaTime;
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
        final IdleSpinner spinner = new IdleSpinner((int) (System.nanoTime() % 10 / 10));
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

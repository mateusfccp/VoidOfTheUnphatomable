package org.pintoschneider.void_of_the_unfathomable;

import com.jediterm.core.util.TermSize;
import com.jediterm.terminal.ui.JediTermWidget;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.pintoschneider.void_of_the_unfathomable.frontend.JLineTtyConnector;
import org.pintoschneider.void_of_the_unfathomable.frontend.SettingsProvider;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.StandardCharsets;

import static org.jline.terminal.TerminalBuilder.PROP_DISABLE_ALTERNATE_CHARSET;

/**
 * A terminal application based on JediTerm that works as a frontend for the
 * game engine.
 * <p>
 * This is the recommended way to run the game on desktop platforms, as it can
 * be compiled to a native executable that
 * will work without requiring a terminal emulator, and will provide a
 * consistent experience across different operating
 * systems and independent of the user's terminal settings.
 */
public final class Main {
    static private final int width = 80;
    static private final int height = 50;
    static private final Dimension dimension = new Dimension(width, height);

    static void main(String[] args) {
        System.setProperty(PROP_DISABLE_ALTERNATE_CHARSET, "true");

        SwingUtilities.invokeLater(Main::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        final var terminalWidget = createTerminalWidget();

        JFrame frame = new JFrame("Void of the Unfathomable");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(terminalWidget);
        frame.setLayout(new GridBagLayout());
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }

    private static JediTermWidget createTerminalWidget() {
        try {
            final PipedOutputStream jediTermOut = new PipedOutputStream();
            final PipedInputStream jlineIn = new PipedInputStream(jediTermOut);

            final PipedOutputStream jlineOut = new PipedOutputStream();
            final PipedInputStream jediTermIn = new PipedInputStream(jlineOut);

            final JLineTtyConnector connector = new JLineTtyConnector(jediTermIn, jediTermOut);

            JediTermWidget widget = new JediTermWidget(
                width,
                height,
                new SettingsProvider()
            );
            widget.setMaximumSize(dimension);
            widget.setMinimumSize(dimension);
            widget.setTtyConnector(connector);
            widget.start();

            // Remove the scroll bar
            if (widget.getComponent(0) instanceof JLayeredPane innerPane &&
                innerPane.getComponent(1) instanceof JScrollBar scrollBar) {
                innerPane.remove(scrollBar);
            } else {
                System.out.println("Warning: Could not remove scroll bar from JediTermWidget.");
            }

            final Terminal jlineTerminal = TerminalBuilder.builder()
                .name("JediTerm")
                .encoding(StandardCharsets.UTF_8)
                .streams(jlineIn, jlineOut)
                .system(false)
                .build();

            connector.setJLineTerminal(jlineTerminal);

            // Set initial terminal size for JLine. JediTermWidget will call
            // connector.resize() on its own.
            connector.resize(
                new TermSize(
                    widget.getTerminal().getSize().getColumns(),
                    widget.getTerminal().getSize().getRows()));

            // Create the application logic and run it in a separate thread
            // to avoid blocking the Swing UI thread.
            final App app = new App(jlineTerminal);
            new Thread(() -> {
                app.run();
                System.exit(0);
            }).start();

            return widget;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

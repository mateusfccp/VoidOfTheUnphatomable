package org.pintoschneider.void_of_the_unfathomable.frontend;

import com.jediterm.terminal.emulator.ColorPalette;
import com.jediterm.terminal.ui.settings.DefaultSettingsProvider;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * Provides custom fonts and colors to the JediTerm widget.
 */
public final class SettingsProvider extends DefaultSettingsProvider {
    private final Font terminalFont;

    /**
     * Creates a new SettingsProvider.
     */
    public SettingsProvider() {
        try (final InputStream fontStream = getClass().getClassLoader().getResourceAsStream("fonts/font.ttf")) {
            if (fontStream == null) {
                terminalFont = new Font(Font.MONOSPACED, Font.PLAIN, 14);
            } else {
                terminalFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(14f);
            }
        } catch (FontFormatException | IOException exception) {
            throw new RuntimeException("Failed to load terminal font.", exception);
        }
    }

    @Override
    public Font getTerminalFont() {
        return terminalFont;
    }

    @Override
    public float getTerminalFontSize() {
        return terminalFont.getSize2D();
    }

    @Override
    public ColorPalette getTerminalColorPalette() {
        return new org.pintoschneider.void_of_the_unfathomable.frontend.ColorPalette();
    }
}

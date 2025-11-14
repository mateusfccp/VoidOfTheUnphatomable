package org.pintoschneider.void_of_the_unfathomable.frontend;

import com.jediterm.core.Color;
import org.jetbrains.annotations.NotNull;

/**
 * The color palette used by the JediTerm terminal emulator.
 */
public class ColorPalette extends com.jediterm.terminal.emulator.ColorPalette {
    private static Color toJediTermColor(org.pintoschneider.void_of_the_unfathomable.ui.core.Color color) {
        return new Color(color.red(), color.green(), color.blue());
    }

    @Override
    protected @NotNull Color getForegroundByColorIndex(int i) {
        final var foreground = org.pintoschneider.void_of_the_unfathomable.game.ColorPalette.WHITE;
        return toJediTermColor(foreground);
    }

    @Override
    protected @NotNull Color getBackgroundByColorIndex(int i) {
        final var background = org.pintoschneider.void_of_the_unfathomable.game.ColorPalette.DEEP_PURPLE;
        return toJediTermColor(background);
    }
}

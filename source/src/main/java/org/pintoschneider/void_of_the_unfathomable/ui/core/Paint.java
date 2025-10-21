package org.pintoschneider.void_of_the_unfathomable.ui.core;

/**
 * A record representing text paint styles for the terminal.
 * <p>
 * All fields have default values. Use {@link #Paint()} for defaults and {@code with*} methods to create modified copies.
 *
 * @param bold            Whether the text is bold.
 * @param italic          Whether the text is italic.
 * @param underline       Whether the text is underlined.
 * @param strikethrough   Whether the text has a strikethrough.
 * @param dim             Whether the text is dimmed. Dimmed text appears lighter than normal text.
 * @param inverted        Whether the text is inverted. Inverted text swaps the foreground and background colors.
 * @param blink           Whether the text is blinking. Blinking text alternates between visible and invisible states.
 * @param foregroundColor The foreground color of the text. If null, the default terminal color is used.
 * @param backgroundColor The background color of the text. If null, the default terminal color is used.
 */
public record Paint(
    boolean bold,
    boolean italic,
    boolean underline,
    boolean strikethrough,
    boolean dim,
    boolean inverted,
    boolean blink,
    Color foregroundColor,
    Color backgroundColor
) {
    /**
     * Constructs a Paint instance with all default values.
     */
    public Paint() {
        this(false, false, false, false, false, false, false, null, null);
    }

    /**
     * Returns a copy of this Paint with the {@code bold} property changed.
     *
     * @param bold Whether the text should be bold.
     * @return a new Paint instance with the updated bold property.
     */
    public Paint withBold(boolean bold) {
        return new Paint(bold, italic, underline, strikethrough, dim, inverted, blink, foregroundColor, backgroundColor);
    }

    /**
     * Returns a copy of this Paint with the {@code italic} property changed.
     *
     * @param italic Whether the text should be italic.
     * @return a new Paint instance with the updated italic property.
     */
    public Paint withItalic(boolean italic) {
        return new Paint(this.bold, italic, underline, strikethrough, dim, inverted, blink, foregroundColor, backgroundColor);
    }

    /**
     * Returns a copy of this Paint with the {@code underline} property changed.
     *
     * @param underline Whether the text should be underlined.
     * @return a new Paint instance with the updated underline property.
     */
    public Paint withUnderline(boolean underline) {
        return new Paint(this.bold, this.italic, underline, strikethrough, dim, inverted, blink, foregroundColor, backgroundColor);
    }

    /**
     * Returns a copy of this Paint with the {@code strikethrough} property changed.
     *
     * @param strikethrough Whether the text should have a strikethrough.
     * @return a new Paint instance with the updated strikethrough property.
     */
    public Paint withStrikethrough(boolean strikethrough) {
        return new Paint(this.bold, this.italic, this.underline, strikethrough, dim, inverted, blink, foregroundColor, backgroundColor);
    }

    /**
     * Returns a copy of this Paint with the {@code dim} property changed.
     *
     * @param dim Whether the text should be dimmed.
     * @return a new Paint instance with the updated dim property.
     */
    public Paint withDim(boolean dim) {
        return new Paint(this.bold, this.italic, this.underline, this.strikethrough, dim, inverted, blink, foregroundColor, backgroundColor);
    }

    /**
     * Returns a copy of this Paint with the {@code inverted} property changed.
     *
     * @param inverted Whether the text should be inverted.
     * @return a new Paint instance with the updated inverted property.
     */
    public Paint withInverted(boolean inverted) {
        return new Paint(this.bold, this.italic, this.underline, this.strikethrough, this.dim, inverted, blink, foregroundColor, backgroundColor);
    }

    /**
     * Returns a copy of this Paint with the {@code blink} property changed.
     *
     * @param blink Whether the text should blink.
     * @return a new Paint instance with the updated blink property.
     */
    public Paint withBlink(boolean blink) {
        return new Paint(this.bold, this.italic, this.underline, this.strikethrough, this.dim, this.inverted, blink, foregroundColor, backgroundColor);
    }

    /**
     * Returns a copy of this Paint with the {@code foregroundColor} property changed.
     *
     * @param foregroundColor The new foreground color, or null for default.
     * @return a new Paint instance with the updated foreground color.
     */
    public Paint withForegroundColor(Color foregroundColor) {
        return new Paint(this.bold, this.italic, this.underline, this.strikethrough, this.dim, this.inverted, this.blink, foregroundColor, backgroundColor);
    }

    /**
     * Returns a copy of this Paint with the {@code backgroundColor} property changed.
     *
     * @param backgroundColor The new background color, or null for default.
     * @return a new Paint instance with the updated background color.
     */
    public Paint withBackgroundColor(Color backgroundColor) {
        return new Paint(this.bold, this.italic, this.underline, this.strikethrough, this.dim, this.inverted, this.blink, this.foregroundColor, backgroundColor);
    }
}

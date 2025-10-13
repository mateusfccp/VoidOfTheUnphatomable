package org.pintoschneider.void_of_the_unfathomable.ui.core;

/**
 * A class representing text paint styles for the terminal.
 */
public final class Paint {
    /**
     * Whether the text is bold.
     */
    public boolean bold = false;

    /**
     * Whether the text is italic.
     */
    public boolean italic = false;

    /**
     * Whether the text is underlined.
     */
    public boolean underline = false;

    /**
     * Whether the text is inverted.
     * <p>
     * Inverted text swaps the foreground and background colors.
     */
    public boolean inverted = false;

    /**
     * Whether the text is blinking.
     * <p>
     * Blinking text alternates between visible and invisible states.
     */
    public boolean blink = false;
}

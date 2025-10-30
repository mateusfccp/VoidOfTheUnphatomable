package org.pintoschneider.void_of_the_unfathomable.ui.core;

/**
 * A record representing a 24-bit RGB color.
 *
 * @param red   The red component of the color.
 * @param green The green component of the color.
 * @param blue  The blue component of the color.
 */
public record Color(short red, short green, short blue) {

    public static final Color RED = new Color((short) 255, (short) 0, (short) 0);
    public static final Color GREEN = new Color((short) 0, (short) 255, (short) 0);

    /**
     * Creates a new Color with the specified red component.
     *
     * @param red The green component of the new color.
     * @return A new Color instance with the specified red component.
     */
    Color withRed(short red) {
        return new Color(red, green, blue);
    }

    /**
     * Creates a new Color with the specified green component.
     *
     * @param green The green component of the new color.
     * @return A new Color instance with the specified green component.
     */
    Color withGreen(short green) {
        return new Color(red, green, blue);
    }

    /**
     * Creates a new Color with the specified blue component.
     *
     * @param blue The blue component of the new color.
     * @return A new Color instance with the specified blue component.
     */
    Color withBlue(short blue) {
        return new Color(red, green, blue);
    }

    /**
     * Converts this Color to a 24-bit integer representation.
     *
     * @return The integer representation of this Color.
     */
    int toInt() {
        return ((red & 0xFF) << 16) | ((green & 0xFF) << 8) | (blue & 0xFF);
    }
}

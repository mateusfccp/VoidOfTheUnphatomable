package org.pintoschneider.void_of_the_unfathomable.ui.core;

/**
 * A record representing a 24-bit RGB color.
 *
 * @param red   The red component of the color.
 * @param green The green component of the color.
 * @param blue  The blue component of the color.
 */
public record Color(short red, short green, short blue) {

    /**
     * The green color constant.
     * <p>
     * Represents the color green with RGB values (0, 255, 0).
     */
    public static final Color GREEN = new Color(0, 255, 0);

    /**
     * THe red color constant.
     * <p>
     * Represents the color red with RGB values (255, 0, 0).
     */
    public static final Color RED = new Color(255, 0, 0);

    /**
     * The white color constant.
     * <p>
     * Represents the color white with RGB values (255, 255, 255).
     */
    public static final Color WHITE = new Color(255, 255, 255);

    /**
     * Creates a new Color with the specified RGB components.
     *
     * @param r The red component of the new color.
     * @param g The green component of the new color.
     * @param b The blue component of the new color.
     */
    public Color(int r, int g, int b) {
        this((short) r, (short) g, (short) b);
    }

    /**
     * Linearly interpolates between two colors.
     *
     * @param start The starting color.
     * @param end   The ending color.
     * @param t     The interpolation factor (0.0 to 1.0).
     * @return The interpolated color.
     */
    public static Color lerp(Color start, Color end, double t) {
        short r = (short) (start.red * (1.0 - t) + end.red * t);
        short g = (short) (start.green * (1.0 - t) + end.green * t);
        short b = (short) (start.blue * (1.0 - t) + end.blue * t);
        return new Color(r, g, b);
    }

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

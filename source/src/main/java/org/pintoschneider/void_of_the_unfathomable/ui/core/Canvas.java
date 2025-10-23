package org.pintoschneider.void_of_the_unfathomable.ui.core;

import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.pintoschneider.void_of_the_unfathomable.core.Offset;
import org.pintoschneider.void_of_the_unfathomable.core.Size;

import java.io.PrintWriter;

/**
 * The {@code Canvas} class represents a 2D grid of characters and optional style objects,
 * used for rendering {@link Component} objects in a terminal or text-based UI.
 * <p>
 * Each cell in the canvas can hold a character and an associated style object.
 * The canvas provides methods to draw characters, draw other {@link Component} objects,
 * and merge other canvases into itself at specified offsets.
 * <p>
 * The canvas can be written to a {@link PrintWriter} for display.
 */
public final class Canvas {
    private final Character[][] tiles;
    private final Paint[][] paints;
    private final int width;
    private final int height;

    /**
     * Constructs a new {@link Canvas} with the specified width and height.
     *
     * @param width  The width of the canvas.
     * @param height The height of the canvas.
     */
    public Canvas(int width, int height) {
        this.width = width;
        this.height = height;
        tiles = new Character[width][height];
        paints = new Paint[width][height];
    }

    /**
     * Constructs a new {@link Canvas} with the specified size.
     *
     * @param size The size of the canvas.
     */
    public Canvas(Size size) {
        this(size.width(), size.height());
    }

    /**
     * Creates a new canvas sized to fit the given component.
     *
     * @param component The component to fit.
     * @return a new canvas sized for the component
     */
    static Canvas forDrawable(Component component) {
        return new Canvas(component.size().width(), component.size().height());
    }

    static AttributedStyle getStyle(Paint paint) {
        AttributedStyle style = AttributedStyle.DEFAULT;

        if (paint.bold()) {
            style = style.bold();
        } else {
            style = style.boldOff();
        }

        if (paint.italic()) {
            style = style.italic();
        } else {
            style = style.italicOff();
        }

        if (paint.underline()) {
            style = style.underline();
        } else {
            style = style.underlineOff();
        }

        if (paint.strikethrough()) {
            style = style.crossedOut();
        } else {
            style = style.crossedOutOff();
        }

        if (paint.dim()) {
            style = style.faint();
        } else {
            style = style.faintOff();
        }

        if (paint.inverted()) {
            style = style.inverse();
        } else {
            style = style.inverseOff();
        }

        if (paint.blink()) {
            style = style.blink();
        } else {
            style = style.blinkOff();
        }

        if (paint.foregroundColor() == null) {
            style = style.foregroundOff();
        } else {
            style = style.foregroundRgb(paint.foregroundColor().toInt());
        }

        if (paint.backgroundColor() == null) {
            style = style.backgroundOff();
        } else {
            style = style.backgroundRgb(paint.backgroundColor().toInt());
        }

        return style;
    }

    /**
     * Draws a {@link Component} object onto this canvas at the specified position.
     * <p>
     * The component is rendered onto a temporary canvas and then merged into this canvas.
     *
     * @param component The {@link Component} to render.
     * @param x         The x-coordinate to draw at.
     * @param y         The y-coordinate to draw at.
     */
    public void draw(Component component, int x, int y) {
        if (component.size() == null) {
            throw new IllegalStateException("Component size is null. Did you forget to call layout()?");
        }

        final Canvas canvas = forDrawable(component);
        component.draw(canvas);
        merge(canvas, x, y);
    }

    /**
     * Draws a {@link Component} object onto this canvas at the specified offset.
     *
     * @param component The {@link Component} to render.
     * @param offset    The {@link Offset} to draw at.
     */
    public void draw(Component component, Offset offset) {
        draw(component, offset.dx(), offset.dy());
    }

    /**
     * Draws a character at the specified position with no style.
     *
     * @param c the character to draw
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public void draw(char c, int x, int y) {
        draw(c, x, y, null);
    }

    /**
     * Draws a character at the specified offset with no style.
     *
     * @param c      The character to draw.
     * @param offset The offset to draw at.
     */
    public void draw(char c, Offset offset) {
        draw(c, offset.dx(), offset.dy(), null);
    }

    /**
     * Draws a character at the specified position with an optional style.
     *
     * @param c     The character to draw.
     * @param x     The x-coordinate.
     * @param y     The y-coordinate.
     * @param paint The paint style to apply, or null for no style.
     */
    public void draw(char c, int x, int y, Paint paint) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            final String errorMessage = "Coordinates out of bounds: (%d, %d) for canvas of size (%d, %d).".formatted(x, y, width, height);
            throw new IndexOutOfBoundsException(errorMessage);
        }

        tiles[x][y] = c;
        paints[x][y] = paint;
    }

    /**
     * Merges another canvas into this canvas at the specified offset.
     * <p>
     * Only non-null characters and styles from the other canvas are copied.
     *
     * @param other   The canvas to merge.
     * @param xOffset The x offset to merge at.
     * @param yOffset The y offset to merge at.
     */
    void merge(Canvas other, int xOffset, int yOffset) {
        for (int x = 0; x < other.width; x++) {
            for (int y = 0; y < other.height; y++) {
                if (other.tiles[x][y] != null) {
                    if (x + xOffset < 0 || x + xOffset >= width || y + yOffset < 0 || y + yOffset >= height) {
                        continue; // Discard for now
                    }

                    tiles[x + xOffset][y + yOffset] = other.tiles[x][y];
                    paints[x + xOffset][y + yOffset] = other.paints[x][y];
                }
            }
        }
    }

    /**
     * Writes the contents of this canvas to the given {@link PrintWriter}.
     * <p>
     * Each row is written in order, with null cells rendered as spaces.
     *
     * @param writer The writer to output to.
     */
    public void writeTo(PrintWriter writer) {
        for (int y = 0; y < tiles[0].length; y++) {
            for (int x = 0; x < tiles.length; x++) {
                final Character tile = tiles[x][y];
                final Paint paint = paints[x][y];

                final Character c = tile == null ? ' ' : tile;

                if (paint != null) {
                    final AttributedStyle style = getStyle(paint);
                    final AttributedStringBuilder builder = new AttributedStringBuilder();
                    builder.style(style);
                    builder.append(c);
                    writer.print(builder.toAnsi());
                } else {
                    writer.print(c);
                }
            }
        }
    }
}

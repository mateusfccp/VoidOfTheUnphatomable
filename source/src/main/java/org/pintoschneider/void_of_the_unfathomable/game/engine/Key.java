package org.pintoschneider.void_of_the_unfathomable.game.engine;

import java.util.Arrays;

/**
 * A utility class that holds key code constants for common keyboard keys.
 */
public final class Key {
    /**
     * Key code for the "Up" arrow key.
     */
    public static final Key UP = new Key(new char[]{27, 91, 65}, "↑");
    /**
     * Key code for the "Down" arrow key.
     */
    public static final Key DOWN = new Key(new char[]{27, 91, 66}, "↓");
    /**
     * Key code for the "Right" arrow key.
     */
    public static final Key RIGHT = new Key(new char[]{27, 91, 67}, "→");
    /**
     * Key code for the "Left" arrow key.
     */
    public static final Key LEFT = new Key(new char[]{27, 91, 68}, "←");
    /**
     * Key code for the "Enter" key.
     */
    public static final Key ENTER = new Key(new char[]{13}, "↩");

    // private list of known keys used by parse()
    private static final Key[] KNOWN_KEYS = {UP, DOWN, RIGHT, LEFT, ENTER};

    final char[] chars;
    final String representation;

    private Key(char[] chars, String representation) {
        this.chars = chars;
        this.representation = representation;
    }

    /**
     * Factory for UNKNOWN keys that can carry any char[].
     */
    private static Key UNKNOWN(char[] chars) {
        return new Key(chars == null ? new char[]{} : chars, "UNKNOWN");
    }

    /**
     * Parses a sequence of key codes and returns the corresponding Key value.
     *
     * @param keyCodes The sequence of key codes to parse.
     * @return The corresponding Key value, or an UNKNOWN Key carrying the provided chars if no match is found.
     */
    public static Key parse(char[] keyCodes) {
        if (keyCodes == null) {
            return UNKNOWN(new char[]{});
        }

        for (Key key : KNOWN_KEYS) {
            if (Arrays.equals(key.chars, keyCodes)) {
                return key;
            }
        }

        return UNKNOWN(keyCodes);
    }

    /**
     * Checks if this Key is UNKNOWN.
     *
     * @return True if this Key is UNKNOWN, false otherwise.
     */
    public boolean isUnknown() {
        return "UNKNOWN".equals(representation);
    }

    @Override
    public String toString() {
        final int[] codes = new int[chars.length];

        for (int i = 0; i < chars.length; i++) {
            codes[i] = chars[i];
        }

        return "%s (%s)".formatted(representation, Arrays.toString(codes));
    }
}

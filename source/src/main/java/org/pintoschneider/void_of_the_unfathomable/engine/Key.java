package org.pintoschneider.void_of_the_unfathomable.engine;

import org.jline.keymap.KeyMap;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp.Capability;

import static org.jline.utils.InfoCmp.Capability.*;

/**
 * A utility class that holds key code constants for common keyboard keys.
 */
public enum Key {
    /**
     * The Up Arrow key.
     */
    UP(key_up),

    /**
     * The Down Arrow key.
     */
    DOWN(key_down),

    /**
     * The Left Arrow key.
     */
    LEFT(key_left),

    /**
     * The Right Arrow key.
     */
    RIGHT(key_right),

    /**
     * The Enter key.
     */
    ENTER(new Capability[]{key_enter}, new CharSequence[]{"\r", "\n"}),

    /**
     * The Escape key.
     */
    ESC("\033"),

    /**
     * The Backspace key.
     */
    BACKSPACE(new Capability[]{key_backspace}, new CharSequence[]{"\177"}),

    /**
     * The 'I' key.
     */
    I("i"),

    /**
     * The 'C' key.
     */
    C("c");

    final Capability[] capabilities;
    final CharSequence[] sequences;

    Key(Capability capability) {
        capabilities = new Capability[]{capability};
        sequences = new CharSequence[0];
    }

    Key(CharSequence code) {
        capabilities = new Capability[0];
        sequences = new CharSequence[]{code};
    }

    Key(Capability[] capabilities, CharSequence[] codes) {
        this.capabilities = capabilities;
        this.sequences = codes;
    }


    static KeyMap<Key> createKeyMap(Terminal terminal) {
        final KeyMap<Key> keyMap = new KeyMap<>();
        keyMap.setAmbiguousTimeout(100);

        for (Key key : values()) {
            for (Capability capability : key.capabilities) {
                final CharSequence code = KeyMap.key(terminal, capability);
                keyMap.bind(key, code);
            }

            for (CharSequence sequence : key.sequences) {
                keyMap.bind(key, sequence);
            }
        }

        return keyMap;
    }
}

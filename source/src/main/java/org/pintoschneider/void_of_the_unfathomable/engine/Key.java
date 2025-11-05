package org.pintoschneider.void_of_the_unfathomable.engine;

import org.jline.keymap.KeyMap;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp.Capability;

import static org.jline.utils.InfoCmp.Capability.*;

/**
 * A utility class that holds key code constants for common keyboard keys.
 */
public enum Key {
    UP(key_up),
    DOWN(key_down),
    LEFT(key_left),
    RIGHT(key_right),
    ENTER(new Capability[]{key_enter}, new CharSequence[]{"\r", "\n"}),
    I("i");

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

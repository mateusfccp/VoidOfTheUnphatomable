package org.pintoschneider.void_of_the_unfathomable.game.scenes;

import org.pintoschneider.void_of_the_unfathomable.engine.Engine;
import org.pintoschneider.void_of_the_unfathomable.engine.Key;
import org.pintoschneider.void_of_the_unfathomable.engine.Scene;
import org.pintoschneider.void_of_the_unfathomable.ui.components.Align;
import org.pintoschneider.void_of_the_unfathomable.ui.components.Column;
import org.pintoschneider.void_of_the_unfathomable.ui.components.SizedBox;
import org.pintoschneider.void_of_the_unfathomable.ui.components.Text;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Alignment;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Component;
import org.pintoschneider.void_of_the_unfathomable.ui.core.CrossAxisAlignment;
import org.pintoschneider.void_of_the_unfathomable.ui.core.MainAxisSize;

/**
 * A scene representing the game over screen.
 */
public class GameOver implements Scene {
    static private final String base = """
        ▄▖         ▄▖
        ▌ ▀▌▛▛▌█▌  ▌▌▌▌█▌▛▘
        ▙▌█▌▌▌▌▙▖  ▙▌▚▘▙▖▌
        """;

    @Override
    public Component build() {
        return new Align(
            Alignment.CENTER,
            new Column(
                new Text(base),
                new SizedBox(0, 1),
                new Text("Presiona ENTER para volver al menú principal.")
            ).mainAxisSize(MainAxisSize.MIN)
                .crossAxisAlignment(CrossAxisAlignment.CENTER)
        );
    }

    @Override
    public void onKeyPress(Key key) {
        if (key == Key.ENTER) {
            Engine.context().sceneManager().pop();
        }
    }
}

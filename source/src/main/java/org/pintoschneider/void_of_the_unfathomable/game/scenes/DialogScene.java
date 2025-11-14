package org.pintoschneider.void_of_the_unfathomable.game.scenes;

import org.pintoschneider.void_of_the_unfathomable.engine.Engine;
import org.pintoschneider.void_of_the_unfathomable.engine.Key;
import org.pintoschneider.void_of_the_unfathomable.engine.Scene;
import org.pintoschneider.void_of_the_unfathomable.ui.components.*;
import org.pintoschneider.void_of_the_unfathomable.ui.core.*;

import java.util.function.Consumer;

/**
 * A scene that displays a dialog message to the player.
 */
public final class DialogScene implements Scene {
    private final Alignment alignment;
    private final String message;
    private final Consumer<Void> onClose;

    /**
     * Creates a new dialog scene with the specified message.
     *
     * @param message   The message to display in the dialog.
     * @param alignment The alignment of the dialog on the screen.
     */
    public DialogScene(String message, Alignment alignment) {
        this(message, alignment, null);
    }

    /**
     * Creates a new dialog scene with the specified message and a callback for when the dialog is closed.
     *
     * @param message   The message to display in the dialog.
     * @param alignment The alignment of the dialog on the screen.
     * @param onClose   A callback function to be executed when the dialog is closed.
     */
    public DialogScene(String message, Alignment alignment, Consumer<Void> onClose) {
        this.message = message;
        this.alignment = alignment;
        this.onClose = onClose;
    }

    @Override
    public Component build() {
        return new Padding(
            EdgeInsets.symmetric(2, 4),
            new Align(
                alignment,
                new ConstrainedBox(
                    new Constraints(1, 60, 1, null),
                    new Box(
                        Border.SINGLE,
                        new Text(message)
                    )
                )
            )
        );
    }

    @Override
    public void onKeyPress(Key key) {
        if (key == Key.ENTER) {
            Engine.context().sceneManager().pop();
            if (onClose != null) {
                onClose.accept(null);
            }
        }
    }
}

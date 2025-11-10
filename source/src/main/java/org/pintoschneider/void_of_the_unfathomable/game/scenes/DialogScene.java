package org.pintoschneider.void_of_the_unfathomable.game.scenes;

import org.pintoschneider.void_of_the_unfathomable.engine.Engine;
import org.pintoschneider.void_of_the_unfathomable.engine.Key;
import org.pintoschneider.void_of_the_unfathomable.engine.Scene;
import org.pintoschneider.void_of_the_unfathomable.ui.components.Box;
import org.pintoschneider.void_of_the_unfathomable.ui.components.Padding;
import org.pintoschneider.void_of_the_unfathomable.ui.components.Text;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Border;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Component;
import org.pintoschneider.void_of_the_unfathomable.ui.core.EdgeInsets;

import java.util.function.Consumer;

/**
 * A scene that displays a dialog message to the player.
 */
public final class DialogScene implements Scene {
    private final String message;
    private final Consumer<Void> onClose;

    /**
     * Creates a new dialog scene with the specified message.
     *
     * @param message The message to display in the dialog.
     */
    public DialogScene(String message) {
        this(message, null);
    }

    /**
     * Creates a new dialog scene with the specified message and a callback for when the dialog is closed.
     *
     * @param message The message to display in the dialog.
     * @param onClose A callback function to be executed when the dialog is closed.
     */
    public DialogScene(String message, Consumer<Void> onClose) {
        this.message = message;
        this.onClose = onClose;
    }

    @Override
    public Component build() {
        return new Padding(
            EdgeInsets.symmetric(2, 4),
            new Box(
                Border.SINGLE_ROUNDED,
                new Text(message)
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

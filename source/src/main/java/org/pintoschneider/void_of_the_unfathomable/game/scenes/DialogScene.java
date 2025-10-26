package org.pintoschneider.void_of_the_unfathomable.game.scenes;

import org.pintoschneider.void_of_the_unfathomable.game.engine.Engine;
import org.pintoschneider.void_of_the_unfathomable.game.engine.Key;
import org.pintoschneider.void_of_the_unfathomable.game.engine.Scene;
import org.pintoschneider.void_of_the_unfathomable.ui.components.Box;
import org.pintoschneider.void_of_the_unfathomable.ui.components.Padding;
import org.pintoschneider.void_of_the_unfathomable.ui.components.Text;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Border;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Component;
import org.pintoschneider.void_of_the_unfathomable.ui.core.EdgeInsets;

import java.util.function.Consumer;

public final class DialogScene implements Scene {
    final String message;
    final Consumer<Void> onClose;

    int pageCount;

    public DialogScene(String message) {
        this(message, null);
    }

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
        Engine.context().sceneManager().pop();
        if (onClose != null) {
            onClose.accept(null);
        }
    }
}

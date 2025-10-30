package org.pintoschneider.void_of_the_unfathomable.game.scenes;

import org.pintoschneider.void_of_the_unfathomable.engine.Engine;
import org.pintoschneider.void_of_the_unfathomable.ui.components.Align;
import org.pintoschneider.void_of_the_unfathomable.ui.components.ConstrainedBox;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Alignment;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Component;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Constraints;
import org.pintoschneider.void_of_the_unfathomable.ui.core.CrossAxisAlignment;

import java.util.List;

public final class MainMenu extends SelectionScene {
    @Override
    List<Option> options() {
        return List.of(
            new Option("Nuevo Juego", () -> Engine.context().sceneManager().push(new InGame())),
            new Option("Cargar Juego"),
            new Option("Configuraciones"),
            new Option("Salir", () -> Engine.context().sceneManager().pop())
        );
    }

    @Override
    protected CrossAxisAlignment alignment() {
        return CrossAxisAlignment.CENTER;
    }

    @Override
    public Component build() {
        return
            new Align(
                Alignment.CENTER,
                new ConstrainedBox(
                    new Constraints(null, 20, null, null),
                    super.build()
                )
            );
    }
}

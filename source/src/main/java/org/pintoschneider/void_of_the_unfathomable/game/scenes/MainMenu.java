package org.pintoschneider.void_of_the_unfathomable.game.scenes;

import org.pintoschneider.void_of_the_unfathomable.game.engine.Context;
import org.pintoschneider.void_of_the_unfathomable.game.engine.Engine;
import org.pintoschneider.void_of_the_unfathomable.game.engine.Key;
import org.pintoschneider.void_of_the_unfathomable.game.engine.Scene;
import org.pintoschneider.void_of_the_unfathomable.ui.components.*;
import org.pintoschneider.void_of_the_unfathomable.ui.core.*;

import java.util.Arrays;

enum MenuOption {
    NEW_GAME("Nuevo Juego"),
    LOAD_GAME("Cargar Juego"),
    SETTINGS("Configuraciones"),
    EXIT("Salir");

    private final String text;

    MenuOption(String text) {
        this.text = text;
    }

    public String text() {
        return text;
    }
}

public final class MainMenu implements Scene {
    MenuOption selectedOption = MenuOption.NEW_GAME;
    int frame = 0;

    @Override
    public Component build() {
        frame = (frame + 1) % Integer.MAX_VALUE;

        final Paint selectedTextPaint = new Paint().withInverted(true);

        Component[] menuItems = Arrays.stream(MenuOption.values())
            .map(option ->
                new ConstrainedBox(
                    new Constraints(null, null, 1, 1),
                    new Align(
                        Alignment.CENTER,
                        new Text(
                            option.text(),
                            option == selectedOption ? selectedTextPaint : null
                        )
                    )
                )
            ).toArray(Component[]::new);

        return
            new Align(
                Alignment.CENTER,
                new ConstrainedBox(
                    new Constraints(null, 20, null, null),
                    new Border(
                        new Column(menuItems)
                            .crossAxisAlignment(CrossAxisAlignment.STRETCH)
                            .mainAxisSize(MainAxisSize.MIN)
                    )
                )
            );
    }

    @Override
    public void onKeyPress(Key key) {
        if (key == Key.UP) {
            if (selectedOption.ordinal() > 0) {
                selectedOption = MenuOption.values()[selectedOption.ordinal() - 1];
            }
        } else if (key == Key.DOWN) {
            if (selectedOption.ordinal() < MenuOption.values().length - 1) {
                selectedOption = MenuOption.values()[selectedOption.ordinal() + 1];
            }
        } else if (key == Key.ENTER) {
            handleSelection();
        }
    }

    private void handleSelection() {
        switch (selectedOption) {
            case NEW_GAME -> Engine.context().sceneManager().push(new InGame());
            case LOAD_GAME -> {
                // Transition to the load game scene
            }
            case SETTINGS -> {
                // Transition to the settings scene
            }
            case EXIT -> Engine.context().sceneManager().pop();
        }
    }

    @Override
    public String toString() {
        return "MainMenu";
    }
}

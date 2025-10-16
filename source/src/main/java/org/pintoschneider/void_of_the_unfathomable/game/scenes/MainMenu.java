package org.pintoschneider.void_of_the_unfathomable.game.scenes;

import org.pintoschneider.void_of_the_unfathomable.game.core.Context;
import org.pintoschneider.void_of_the_unfathomable.game.core.Keys;
import org.pintoschneider.void_of_the_unfathomable.game.core.Scene;
import org.pintoschneider.void_of_the_unfathomable.ui.components.*;
import org.pintoschneider.void_of_the_unfathomable.ui.components.LinearLayout.*;
import org.pintoschneider.void_of_the_unfathomable.ui.core.*;

import java.util.Arrays;

public final class MainMenu implements Scene {
    MenuOption selectedOption = MenuOption.NEW_GAME;
    int frame = 0;

    @Override
    public Component build(Context context) {
        frame = (frame + 1) % Integer.MAX_VALUE;

        final Paint selectedTextPaint = new Paint();
        selectedTextPaint.inverted = true;

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
                new Border(
                    new SizedBox(
                        20, 4,
                        new Column(menuItems)
                    )
                )
            );
    }

    @Override
    public void onKeyPress(Context context, int keyCode) {
        switch (keyCode) {
            case 'w', Keys.UP -> { // Up
                if (selectedOption.ordinal() > 0) {
                    selectedOption = MenuOption.values()[selectedOption.ordinal() - 1];
                }
            }
            case 's', Keys.DOWN -> { // Down
                if (selectedOption.ordinal() < MenuOption.values().length - 1) {
                    selectedOption = MenuOption.values()[selectedOption.ordinal() + 1];
                }
            }
            case Keys.ENTER -> handleSelection(context); // Enter
        }
    }

    private void handleSelection(Context context) {
        switch (selectedOption) {
            case NEW_GAME -> context.sceneManager().push(new InGame());
            case LOAD_GAME -> {
                // Transition to the load game scene
            }
            case SETTINGS -> {
                // Transition to the settings scene
            }
            case EXIT -> context.sceneManager().pop();
        }
    }

    @Override
    public String toString() {
        return "MainMenu";
    }
}

enum MenuOption {
    NEW_GAME("Nuevo Juego"),
    LOAD_GAME("Cargar Juego"),
    SETTINGS("Configuraciones"),
    EXIT("Salir");

    MenuOption(String text) {
        this.text = text;
    }

    private final String text;

    public String text() {
        return text;
    }
}
package org.pintoschneider.void_of_the_unfathomable.game.scenes;

import org.pintoschneider.void_of_the_unfathomable.game.core.Context;
import org.pintoschneider.void_of_the_unfathomable.game.core.Scene;
import org.pintoschneider.void_of_the_unfathomable.ui.components.*;
import org.pintoschneider.void_of_the_unfathomable.ui.components.LinearLayout.*;
import org.pintoschneider.void_of_the_unfathomable.ui.core.*;

import java.util.Arrays;

public final class MainMenu implements Scene {
    MenuOption selectedOption = MenuOption.START_GAME;
    int frame = 0;

    @Override
    public Component build(Context context) {
        frame = (frame + 1) % Integer.MAX_VALUE;

        final Paint selectedTextPaint = new Paint();
        selectedTextPaint.inverted = true;

        Item[] menuItems = Arrays.stream(MenuOption.values())
                .map(option ->
                        new Intrinsic(
                                new ConstrainedBox(
                                        new Constraints(null, null, 1, 1),
                                        new Align(
                                                Alignment.CENTER,
                                                new Text(
                                                        option.getText(),
                                                        option == selectedOption ? selectedTextPaint : null
                                                )
                                        )
                                )
                        )
                )
                .toArray(Item[]::new);

        return new Column(
                new Flexible(
                        1,
                        new Align(
                                Alignment.CENTER,
                                new Border(
                                        new SizedBox(
                                                20, 4,
                                                new Column(menuItems)
                                        )
                                )
                        )
                ),
                new Intrinsic(
                        new IdleSpinner(frame)
                )
        );
    }

    @Override
    public void onKeyPress(Context context, int keyCode) {
        switch (keyCode) {
            case 'w', 'W', 65 -> { // Up
                if (selectedOption.ordinal() > 0) {
                    selectedOption = MenuOption.values()[selectedOption.ordinal() - 1];
                }
            }
            case 's', 'S', 66 -> { // Down
                if (selectedOption.ordinal() < MenuOption.values().length - 1) {
                    selectedOption = MenuOption.values()[selectedOption.ordinal() + 1];
                }
            }
            case 10, 13 -> { // Enter
                handleSelection(context);
            }
            default -> {
                // Ignore other keys
            }
        }
    }

    private void handleSelection(Context context) {
        switch (selectedOption) {
            case START_GAME -> {
                // Transition to the game scene
            }
            case LOAD_GAME -> {
                // Transition to the load game scene
            }
            case SETTINGS -> {
                // Transition to the settings scene
            }
            case EXIT -> {
                context.sceneManager().pop();
            }
        }
    }

    @Override
    public String toString() {
        return "MainMenu";
    }
}

enum MenuOption {
    START_GAME("Start Game"),
    LOAD_GAME("Load Game"),
    SETTINGS("Settings"),
    EXIT("Exit");

    MenuOption(String text) {
        this.text = text;
    }

    private final String text;

    public String getText() {
        return text;
    }
}
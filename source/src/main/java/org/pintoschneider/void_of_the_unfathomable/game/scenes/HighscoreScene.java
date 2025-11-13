package org.pintoschneider.void_of_the_unfathomable.game.scenes;

import org.pintoschneider.void_of_the_unfathomable.engine.Engine;
import org.pintoschneider.void_of_the_unfathomable.engine.Key;
import org.pintoschneider.void_of_the_unfathomable.engine.Scene;
import org.pintoschneider.void_of_the_unfathomable.game.ColorPalette;
import org.pintoschneider.void_of_the_unfathomable.game.highscore.HighscoreEntry;
import org.pintoschneider.void_of_the_unfathomable.game.highscore.HighscoreManager;
import org.pintoschneider.void_of_the_unfathomable.game.highscore.RunStatus;
import org.pintoschneider.void_of_the_unfathomable.ui.components.*;
import org.pintoschneider.void_of_the_unfathomable.ui.core.*;

import java.util.ArrayList;
import java.util.List;

/**
 * A scene that shows the list of high-scores
 */
public class HighscoreScene implements Scene {
    private final HighscoreManager manager;

    public HighscoreScene(HighscoreManager manager) {
        this.manager = manager;
    }

    static Component entryToComponent(HighscoreEntry entry) {
        return new Row(
            new ConstrainedBox(
                new Constraints(12, 12, null, null),
                statusToComponent(entry.status())
            ),
            new SizedBox(1, 0),
            new ConstrainedBox(
                new Constraints(12, 12, null, null),
                new Text("%d%%".formatted(entry.percentage()))
            ),
            new SizedBox(1, 0),
            new ConstrainedBox(
                new Constraints(12, 12, null, null),
                new Text(String.valueOf(entry.turnCount()))
            )
        ).mainAxisSize(MainAxisSize.MIN);
    }

    static private Component statusToComponent(RunStatus status) {
        return switch (status) {
            case FORSAKEN -> new Text(
                "Abandonado",
                new Paint().withForegroundColor(ColorPalette.VERMILION)
            );
            case COWARD -> new Text(
                "Cobarde",
                new Paint().withForegroundColor(ColorPalette.VERMILION)
            );
            case DECEASED -> new Text(
                "Muerto",
                new Paint().withForegroundColor(ColorPalette.BANANA)
            );
            case VICTORIOUS -> new Text(
                "Victoria",
                new Paint().withForegroundColor(ColorPalette.EMERALD)
            );
        };
    }

    @Override
    public Component build() {
        final List<Component> content = new ArrayList<>();

        content.add(
            new Row(
                new ConstrainedBox(
                    new Constraints(12, 12, null, null),
                    new Text("Status", Paint.BOLD)
                ),
                new SizedBox(1, 0),
                new ConstrainedBox(
                    new Constraints(12, 12, null, null),
                    new Text("Porcentage", Paint.BOLD)
                ),
                new SizedBox(1, 0),
                new ConstrainedBox(
                    new Constraints(12, 12, null, null),
                    new Text("Turnos", Paint.BOLD)
                )
            ).mainAxisSize(MainAxisSize.MIN)
        );

        content.add(
            new SizedBox(0, 1)
        );

        content.addAll(
            manager
                .entries()
                .stream()
                .map(HighscoreScene::entryToComponent)
                .toList()
        );

        return new Box(
            new Align(
                Alignment.CENTER,
                new Column(
                    new Box(
                        Border.SINGLE_ROUNDED,
                        new Text("Puntaje Máximo", Paint.BOLD)
                    ),
                    new SizedBox(0, 1),
                    new Box(
                        Border.SINGLE_ROUNDED,
                        new Padding(
                            EdgeInsets.all(1),
                            new Column(
                                content.toArray(new Component[0])
                            ).mainAxisSize(MainAxisSize.MIN)
                        )
                    )
                ).crossAxisAlignment(CrossAxisAlignment.CENTER)
                    .mainAxisSize(MainAxisSize.MIN)
            )
        );
    }

    @Override
    public void onKeyPress(Key key) {
        if (key == Key.ENTER) {
            Engine.context().sceneManager().pop();
        }
    }
}

package org.pintoschneider.void_of_the_unfathomable.game.scenes;

import org.pintoschneider.void_of_the_unfathomable.core.Offset;
import org.pintoschneider.void_of_the_unfathomable.engine.Context;
import org.pintoschneider.void_of_the_unfathomable.engine.Engine;
import org.pintoschneider.void_of_the_unfathomable.engine.Key;
import org.pintoschneider.void_of_the_unfathomable.engine.Scene;
import org.pintoschneider.void_of_the_unfathomable.game.Player;
import org.pintoschneider.void_of_the_unfathomable.game.components.MapComponent;
import org.pintoschneider.void_of_the_unfathomable.game.entities.*;
import org.pintoschneider.void_of_the_unfathomable.game.items.Equippable;
import org.pintoschneider.void_of_the_unfathomable.game.items.equippables.Stickool;
import org.pintoschneider.void_of_the_unfathomable.game.items.key_items.ResoundingCore;
import org.pintoschneider.void_of_the_unfathomable.game.map.Map;
import org.pintoschneider.void_of_the_unfathomable.game.turn_steps.TurnStep;
import org.pintoschneider.void_of_the_unfathomable.ui.components.*;
import org.pintoschneider.void_of_the_unfathomable.ui.core.*;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;

/**
 * The in-game scene where the player can explore the map and interact with the game world.
 */
public final class InGame implements Scene {
    static private final Offset verticalOffset = new Offset(0, 1);
    static private final Offset horizontalOffset = new Offset(1, 0);
    private final Map map = new Map();
    private final Player player = new Player();
    private final Entity<Player> playerEntity = new PlayerEntity(new Offset(4, 4), player, map);
    private Offset mapOffset = Offset.ZERO;

    // Turn-related variables
    static private final int turnStepInterval = 100_000_000; // 0.1 seconds per turn
    private boolean processingTurn = false;
    private long timeSinceLastTurnStep = 0;
    private Boolean lastStepResult = null;
    final Queue<TurnStep> turnSteps = new ArrayDeque<>();

    /**
     * Creates a new in-game scene.
     */
    InGame() {
        final Equippable initialWeapon = new Stickool();
        player.addItemToInventory(initialWeapon);
        player.equipItem(initialWeapon);

        // Add entity for testing purposes
        new StaticDissonanceEntity(new Offset(14, 9), map);
        new StaticDissonanceEntity(new Offset(15, 10), map);
        new ItemEntity(new Offset(6, 6), new ResoundingCore(), map);
        new StairEntity(new Offset(4, 2), map);
    }

    @Override
    public void onEnter() {
        Engine.context().sceneManager().push(
            new DialogScene(
                "Finalmente entraste en El Vacío. Apenas puedes distinguir formas en la distancia. Te cuesta respirar, y una sensación de temor llena tu mente. Debés encontrar el Núcleo Resonante antes de que El Vacío te consuma por completo."
            )
        );
    }

    @Override
    public Component build() {
        centerOnPlayer(Engine.context());

        return new Row(
            new Flexible(
                new Stack(
                    // Use a box to fill the background
                    new ConstrainedBox(
                        Constraints.expand(),
                        new Box()
                    ),
                    new MapComponent(map, mapOffset, playerEntity.position())
                )
            ),
            new VerticalDivider(),
            new ConstrainedBox(
                new Constraints(12, 12, null, null),
                new Column(
                    new Padding(
                        EdgeInsets.all(1),
                        new Column(
                            new ConstrainedBox(
                                new Constraints(null, null, 1, 1),
                                new Row(
                                    new Text("SP:", Paint.BOLD),
                                    new Text("%3d/%3d".formatted(player.currentHealth(), player.maximumHealth()))
                                )
                            ),
                            new ConstrainedBox(
                                new Constraints(null, null, 1, 1),
                                new Row(
                                    new Text("CP:", Paint.BOLD),
                                    new Text("%3d/%3d".formatted(player.currentColorPoints(), player.maximumColorPoints()))
                                )
                            )
                        ).crossAxisAlignment(CrossAxisAlignment.STRETCH)
                            .mainAxisSize(MainAxisSize.MIN)
                    )
                ).crossAxisAlignment(CrossAxisAlignment.STRETCH)
            )
        ).crossAxisAlignment(CrossAxisAlignment.STRETCH);
    }

    @Override
    public void onKeyPress(Key key) {
        if (processingTurn) return;

        if (key == Key.UP) {
            playerEntity.moveBy(verticalOffset.multiply(-1));
            startTurnProcessing();
        } else if (key == Key.DOWN) {
            playerEntity.moveBy(verticalOffset);
            startTurnProcessing();
        } else if (key == Key.LEFT) {
            playerEntity.moveBy(horizontalOffset.multiply(-1));
            startTurnProcessing();
        } else if (key == Key.RIGHT) {
            playerEntity.moveBy(horizontalOffset);
            startTurnProcessing();
        } else if (key == Key.I) {
            final CompletableFuture<Boolean> didConsumeItem = Engine.context().sceneManager().push(new Inventory(player));
            didConsumeItem.thenAccept(consumed -> {
                if (consumed) {
                    startTurnProcessing();
                }
            });
        }
    }

    @Override
    public void onUpdate(long deltaTime) {
        if (!processingTurn) return;

        if (turnSteps.isEmpty()) {
            processingTurn = false;
            return;
        }

        timeSinceLastTurnStep = timeSinceLastTurnStep + deltaTime;

        if (timeSinceLastTurnStep > turnStepInterval) { // 1 second
            timeSinceLastTurnStep = timeSinceLastTurnStep - turnStepInterval;

            final TurnStep step = turnSteps.poll();

            assert step != null;
            lastStepResult = step.execute(lastStepResult);
        }
    }

    private void startTurnProcessing() {
        if (processingTurn) return;

        processingTurn = true;
        timeSinceLastTurnStep = 0;

        for (final Entity<?> entity : map.entities()) {
            final List<TurnStep> enemyTurnSteps = entity.processTurn();
            turnSteps.addAll(enemyTurnSteps);
        }
    }

    private void centerOnPlayer(Context context) {
        mapOffset = new Offset(
            playerEntity.position().dx() - context.size().width() / 2,
            playerEntity.position().dy() - context.size().height() / 2
        );
    }
}

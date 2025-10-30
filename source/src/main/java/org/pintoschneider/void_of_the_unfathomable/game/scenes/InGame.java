package org.pintoschneider.void_of_the_unfathomable.game.scenes;

import org.pintoschneider.void_of_the_unfathomable.core.Offset;
import org.pintoschneider.void_of_the_unfathomable.game.Player;
import org.pintoschneider.void_of_the_unfathomable.game.components.MapComponent;
import org.pintoschneider.void_of_the_unfathomable.game.engine.Context;
import org.pintoschneider.void_of_the_unfathomable.game.engine.Engine;
import org.pintoschneider.void_of_the_unfathomable.game.engine.Key;
import org.pintoschneider.void_of_the_unfathomable.game.engine.Scene;
import org.pintoschneider.void_of_the_unfathomable.game.entities.*;
import org.pintoschneider.void_of_the_unfathomable.game.items.Equippable;
import org.pintoschneider.void_of_the_unfathomable.game.items.Item;
import org.pintoschneider.void_of_the_unfathomable.game.items.consumables.HaloperidolAmpoule;
import org.pintoschneider.void_of_the_unfathomable.game.items.equippables.Stickool;
import org.pintoschneider.void_of_the_unfathomable.game.map.Map;
import org.pintoschneider.void_of_the_unfathomable.game.turn_steps.TurnStep;
import org.pintoschneider.void_of_the_unfathomable.ui.components.*;
import org.pintoschneider.void_of_the_unfathomable.ui.core.*;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

/**
 * The in-game scene where the player can explore the map and interact with the game world.
 */
public final class InGame implements Scene {
    static private final Offset verticalOffset = new Offset(0, 1);
    static private final Offset horizontalOffset = new Offset(1, 0);
    static private final int turnStepInterval = 100_000_000; // 0.1 seconds per turn
    private final Map map = new Map();
    private final Player player = new Player();
    private final Entity<Player> playerEntity = new PlayerEntity(new Offset(4, 4), player, map);
    boolean processingTurn = false;
    private Offset mapOffset = Offset.ZERO;

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
        new ItemEntity(new Offset(6, 6), new HaloperidolAmpoule(), map);
        new StairEntity(new Offset(4, 2), map);
    }

    @Override
    public void onEnter() {
        Engine.context().sceneManager().push(
            new DialogScene(
                "You have entered The Void. You can barely make out shapes in the distance. It's hard to breathe, and a sense of dread fills your mind. You must find the Resounding Core before The Void consumes you entirely."
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
            processTurn();
        } else if (key == Key.DOWN) {
            playerEntity.moveBy(verticalOffset);
            processTurn();
        } else if (key == Key.LEFT) {
            playerEntity.moveBy(horizontalOffset.multiply(-1));
            processTurn();
        } else if (key == Key.RIGHT) {
            playerEntity.moveBy(horizontalOffset);
            processTurn();
        } else if (key == Key.I) {
            Engine.context().sceneManager().push(new Inventory(player));
        }
    }

    private void processTurn() {
        if (processingTurn) return;

        processingTurn = true;

        // We first have to generate a list of all steps to process
        final Queue<TurnStep> steps = new ArrayDeque<>();

        for (final Entity<?> entity : map.entities()) {
            final List<TurnStep> enemyTurnSteps = entity.processTurn();
            steps.addAll(enemyTurnSteps);
        }

        // For visual purposes, each turn step is processed in a fixed time interval
        long time = 0;
        Boolean lastStepResult = null;
        try {
            while (!steps.isEmpty()) {
                final long deltaTime;
                deltaTime = Engine.context().waitTick();

                time = time + deltaTime;

                if (time > turnStepInterval) { // 1 second
                    time = time - turnStepInterval;

                    final TurnStep step = steps.poll();

                    assert step != null;
                    lastStepResult = step.execute(lastStepResult);
                }
            }
        } catch (InterruptedException exception) {
            System.err.printf(
                "Turn processing interrupted:%s%n%s%n",
                exception.getMessage(),
                Arrays.toString(exception.getStackTrace())
            );
        } finally {
            processingTurn = false;
        }
    }

    private void centerOnPlayer(Context context) {
        mapOffset = new Offset(
            playerEntity.position().dx() - context.size().width() / 2,
            playerEntity.position().dy() - context.size().height() / 2
        );
    }
}

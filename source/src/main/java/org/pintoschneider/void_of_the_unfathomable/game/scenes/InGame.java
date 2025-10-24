package org.pintoschneider.void_of_the_unfathomable.game.scenes;

import org.pintoschneider.void_of_the_unfathomable.core.Offset;
import org.pintoschneider.void_of_the_unfathomable.core.Size;
import org.pintoschneider.void_of_the_unfathomable.game.Player;
import org.pintoschneider.void_of_the_unfathomable.game.components.MapComponent;
import org.pintoschneider.void_of_the_unfathomable.game.enemies.Enemy;
import org.pintoschneider.void_of_the_unfathomable.game.enemies.StaticDissonance;
import org.pintoschneider.void_of_the_unfathomable.game.engine.Context;
import org.pintoschneider.void_of_the_unfathomable.game.engine.Keys;
import org.pintoschneider.void_of_the_unfathomable.game.engine.Scene;
import org.pintoschneider.void_of_the_unfathomable.game.items.consumables.FluoxetineBottle;
import org.pintoschneider.void_of_the_unfathomable.game.items.consumables.HaloperidolAmpoule;
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
    static final Paint boldPaint = new Paint().withBold(true);
    static private final Offset verticalOffset = new Offset(0, 1);
    static private final Offset horizontalOffset = new Offset(1, 0);
    static private final int turnStepInterval = 100_000_000; // 0.1 seconds per turn
    private final Map map = new Map();
    private final Player player = new Player();
    private final Map.Entity<Player> playerEntity = map.new Entity<>(new Offset(4, 4), '@', new Player());
    boolean processingTurn = false;
    private Offset mapOffset = Offset.ZERO;

    /**
     * Creates a new in-game scene.
     */
    InGame() {
        // Add items for testing purposes
        for (int i = 0; i < 10; i++) {
            player.addItemToInventory(new FluoxetineBottle());
            player.addItemToInventory(new HaloperidolAmpoule());
        }

        // Add enemy entity for testing purposes
        map.new Entity<>(new Offset(14, 9), '*', new StaticDissonance());
        map.new Entity<>(new Offset(15, 10), '*', new StaticDissonance());
    }

    @Override
    public Component build(Context context) {
        centerOnPlayer(context);

        final Component[] statusList = player.statusEffects().stream()
            .map(statusEffect -> new Text("- " + statusEffect.displayString()))
            .toArray(Component[]::new);

        return new Row(
            new Flexible(1, new MapComponent(map, mapOffset, playerEntity.position())),
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
                                    new Text("HP:", boldPaint),
                                    new Text("%3d/%3d".formatted(player.currentHealth(), player.maximumHealth()))
                                )
                            ),
                            new ConstrainedBox(
                                new Constraints(null, null, 1, 1),
                                new Row(
                                    new Text("CP:", boldPaint),
                                    new Text("%3d/%3d".formatted(player.getCurrentColorPoints(), player.maximumColorPoints()))
                                )
                            ),
                            new SizedBox(new Size(0, 1), null),
                            new Text("Estatus:", boldPaint),
                            new SizedBox(new Size(0, 1), null),
                            new Column(statusList)
                                .mainAxisSize(MainAxisSize.MIN)
                                .crossAxisAlignment(CrossAxisAlignment.STRETCH)
                        ).crossAxisAlignment(CrossAxisAlignment.STRETCH)
                            .mainAxisSize(MainAxisSize.MIN)
                    )
                ).crossAxisAlignment(CrossAxisAlignment.STRETCH)
            )
        ).crossAxisAlignment(CrossAxisAlignment.STRETCH);
    }

    @Override
    public void onKeyPress(Context context, int keyCode) {
        if (!processingTurn) {
            switch (keyCode) {
                case Keys.UP -> {
                    playerEntity.moveBy(verticalOffset.multiply(-1));
                    processTurn(context);
                }
                case Keys.DOWN -> {
                    playerEntity.moveBy(verticalOffset);
                    processTurn(context);
                }
                case Keys.LEFT -> {
                    playerEntity.moveBy(horizontalOffset.multiply(-1));
                    processTurn(context);
                }
                case Keys.RIGHT -> {
                    playerEntity.moveBy(horizontalOffset);
                    processTurn(context);
                }
            }
        }
    }

    private void processTurn(Context context) {
        processingTurn = true;

        // We first have to generate a list of all steps to process
        final Queue<TurnStep> steps = new ArrayDeque<>();

        for (final Map.Entity<Enemy> enemyEntity : map.entitiesOfType(Enemy.class)) {
            final Enemy enemy = enemyEntity.associatedObject();
            final List<TurnStep> enemyTurnSteps = enemy.processTurn(enemyEntity);
            steps.addAll(enemyTurnSteps);
        }

        // For visual purposes, each turn step is processed in a fixed time interval
        long time = 0;
        Boolean lastStepResult = null;
        try {
            while (!steps.isEmpty()) {
                final long deltaTime;
                deltaTime = context.waitTick();

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

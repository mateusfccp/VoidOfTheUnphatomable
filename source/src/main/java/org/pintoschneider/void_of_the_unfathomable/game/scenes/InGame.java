package org.pintoschneider.void_of_the_unfathomable.game.scenes;

import org.pintoschneider.void_of_the_unfathomable.Main;
import org.pintoschneider.void_of_the_unfathomable.core.Offset;
import org.pintoschneider.void_of_the_unfathomable.engine.Context;
import org.pintoschneider.void_of_the_unfathomable.engine.Engine;
import org.pintoschneider.void_of_the_unfathomable.engine.Key;
import org.pintoschneider.void_of_the_unfathomable.engine.Scene;
import org.pintoschneider.void_of_the_unfathomable.game.Player;
import org.pintoschneider.void_of_the_unfathomable.game.components.MapComponent;
import org.pintoschneider.void_of_the_unfathomable.game.entities.*;
import org.pintoschneider.void_of_the_unfathomable.game.items.Equippable;
import org.pintoschneider.void_of_the_unfathomable.game.items.EquippableSlot;
import org.pintoschneider.void_of_the_unfathomable.game.items.equippables.armors.Blue;
import org.pintoschneider.void_of_the_unfathomable.game.items.equippables.armors.MaidDress;
import org.pintoschneider.void_of_the_unfathomable.game.items.equippables.armors.Pajamas;
import org.pintoschneider.void_of_the_unfathomable.game.items.equippables.armors.Sunga;
import org.pintoschneider.void_of_the_unfathomable.game.items.equippables.weapons.BlackHole;
import org.pintoschneider.void_of_the_unfathomable.game.items.equippables.weapons.Stickool;
import org.pintoschneider.void_of_the_unfathomable.game.items.key_items.LeftBanana;
import org.pintoschneider.void_of_the_unfathomable.game.items.key_items.ResoundingCore;
import org.pintoschneider.void_of_the_unfathomable.game.items.key_items.RightBanana;
import org.pintoschneider.void_of_the_unfathomable.game.items.key_items.SpecialWeapon;
import org.pintoschneider.void_of_the_unfathomable.game.map.Map;
import org.pintoschneider.void_of_the_unfathomable.game.turn_steps.TurnStep;
import org.pintoschneider.void_of_the_unfathomable.ui.components.*;
import org.pintoschneider.void_of_the_unfathomable.ui.components.Stack;
import org.pintoschneider.void_of_the_unfathomable.ui.core.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * The in-game scene where the player can explore the map and interact with the game world.
 */
public final class InGame implements Scene {
    static private final Offset playerInitialPosition = new Offset(103, 189);
    static private final Offset debugBossRoomPosition = new Offset(130, 50);
    static private final Offset mainHallRoomPosition = new Offset(110, 110);

    static private final Offset verticalOffset = new Offset(0, 1);
    static private final Offset horizontalOffset = new Offset(1, 0);
    private final Map map = new Map();
    private final BitSet[] exploredTiles;
    private final Player player = new Player();
    private final PlayerEntity playerEntity = new PlayerEntity(mainHallRoomPosition, player, map);
    private final TurnManager turnManager = new TurnManager(playerEntity, map);
    private Offset mapOffset = Offset.ZERO;

    /**
     * Creates a new in-game scene.
     */
    public InGame() {
        final Equippable initialWeapon = new Stickool();
        player.addItemToInventory(initialWeapon);
        player.equipItem(initialWeapon);

        if (Main.debugMode) {
            player.addItemToInventory(new Blue());
            player.addItemToInventory(new Sunga());
            player.addItemToInventory(new MaidDress());
            player.addItemToInventory(new Pajamas());
            player.addItemToInventory(new LeftBanana());
            player.addItemToInventory(new RightBanana());
        }

        // Add entity for testing purposes
        new StaticDissonanceEntity(new Offset(14, 9), map);
        new StaticDissonanceEntity(new Offset(15, 10), map);
        new ItemEntity(new Offset(6, 6), new ResoundingCore(), map);
        new StairEntity(new Offset(4, 2), map);

        // Initialize explored tiles
        exploredTiles = new BitSet[map.height()];
        for (int y = 0; y < map.height(); y++) {
            exploredTiles[y] = new BitSet(map.width());
        }

        // Initialize entities
        for (int x = 108; x <= 111; x++) {
            final Offset position = new Offset(x, 101);
            new LockedDoor(position, map);
        }
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
                    new MapComponent(map, mapOffset, playerEntity.position(), exploredTiles)
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
        if (turnManager.isTurnInProgress()) return;

        if (key == Key.UP) {
            playerEntity.moveBy(verticalOffset.multiply(-1));
            turnManager.startNewTurn();
        } else if (key == Key.DOWN) {
            playerEntity.moveBy(verticalOffset);
            turnManager.startNewTurn();
        } else if (key == Key.LEFT) {
            playerEntity.moveBy(horizontalOffset.multiply(-1));
            turnManager.startNewTurn();
        } else if (key == Key.RIGHT) {
            playerEntity.moveBy(horizontalOffset);
            turnManager.startNewTurn();
        } else if (key == Key.I) {
            final CompletableFuture<Boolean> didConsumeItem = Engine.context().sceneManager().push(new Inventory(player));
            didConsumeItem.thenAccept(consumed -> {
                if (consumed) {
                    turnManager.startNewTurn();
                }
            });
        } else if (key == Key.C) {
            final Equippable weapon = playerEntity.associatedObject().equippedItem(EquippableSlot.WEAPON);

            if (weapon instanceof SpecialWeapon specialWeapon) {
                specialWeapon.specialAbility(playerEntity);
            }
        }
    }

    @Override
    public void onUpdate(long deltaTime) {
        turnManager.update(deltaTime);
    }

    private void centerOnPlayer(Context context) {
        mapOffset = new Offset(
            playerEntity.position().dx() - context.size().width() / 2,
            playerEntity.position().dy() - context.size().height() / 2
        );
    }
}

final class TurnManager {
    static private final int turnStepInterval = 100_000_000; // 0.1 seconds per turn
    private final PlayerEntity playerEntity;
    private final Map map;
    private final Queue<Step> currentTurnSteps = new ArrayDeque<>();
    private boolean isProcessingTurn = false;
    private boolean isProcessingStep = false;
    private long timeSinceLastTurnStep = 0;
    private Boolean lastStepResult = null;

    TurnManager(PlayerEntity playerEntity, Map map) {
        this.playerEntity = playerEntity;
        this.map = map;
    }

    /**
     * Checks if a turn is currently being processed.
     *
     * @return True if a turn is in progress, false otherwise.
     */
    public boolean isTurnInProgress() {
        return isProcessingTurn;
    }

    /**
     * Starts a new turn and prepares the steps for all entities.
     */
    public void startNewTurn() {
        if (isProcessingTurn) return;

        isProcessingTurn = true;
        timeSinceLastTurnStep = 0;

        for (final Entity<?> entity : map.entities()) {
            final boolean isInstant = !entity.canSee(playerEntity);
            final List<TurnStep> enemyTurnSteps = entity.processTurn();
            for (final TurnStep step : enemyTurnSteps) {
                currentTurnSteps.add(new Step(step, isInstant));
            }
        }
    }

    public void update(long deltaTime) {
        if (!isProcessingTurn) return;

        if (!isProcessingStep) {
            final List<Step> stepsThisIteration = new ArrayList<>();

            Step currentStep;
            while ((currentStep = currentTurnSteps.poll()) != null && currentStep.isInstant) {
                stepsThisIteration.add(currentStep);
            }

            if (currentStep != null) {
                stepsThisIteration.add(currentStep);
            }

            if (!stepsThisIteration.isEmpty()) {
                isProcessingStep = true;
                timeSinceLastTurnStep = 0;
            }

            for (final Step step : stepsThisIteration) {
                lastStepResult = step.step.execute(lastStepResult);
            }
        }

        timeSinceLastTurnStep = timeSinceLastTurnStep + deltaTime;
        if (timeSinceLastTurnStep > turnStepInterval) {
            isProcessingStep = false;
        }

        if (currentTurnSteps.isEmpty() && !isProcessingStep) {
            isProcessingTurn = false;
            lastStepResult = null;
        }
    }

    private record Step(TurnStep step, boolean isInstant) {}
}

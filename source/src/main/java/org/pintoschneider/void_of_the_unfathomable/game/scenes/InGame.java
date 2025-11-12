package org.pintoschneider.void_of_the_unfathomable.game.scenes;

import org.pintoschneider.void_of_the_unfathomable.Main;
import org.pintoschneider.void_of_the_unfathomable.core.Offset;
import org.pintoschneider.void_of_the_unfathomable.engine.Context;
import org.pintoschneider.void_of_the_unfathomable.engine.Engine;
import org.pintoschneider.void_of_the_unfathomable.engine.Key;
import org.pintoschneider.void_of_the_unfathomable.engine.Scene;
import org.pintoschneider.void_of_the_unfathomable.game.ColorPalette;
import org.pintoschneider.void_of_the_unfathomable.game.Player;
import org.pintoschneider.void_of_the_unfathomable.game.StatusEffect;
import org.pintoschneider.void_of_the_unfathomable.game.components.MapComponent;
import org.pintoschneider.void_of_the_unfathomable.game.entities.*;
import org.pintoschneider.void_of_the_unfathomable.game.items.Equippable;
import org.pintoschneider.void_of_the_unfathomable.game.items.EquippableSlot;
import org.pintoschneider.void_of_the_unfathomable.game.items.consumables.FluoxetineBottle;
import org.pintoschneider.void_of_the_unfathomable.game.items.consumables.HaloperidolAmpoule;
import org.pintoschneider.void_of_the_unfathomable.game.items.equippables.armors.Blue;
import org.pintoschneider.void_of_the_unfathomable.game.items.equippables.armors.MaidDress;
import org.pintoschneider.void_of_the_unfathomable.game.items.equippables.armors.Pajamas;
import org.pintoschneider.void_of_the_unfathomable.game.items.equippables.armors.Sunga;
import org.pintoschneider.void_of_the_unfathomable.game.items.equippables.weapons.BlackHole;
import org.pintoschneider.void_of_the_unfathomable.game.items.equippables.weapons.Stickool;
import org.pintoschneider.void_of_the_unfathomable.game.items.key_items.*;
import org.pintoschneider.void_of_the_unfathomable.game.map.Map;
import org.pintoschneider.void_of_the_unfathomable.game.map.MapTile;
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
    static private final Offset bnnRoomPosition = new Offset(99, 125);

    static private final Offset verticalOffset = new Offset(0, 1);
    static private final Offset horizontalOffset = new Offset(1, 0);
    private final Map map = new Map();
    private final BitSet[] exploredTiles;
    private final Player player = new Player();
    private final PlayerEntity playerEntity = new PlayerEntity(bnnRoomPosition, player, map);
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
            player.addItemToInventory(new Pajamas());
            player.addItemToInventory(new LeftBanana());
            player.addItemToInventory(new RightBanana());
            player.addItemToInventory(new BlackHole());
            for (int i = 0; i < 100; i++) {
                player.addItemToInventory(new FragmentOfNothingness());
                player.addItemToInventory(new FluoxetineBottle());
                player.addItemToInventory(new HaloperidolAmpoule());
            }
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

        // Shop
        new ShopKeeperEntity(new Offset(104, 103), map, player);

        // First area entities
        new StaticDissonanceEntity(new Offset(53, 196), map);
        new StaticDissonanceEntity(new Offset(57, 190), map);
        new StaticDissonanceEntity(new Offset(55, 191), map);
        new StaticDissonanceEntity(new Offset(60, 197), map);
        new ChestEntity(new Offset(57, 194), MaidDress::new, map);
        new StaticDissonanceEntity(new Offset(60, 197), map);
        new ChestEntity(new Offset(60, 179), FluoxetineBottle::new, map);
        new StaticDissonanceEntity(new Offset(91, 165), map);
        new ChestEntity(new Offset(115, 156), FragmentOfNothingness::new, 10, null, map);

        // Entities in the maze area
        new MimicEntity(new Offset(11, 134), map);
        new MimicEntity(new Offset(20, 130), map);
        new MimicEntity(new Offset(52, 153), map);
        new MimicEntity(new Offset(73, 152), map);
        new ChestEntity(
            new Offset(99, 120),
            LeftBanana::new,
            1,
            () -> map.setTileAt(
                109,
                118,
                MapTile.FLOOR
            ),
            map
        );
        new ItemEntity(
            new Offset(58, 54),
            new BlackHole(),
            new Paint().withForegroundColor(ColorPalette.CHARCOAL),
            map
        );

        // Entities in the bullet hell area
        new BulletManagerEntity(Offset.ZERO, map);
        new TurretOfNothingnessEntity(new Offset(178, 88), map);
        new TurretOfNothingnessEntity(new Offset(185, 105), map);
        new TurretOfNothingnessEntity(new Offset(187, 122), map);
        new TurretOfNothingnessEntity(new Offset(166, 123), map);
        new TurretOfNothingnessEntity(new Offset(175, 139), map);
        new TurretOfNothingnessEntity(new Offset(190, 158), map);
        new TurretOfNothingnessEntity(new Offset(165, 173), map);
        new TurretOfNothingnessEntity(new Offset(187, 181), map);
        new ItemEntity(
            new Offset(153, 190),
            new RightBanana(),
            new Paint().withForegroundColor(ColorPalette.BANANA),
            map
        );
    }

    @Override
    public void onEnter() {
        Engine.context().sceneManager().push(
            new DialogScene(
                "Finalmente entraste en El Vacío. Apenas puedes distinguir formas en la distancia. Te cuesta respirar, y una sensación de temor llena tu mente. Debés encontrar el Núcleo Resonante antes de que El Vacío te consuma por completo.",
                Alignment.CENTER
            )
        );
    }

    @Override
    public Component build() {
        centerOnPlayer(Engine.context());

        final Component[] statusEffects =
            player.statusEffects()
                .stream()
                .map(
                    status -> new Text(
                        "· " + status.displayString(),
                        new Paint().withForegroundColor(statusToColor(status))
                    )
                )
                .toArray(Component[]::new);

        return
            new Stack(
                // Use a box to fill the background
                new ConstrainedBox(
                    Constraints.expand(),
                    new Box()
                ),
                new MapComponent(map, mapOffset, playerEntity.position(), exploredTiles),
                new Align(
                    Alignment.TOP_RIGHT,
                    new Padding(
                        EdgeInsets.all(1),
                        new Box(
                            Border.SINGLE_ROUNDED,
                            new Padding(
                                EdgeInsets.all(1),
                                new Column(
                                    new Text("Sanidad:", Paint.BOLD),
                                    new Text("%d/%d".formatted(player.currentHealth(), player.maximumHealth())),
                                    statusEffects.length == 0 ? null : new SizedBox(0, 1),
                                    statusEffects.length == 0 ? null : new Column(statusEffects).mainAxisSize(MainAxisSize.MIN)
                                ).mainAxisSize(MainAxisSize.MIN)
                            )
                        )
                    )
                )
            );
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
                turnManager.startNewTurn();
            }
        } else if (key == Key.ESC) {
            Engine.context().sceneManager().<Boolean>push(
                new QuestionScene<>(
                    "¿Deseas salir al menú principal?\nTodo el progreso se perderá.",
                    List.of(
                        new QuestionScene.Answer<>("Sí", true, true),
                        new QuestionScene.Answer<>("No", false, true)
                    )
                )
            ).thenAccept(answers -> {
                if (answers) {
                    Engine.context().sceneManager().pop();
                }
            });
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

    private Color statusToColor(StatusEffect statusEffect) {
        return switch (statusEffect) {
            case DEPRESSION -> ColorPalette.ROYAL_BLUE;
            case DEPENDENCY -> ColorPalette.BANANA;
            case DISCONTINUATION_SYNDROME -> ColorPalette.APRICOT;
            case DYSKINESIA -> ColorPalette.CLAY;
            case TARDIVE_DYSKINESIA -> ColorPalette.VERMILION;
            case INSANITY -> ColorPalette.BLUSH;
            case DEATH -> ColorPalette.CHARCOAL;
        };
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

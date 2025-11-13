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
import org.pintoschneider.void_of_the_unfathomable.game.highscore.HighscoreEntry;
import org.pintoschneider.void_of_the_unfathomable.game.highscore.RunStatus;
import org.pintoschneider.void_of_the_unfathomable.game.items.Equippable;
import org.pintoschneider.void_of_the_unfathomable.game.items.EquippableSlot;
import org.pintoschneider.void_of_the_unfathomable.game.items.Item;
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
import java.util.stream.Stream;

/**
 * The in-game scene where the player can explore the map and interact with the game world.
 */
public final class InGame implements Scene {
    static private final Offset playerInitialPosition = new Offset(103, 189);
    static private final Offset debugBossRoomPosition = new Offset(130, 50);
    static private final Offset debugMainHallRoomPosition = new Offset(110, 110);
    static private final Offset debugBnnRoomPosition = new Offset(99, 125);
    static private final Offset debugWellRoomPosition = new Offset(49, 118);

    static private final Offset verticalOffset = new Offset(0, 1);
    static private final Offset horizontalOffset = new Offset(1, 0);
    private final Map map = new Map();
    private final BitSet[] exploredTiles;
    private final Player player = new Player();
    private final PlayerEntity playerEntity = new PlayerEntity(playerInitialPosition, player, map);
    private final TurnManager turnManager = new TurnManager(playerEntity, map);
    private Offset mapOffset = Offset.ZERO;
    private final List<Entity<?>> allEntities = new ArrayList<>();

    /**
     * Creates a new in-game scene.
     */
    public InGame() {
        final Equippable initialWeapon = new Stickool();
        player.addItemToInventory(initialWeapon);
        player.equipItem(initialWeapon);
        player.addItemToInventory(new FamilyPhoto());

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

        // Initialize explored tiles
        exploredTiles = new BitSet[map.height()];
        for (int y = 0; y < map.height(); y++) {
            exploredTiles[y] = new BitSet(map.width());
        }

        // Initialize entities
        for (int x = 108; x <= 111; x++) {
            final Offset position = new Offset(x, 101);
            new LockedDoor(position, map); // Not a scorable enemy
        }

        // Shop
        new ShopKeeperEntity(new Offset(104, 103), map, player); // Not scorable

        // First area entities
        new StairEntity(new Offset(98, 187), map); // Not scorable

        // StaticDissonance (x6)
        allEntities.add(new StaticDissonanceEntity(new Offset(53, 196), map));
        allEntities.add(new StaticDissonanceEntity(new Offset(57, 190), map));
        allEntities.add(new StaticDissonanceEntity(new Offset(55, 191), map));
        allEntities.add(new StaticDissonanceEntity(new Offset(60, 197), map));
        allEntities.add(new StaticDissonanceEntity(new Offset(60, 197), map));
        allEntities.add(new StaticDissonanceEntity(new Offset(91, 165), map));

        // Chests
        new ChestEntity(new Offset(57, 194), FragmentOfNothingness::new, 20, null, map);
        new ChestEntity(new Offset(60, 179), FluoxetineBottle::new, map);
        new ChestEntity(new Offset(115, 156), FragmentOfNothingness::new, 15, null, map);

        // Entities in the maze area
        // Mimics (x4)
        allEntities.add(new MimicEntity(new Offset(11, 134), map));
        allEntities.add(new MimicEntity(new Offset(20, 130), map));
        allEntities.add(new MimicEntity(new Offset(52, 153), map));
        allEntities.add(new MimicEntity(new Offset(73, 152), map));

        // LeftBanana
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

        // Trans Well
        new ItemEntity(
            new Offset(58, 54),
            new BlackHole(),
            new Paint().withForegroundColor(ColorPalette.CHARCOAL),
            map
        );
        new WellOfTransformationEntity(new Offset(49, 120), map);

        // Entities in the bullet hell area
        new BulletManagerEntity(Offset.ZERO, map); // Not scorable

        // Turrets (x8)
        allEntities.add(new TurretOfNothingnessEntity(new Offset(178, 88), map));
        allEntities.add(new TurretOfNothingnessEntity(new Offset(185, 105), map));
        allEntities.add(new TurretOfNothingnessEntity(new Offset(187, 122), map));
        allEntities.add(new TurretOfNothingnessEntity(new Offset(166, 123), map));
        allEntities.add(new TurretOfNothingnessEntity(new Offset(175, 139), map));
        allEntities.add(new TurretOfNothingnessEntity(new Offset(190, 158), map));
        allEntities.add(new TurretOfNothingnessEntity(new Offset(165, 173), map));
        allEntities.add(new TurretOfNothingnessEntity(new Offset(187, 181), map));

        // Right Banana
        new ItemEntity(
            new Offset(153, 190),
            new RightBanana(),
            new Paint().withForegroundColor(ColorPalette.BANANA),
            map
        );

// Entities in the boss area
        // Abyssmonkey (x1)
        allEntities.add(new AbyssmonkeyEntity(new Offset(130, 32), map));

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
                        status.displayString(),
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
                                    statusEffects.length == 0 ? null : new Column(statusEffects).mainAxisSize(MainAxisSize.MIN),
                                    new SizedBox(0, 1),
                                    new Text("Progreso:", Paint.BOLD),
                                    new Text("%d%%".formatted(progress()))
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
                    Engine.context().sceneManager().pop(
                        HighscoreEntry.fromGame(this, RunStatus.FORSAKEN)
                    );
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
            case DEATH -> ColorPalette.SLATE;
        };
    }

    /**
     * Returns the progress of the current game.
     *
     * @return An integer that represents the progress between 0 and 100.
     */
    public int progress() {
        int total = 0;
        final Set<Entity<?>> entities = new HashSet<>(map.entities());
        final List<Item> items = player.inventory();

        for (Entity<?> enemy : allEntities) {
            if (!entities.contains(enemy)) {
                total += percentageEnemies(enemy);
            }
        }

        for (Item item : items) {
            total += percentageItems(item);
        }


        return total;
    }

    /**
     * Returns the progress value of an enemy
     *
     * @return An integer representing the progress between 0 and 100.
     */
    private int percentageEnemies(Entity<?> entity) {
        if (entity instanceof AbyssmonkeyEntity) { // x1
            return 10;
        } else if (entity instanceof StaticDissonanceEntity) { // x6
            return 2;
        } else if (entity instanceof TurretOfNothingnessEntity) { // x8
            return 2;
        } else if (entity instanceof MimicEntity) { // x4
            return 3;
        } else if (!LockedDoor.isLocked() && entity instanceof LockedDoor) {
            return 10;
        }

        return 0;
    }

    /**
     * Returns the progress value of an Item
     *
     * @return An integer representing the progress between 0 and 100.
     */
    private int percentageItems(Item item) {
        // Armours and Weapons
        if (item instanceof MaidDress) {
            return 5;
        } else if (item instanceof Blue) {
            return 10;
        } else if (item instanceof Sunga) {
            return 5;
        } else if (item instanceof Pajamas) {
            return 10;
        } else if (item instanceof BlackHole) {
            return 5;
        } else if (item instanceof Stickool) {
            return 5;
        }

        // Key Items
        else if (item instanceof RightBanana) {
            return 5;
        } else if (item instanceof LeftBanana) {
            return 5;
        } else if (item instanceof ResoundingCore) {
            return 10;
        }

        return 0;
    }


    /**
     * Returns the current turn count.
     *
     * @return The current turn count.
     */
    public long turnCount() {
        return turnManager.turnCount();
    }
}

/**
 * Manages the turn-based system of the game, processing turns and turn steps for all entities.
 */
final class TurnManager {
    static private final int turnStepInterval = 100_000_000; // 0.1 seconds per turn
    private final PlayerEntity playerEntity;
    private final Map map;
    private final Queue<Step> currentTurnSteps = new ArrayDeque<>();
    private boolean isProcessingTurn = false;
    private boolean isProcessingStep = false;
    private long timeSinceLastTurnStep = 0;
    private Boolean lastStepResult = null;
    private int turnCount = 0;

    /**
     * Creates a new TurnManager for the given player entity and map.
     *
     * @param playerEntity The player entity.
     * @param map          The map.
     */
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

    /**
     * Updates the turn manager, processing turn steps based on the elapsed time.
     *
     * @param deltaTime The time elapsed since the last update in nanoseconds.
     */
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
            turnCount = turnCount + 1;
        }
    }

    /**
     * Returns the number of turns that have been processed.
     *
     * @return The turn count.
     */
    public long turnCount() {
        return turnCount;
    }

    private record Step(TurnStep step, boolean isInstant) {}
}

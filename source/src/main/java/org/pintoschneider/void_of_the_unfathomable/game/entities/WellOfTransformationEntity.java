package org.pintoschneider.void_of_the_unfathomable.game.entities;

import org.pintoschneider.void_of_the_unfathomable.animation.Animation;
import org.pintoschneider.void_of_the_unfathomable.core.Offset;
import org.pintoschneider.void_of_the_unfathomable.engine.Engine;
import org.pintoschneider.void_of_the_unfathomable.game.ColorPalette;
import org.pintoschneider.void_of_the_unfathomable.game.items.Equippable;
import org.pintoschneider.void_of_the_unfathomable.game.items.EquippableSlot;
import org.pintoschneider.void_of_the_unfathomable.game.items.equippables.armors.Blue;
import org.pintoschneider.void_of_the_unfathomable.game.items.equippables.armors.MaidDress;
import org.pintoschneider.void_of_the_unfathomable.game.items.equippables.armors.Pajamas;
import org.pintoschneider.void_of_the_unfathomable.game.items.equippables.armors.Sunga;
import org.pintoschneider.void_of_the_unfathomable.game.map.Map;
import org.pintoschneider.void_of_the_unfathomable.game.map.SpatialProperty;
import org.pintoschneider.void_of_the_unfathomable.game.scenes.DialogScene;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Alignment;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Color;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Paint;

import java.time.Duration;
import java.util.List;

/**
 * An entity that represents a well of transformation in the game.
 * <p>
 * A well of transformation is a special entity that can be used once to transform an equipment item into another, more
 * powerful one.
 */
public class WellOfTransformationEntity extends Entity<Void> {
    private static final List<Color> colorSequence = List.of(
        ColorPalette.BLUSH,
        ColorPalette.AZURE,
        ColorPalette.ROYAL_BLUE,
        ColorPalette.APRICOT,
        ColorPalette.CLAY,
        ColorPalette.BANANA,
        ColorPalette.VERMILION,
        ColorPalette.MINT_GREEN,
        ColorPalette.MERLOT,
        ColorPalette.EMERALD,
        ColorPalette.IVORY
    );
    private final Animation idleAnimation = Animation.repeating(
        Duration.ofSeconds(2)
    );
    private boolean wasUsed = false;

    /**
     * Creates a new WellOfTransformationEntity.
     *
     * @param position The position of the well.
     * @param map      The map the well is in.
     */
    public WellOfTransformationEntity(Offset position, Map map) {
        super(position, null, map);
        idleAnimation.play();
    }

    private static Equippable getTransformation(Equippable item) {
        if (item instanceof Sunga) {
            return new Pajamas();
        } else if (item instanceof MaidDress) {
            return new Blue();
        } else {
            return null;
        }
    }

    @Override
    public Character representation() {
        if (wasUsed) {
            return '○';
        }

        final int frame = (int) (idleAnimation.progress() * 8);

        return switch (frame) {
            case 4 -> '☼';
            case 3, 5 -> '*';
            case 2, 6 -> '○';
            case 1, 7 -> '•';
            default -> '∙';
        };
    }

    @Override
    public Paint paint() {
        final int colorIndex = (int) (idleAnimation.progress() * colorSequence.size());
        final Color colorA = colorSequence.get(colorIndex);
        final Color colorB = colorSequence.get((colorIndex + 1) % colorSequence.size());
        final Color color = Color.lerp(
            colorA,
            colorB,
            (idleAnimation.progress() * colorSequence.size()) % 1.0
        );

        return new Paint().withForegroundColor(wasUsed ? ColorPalette.CHARCOAL : color);
    }

    @Override
    public SpatialProperty spatialProperty() {
        return new SpatialProperty(false, false);
    }

    @Override
    public void interact(Entity<?> entity) {
        if (entity instanceof PlayerEntity playerEntity) {
            if (wasUsed) {
                Engine.context().sceneManager().push(
                    new DialogScene(
                        "Podés sentir que en algún momento este pozo tuvo un poder inmenso de transformación, pero ahora está dormido. Al intentar conectarte con él, nada sucede.",
                        Alignment.CENTER
                    )
                );
            } else if (getTransformation(playerEntity.associatedObject().equippedItem(EquippableSlot.ARMOR)) instanceof Equippable newArmor) {
                Engine.context().sceneManager().push(
                    new DialogScene(
                        "Te acercás al pozo y sentís una energía vibrante que emana de él. Al tocar su superficie, una oleada de poder recorre tu cuerpo, te sentís más poderoso que antes.",
                        Alignment.CENTER
                    )
                );
                final Equippable currentArmor = playerEntity.associatedObject().equippedItem(EquippableSlot.ARMOR);
                playerEntity.associatedObject().addItemToInventory(newArmor);
                playerEntity.associatedObject().equipItem(newArmor);
                playerEntity.associatedObject().removeItemOfType(currentArmor.getClass());
                wasUsed = true;
            } else {
                Engine.context().sceneManager().push(
                    new DialogScene(
                        "Te acercás al pozo. El pozo te examina e intenta mirar adentro de tu alma en busca de algo valioso, pero nada sucede.",
                        Alignment.CENTER
                    )
                );
            }
        }
    }

    @Override
    protected void dispose() {
        idleAnimation.dispose();
    }
}

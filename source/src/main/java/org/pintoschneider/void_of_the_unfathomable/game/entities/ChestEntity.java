package org.pintoschneider.void_of_the_unfathomable.game.entities;

import org.pintoschneider.void_of_the_unfathomable.core.Offset;
import org.pintoschneider.void_of_the_unfathomable.engine.Engine;
import org.pintoschneider.void_of_the_unfathomable.game.ColorPalette;
import org.pintoschneider.void_of_the_unfathomable.game.items.Item;
import org.pintoschneider.void_of_the_unfathomable.game.map.Map;
import org.pintoschneider.void_of_the_unfathomable.game.map.SpatialProperty;
import org.pintoschneider.void_of_the_unfathomable.game.scenes.DialogScene;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Alignment;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Paint;

import java.util.function.Supplier;

public class ChestEntity extends Entity<Supplier<Item>> {
    private boolean isOpened = false;
    private final int amount;
    private Runnable onOpenCallback;

    public ChestEntity(Offset position, Supplier<Item> itemBuilder, Map map) {
        this(position, itemBuilder, 1, null, map);
    }

    public ChestEntity(Offset position, Supplier<Item> itemBuilder, int amount, Runnable onOpenCallback, Map map) {
        super(position, itemBuilder, map);
        this.amount = amount;
        this.onOpenCallback = onOpenCallback;
    }

    @Override
    public Character representation() {
        if (isOpened) {
            return '▭';
        } else {
            return '▆';
        }
    }

    @Override
    public Paint paint() {
        return new Paint().withForegroundColor(ColorPalette.APRICOT);
    }

    @Override
    public SpatialProperty spatialProperty() {
        return new SpatialProperty(false, false);
    }

    @Override
    public void interact(Entity<?> entity) {
        if (!isOpened && entity instanceof PlayerEntity playerEntity) {
            final String message = amount == 1
                ? "Abriste el cofre y encontraste un %s!".formatted(associatedObject().get().name())
                : "Abriste el cofre y encontraste %d %ss!".formatted(
                amount,
                associatedObject().get().name()
            );

            Engine.context().sceneManager().push(
                new DialogScene(message, Alignment.CENTER)
            ).thenRun(onOpenCallback);

            for (int i = 0; i < amount; i++) {
                playerEntity.associatedObject().addItemToInventory(associatedObject().get());
            }

            isOpened = true;
        }
    }
}

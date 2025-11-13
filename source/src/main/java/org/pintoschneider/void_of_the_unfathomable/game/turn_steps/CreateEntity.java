package org.pintoschneider.void_of_the_unfathomable.game.turn_steps;

import org.pintoschneider.void_of_the_unfathomable.game.entities.Entity;

import java.util.function.Supplier;

/**
 * A {@link TurnStep} that drops an item at a specified offset.
 */
public class CreateEntity implements TurnStep {
    final Entity<?> entity;
    final Supplier<Entity<?>> entitySupplier;

    /**
     * Creates a new CreateEntity action for the given entity.
     *
     * @param entity         The entity that will drop the item.
     * @param entitySupplier A supplier that provides the item to be dropped.
     */
    public CreateEntity(Entity<?> entity, Supplier<Entity<?>> entitySupplier) {
        this.entity = entity;
        this.entitySupplier = entitySupplier;
    }

    @Override
    public boolean execute(Boolean lastTurnResult) {
        entitySupplier.get();
        return true;
    }
}

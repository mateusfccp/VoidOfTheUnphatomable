package org.pintoschneider.void_of_the_unfathomable.game.turn_steps;

import org.pintoschneider.void_of_the_unfathomable.game.entities.Entity;

import java.util.function.Supplier;

/**
 * A {@link TurnStep} that drops an item at a specified offset.
 */
public class CreateEntity implements TurnStep {
    private final Supplier<Entity<?>> entitySupplier;

    /**
     * Creates a new CreateEntity action for the given entity.
     *
     * @param entitySupplier A supplier that provides the item to be dropped.
     */
    public CreateEntity(Supplier<Entity<?>> entitySupplier) {
        this.entitySupplier = entitySupplier;
    }

    @Override
    public boolean execute(Boolean lastTurnResult) {
        entitySupplier.get();
        return true;
    }
}

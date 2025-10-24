package org.pintoschneider.void_of_the_unfathomable.game.entities;

import org.pintoschneider.void_of_the_unfathomable.core.Offset;
import org.pintoschneider.void_of_the_unfathomable.game.map.Map;

public abstract class StepOnTriggeringEntity<T> extends Entity<T> {
    protected StepOnTriggeringEntity(Offset position, T associatedObject, Map map) {
        super(position, associatedObject, map);
    }
}

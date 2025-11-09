package org.pintoschneider.void_of_the_unfathomable.game.items.equippables.armors;

import org.pintoschneider.void_of_the_unfathomable.game.StatusEffect;
import org.pintoschneider.void_of_the_unfathomable.game.items.Equippable;
import org.pintoschneider.void_of_the_unfathomable.game.items.EquippableSlot;

import java.util.EnumSet;

/**
 * The Pajamas armor item.
 * <p>
 * It provides comfort and a sense of security to the player, reducing the dyskinesia related effects.
 */
public class Pajamas implements Equippable {
    @Override
    public EquippableSlot slot() {
        return EquippableSlot.ARMOR;
    }

    @Override
    public String name() {
        return "Piyamas";
    }

    @Override
    public String description() {
        return "Son tan cómodas que te hacen sentir seguro y protegido, e incluso te olvidas de tus ganas de temblar y moverte sin control.";
    }

    @Override
    public void transformStatusEffects(EnumSet<StatusEffect> originalStatusEffects) {
        if (originalStatusEffects.contains(StatusEffect.DYSKINESIA)) {
            originalStatusEffects.remove(StatusEffect.DYSKINESIA);
        } else if (originalStatusEffects.contains(StatusEffect.TARDIVE_DYSKINESIA)) {
            originalStatusEffects.remove(StatusEffect.TARDIVE_DYSKINESIA);
            originalStatusEffects.add(StatusEffect.DYSKINESIA);
        }
    }
}

package org.pintoschneider.void_of_the_unfathomable.game.items.equippables.weapons;

import org.pintoschneider.void_of_the_unfathomable.game.entities.AbyssmonkeyEntity;
import org.pintoschneider.void_of_the_unfathomable.game.entities.DamageableEntity;
import org.pintoschneider.void_of_the_unfathomable.game.entities.PlayerEntity;
import org.pintoschneider.void_of_the_unfathomable.game.items.key_items.SpecialWeapon;

/**
 * A black hole weapon.
 * <p>
 * It attacks an area of enemies and deals a small amount of damage to the user.
 * <p>
 * The special ability of the black hole is to one-shot the entire game and deal 90 damage to the user.
 */
public final class BlackHole extends SpecialWeapon {
    @Override
    public String name() {
        return "Ticiujero Negro";
    }

    @Override
    public String description() {
        return "Ni mismo vos podés entender como podés tenerlo en tu mano. Oblitera todo que está alrededor, incluso vos mismo.";
    }

    @Override
    public void specialAbility(PlayerEntity player) {
        player.damage(90);

        for (final var entity : player.map().getEntitiesOfType(DamageableEntity.class)) {
            if (entity != player && !(entity instanceof AbyssmonkeyEntity)) {
                entity.damage(Integer.MAX_VALUE);
            }
        }
    }
}

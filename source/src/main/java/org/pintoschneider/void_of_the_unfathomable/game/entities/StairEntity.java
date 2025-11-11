package org.pintoschneider.void_of_the_unfathomable.game.entities;

import org.pintoschneider.void_of_the_unfathomable.core.Offset;
import org.pintoschneider.void_of_the_unfathomable.engine.Engine;
import org.pintoschneider.void_of_the_unfathomable.game.items.key_items.ResoundingCore;
import org.pintoschneider.void_of_the_unfathomable.game.map.Map;
import org.pintoschneider.void_of_the_unfathomable.game.map.SpatialProperty;
import org.pintoschneider.void_of_the_unfathomable.game.scenes.DialogScene;
import org.pintoschneider.void_of_the_unfathomable.game.scenes.QuestionScene;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Alignment;

/**
 * An entity representing stairs that allow the player to exit the Void.
 * <p>
 * When the player interacts with this entity, they are given the option to exit the Void. If the player possesses the
 * Resounding Core, they are informed of their ability to stop the Void. If they do not possess the Resounding Core,
 * they are asked if they wish to exit without it, leading to a somber outcome.
 */
public final class StairEntity extends Entity<Void> {
    public StairEntity(Offset position, Map map) {
        super(position, null, map);
    }

    @Override
    public Character representation() {
        return '≡';
    }

    @Override
    public SpatialProperty spatialProperty() {
        return new SpatialProperty(false, false);
    }

    @Override
    public void interact(Entity<?> entity) {
        if (entity instanceof PlayerEntity playerEntity) {
            if (playerEntity.associatedObject().hasItemOfType(ResoundingCore.class)) {
                Engine.context().sceneManager().push(
                    new DialogScene(
                        "Con el Núcleo Resonante en tu posesión, sentís una oleada de poder y esperanza. Sabés que tenés la capacidad de detener El Vacío y restaurar el equilibrio en el mundo. Mientras ascendés de nuevo a la superficie, te preparás para enfrentar el desafío que te espera. El destino del mundo está en tus manos.",
                        Alignment.CENTER,
                        _ -> Engine.context().sceneManager().pop()
                    )
                );
            } else {
                final QuestionScene<Boolean> questionScene = QuestionScene.yesNo("¿Querés salir del Vacío y regresar a la superficie sin el Núcleo Resonante?");
                Engine.context().sceneManager().<Boolean>push(questionScene).thenAccept((result) -> {
                    if (result) {
                        Engine.context().sceneManager().push(
                            new DialogScene(
                                "Aunque no tengas el Núcleo Resonante, decidís huir del Vacío. Sentís un breve momento de alivio al ascender de nuevo a la superficie, pero sabés que toda esperanza para el mundo se ha perdido por tu culpa. El Vacío continuará extendiéndose, consumiendo todo a su paso. COBARDE.",
                                Alignment.CENTER,
                                _ -> Engine.context().sceneManager().pop()
                            )
                        );
                    }
                });
            }
        }
    }
}


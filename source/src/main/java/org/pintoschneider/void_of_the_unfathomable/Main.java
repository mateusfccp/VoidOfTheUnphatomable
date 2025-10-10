package org.pintoschneider.void_of_the_unfathomable;

import org.pintoschneider.void_of_the_unfathomable.ui.Engine;
import org.pintoschneider.void_of_the_unfathomable.ui.components.LinearLayout.Fixed;
import org.pintoschneider.void_of_the_unfathomable.ui.components.LinearLayout.Flexible;
import org.pintoschneider.void_of_the_unfathomable.ui.components.Row;
import org.pintoschneider.void_of_the_unfathomable.ui.components.Box;
import org.pintoschneider.void_of_the_unfathomable.ui.components.Text;

public class Main {
    static final String lore = "Todos los ojos, desprovistos de brillo, se volvían hacia la misma cicatriz en el corazón del mundo: el Vacío. Era un no-lugar de locura geométrica, una herida supurante en el tejido del universo, nacida de la arrogancia de los Ancestros que osaron mirar a la cara a la nada. Dentro de esa pesadilla física, se decía, habían dejado caer una última lágrima de la Creación, una nota del primer sonido: el Núcleo Resonante, cuya vibración era lo único que podía recordarle al universo cómo existir.";

    public static void main(String[] args) {
        final Box box = new Box(
                new Row(
                        new Fixed(new Box(

                        ), 10),
                        new Flexible(new Box(new Text(lore)), 1),
                        new Fixed(new Box(), 10)
                )
        );
        try (final Engine engine = new Engine(box)) {
            while (engine.isAlive()) {
                engine.tick();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

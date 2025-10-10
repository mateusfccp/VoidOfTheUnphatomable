package org.pintoschneider.void_of_the_unfathomable;

import org.pintoschneider.void_of_the_unfathomable.ui.Engine;
import org.pintoschneider.void_of_the_unfathomable.ui.components.LinearLayout.Fixed;
import org.pintoschneider.void_of_the_unfathomable.ui.components.LinearLayout.Flexible;
import org.pintoschneider.void_of_the_unfathomable.ui.components.Row;
import org.pintoschneider.void_of_the_unfathomable.ui.components.Box;

public class Main {
    public static void main(String[] args) {
        final Box box = new Box(
                new Row(
                        new Fixed(new Box(

                        ), 10),
                        new Flexible(new Box(new Text("Void of the Unfathomable")), 1),
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

package org.pintoschneider.void_of_the_unphatomable;

import org.pintoschneider.void_of_the_unphatomable.ui.Engine;
import org.pintoschneider.void_of_the_unphatomable.ui.components.LinearLayout.Fixed;
import org.pintoschneider.void_of_the_unphatomable.ui.components.LinearLayout.Flexible;
import org.pintoschneider.void_of_the_unphatomable.ui.components.Row;
import org.pintoschneider.void_of_the_unphatomable.ui.components.Window;

public class Main {
    public static void main(String[] args) {
        final Window window = new Window(
                new Row(
                        new Fixed(new Window(), 10),
                        new Flexible(new Window(), 1),
                        new Fixed(new Window(), 10)
                )
        );
        try (final Engine engine = new Engine(window)) {
            while (engine.isAlive()) {
                engine.tick();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

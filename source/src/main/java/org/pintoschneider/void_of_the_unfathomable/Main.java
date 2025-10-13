package org.pintoschneider.void_of_the_unfathomable;

import org.pintoschneider.void_of_the_unfathomable.ui.Engine;
import org.pintoschneider.void_of_the_unfathomable.ui.components.Column;
import org.pintoschneider.void_of_the_unfathomable.ui.components.LinearLayout.Intrinsic;
import org.pintoschneider.void_of_the_unfathomable.ui.components.Box;
import org.pintoschneider.void_of_the_unfathomable.ui.components.Text;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Paint;

public class Main {

    public static void main(String[] args) {
        final Paint bold = new Paint();
        bold.bold = true;

        final Paint italic = new Paint();
        italic.italic = true;

        final Paint underline = new Paint();
        underline.underline = true;

        final Paint strikethrough = new Paint();
        strikethrough.strikethrough = true;

        final Paint dim = new Paint();
        dim.dim = true;

        final Paint inverted = new Paint();
        inverted.inverted = true;

        final Paint blink = new Paint();
        blink.blink = true;

        final Paint red = new Paint();
        red.foregroundColor = 0xFF0000;

        final Paint redBackground = new Paint();
        redBackground.backgroundColor = 0xFF0000;

        final Box box = new Box(
                new Column(
                        new Intrinsic(
                                new Text("Bold", bold)
                        ),
                        new Intrinsic(
                                new Text("Italic", italic)
                        ),
                        new Intrinsic(
                                new Text("Underline", underline)
                        ),
                        new Intrinsic(
                                new Text("Strikethrough", strikethrough)
                        ),
                        new Intrinsic(
                                new Text("Dim", dim)
                        ),
                        new Intrinsic(
                                new Text("Inverted", inverted)
                        ),
                        new Intrinsic(
                                new Text("Blink", blink)
                        ),
                        new Intrinsic(
                                new Text("Red", red)
                        ),
                        new Intrinsic(
                                new Text("Red Background", redBackground)
                        )
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

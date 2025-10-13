package org.pintoschneider.void_of_the_unfathomable;

import org.pintoschneider.void_of_the_unfathomable.ui.Engine;
import org.pintoschneider.void_of_the_unfathomable.ui.components.Align;
import org.pintoschneider.void_of_the_unfathomable.ui.components.Column;
import org.pintoschneider.void_of_the_unfathomable.ui.components.LinearLayout.Intrinsic;
import org.pintoschneider.void_of_the_unfathomable.ui.components.Border;
import org.pintoschneider.void_of_the_unfathomable.ui.components.Text;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Alignment;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Paint;

import java.io.IOException;

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

        final Border border = new Border(
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
                        ,
                        new Intrinsic(
                            new Border(
                                    new Align(
                                            Alignment.CENTER,
                                            new Text("Centered Text", red)
                                    )
                            )
                        )
                )
        );
        try (final Engine engine = new Engine(border)) {
            while (engine.isAlive()) {
                engine.tick();
            }
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}

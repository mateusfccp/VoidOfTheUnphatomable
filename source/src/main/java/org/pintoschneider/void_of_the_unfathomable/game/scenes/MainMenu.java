package org.pintoschneider.void_of_the_unfathomable.game.scenes;

import org.pintoschneider.void_of_the_unfathomable.animation.Animation;
import org.pintoschneider.void_of_the_unfathomable.animation.curves.CubicCurve;
import org.pintoschneider.void_of_the_unfathomable.engine.Engine;
import org.pintoschneider.void_of_the_unfathomable.game.ColorPalette;
import org.pintoschneider.void_of_the_unfathomable.ui.components.Align;
import org.pintoschneider.void_of_the_unfathomable.ui.components.Column;
import org.pintoschneider.void_of_the_unfathomable.ui.components.SizedBox;
import org.pintoschneider.void_of_the_unfathomable.ui.components.Text;
import org.pintoschneider.void_of_the_unfathomable.ui.core.*;

import java.time.Duration;
import java.util.List;

public final class MainMenu extends SelectionScene {
    Animation titleAnimation;

    @Override
    public void onEnter() {
        super.onEnter();
        titleAnimation = Animation.repeating(Duration.ofSeconds(2));
        titleAnimation.play();
    }

    @Override
    List<Option> options() {
        return List.of(
            new Option("Nuevo Juego", this::startNewGame),
            new Option("Cargar Juego"),
            new Option("Configuraciones"),
            new Option("Salir", this::exitGame)
        );
    }

    @Override
    protected CrossAxisAlignment alignment() {
        return CrossAxisAlignment.CENTER;
    }

    @Override
    public Component build() {
        return new Align(
            Alignment.CENTER,
            new Column(
                new Title(titleAnimation),
                new SizedBox(0, 2),
                super.build()
            ).crossAxisAlignment(CrossAxisAlignment.CENTER).mainAxisSize(MainAxisSize.MIN)
        );
    }

    private void startNewGame() {
        Engine.context().sceneManager().push(new InGame());
    }

    private void exitGame() {
        Engine.context().sceneManager().pop();
    }

    @Override
    public void dispose() {
        titleAnimation.dispose();
        super.dispose();
    }
}

final class Title extends Composent {
    static private final String base = """
          ▖▖  ▘ ▌    ▐▘  ▄▖▌
          ▌▌▛▌▌▛▌  ▛▌▜▘  ▐ ▛▌█▌
          ▚▘▙▌▌▙▌  ▙▌▐   ▐ ▌▌▙▖
        ▖▖    ▌   ▗        ▌ ▜
        ▌▌▛▌▛▌▛▌▀▌▜▘▛▌▛▛▌▀▌▛▌▐ █▌
        ▙▌▌▌▙▌▌▌█▌▐▖▙▌▌▌▌█▌▙▌▐▖▙▖
            ▌""";

    private final Animation animation;

    Title(Animation animation) {
        this.animation = animation;
    }

    @Override
    public Component build() {
        final double t = CubicCurve.EASE_IN_OUT.transform(
            Math.min(animation.progress(), 1.0 - animation.progress()) * 2.0
        );
        final Color color = Color.lerp(ColorPalette.LAVENDER_GRAY, ColorPalette.SLATE, t);
        return new Text(base, new Paint().withForegroundColor(color));
    }
}


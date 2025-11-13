package org.pintoschneider.void_of_the_unfathomable.game.entities;

import org.pintoschneider.void_of_the_unfathomable.core.Offset;
import org.pintoschneider.void_of_the_unfathomable.engine.Engine;
import org.pintoschneider.void_of_the_unfathomable.engine.Scene;
import org.pintoschneider.void_of_the_unfathomable.game.ColorPalette;
import org.pintoschneider.void_of_the_unfathomable.game.Player;
import org.pintoschneider.void_of_the_unfathomable.game.highscore.HighscoreEntry;
import org.pintoschneider.void_of_the_unfathomable.game.highscore.RunStatus;
import org.pintoschneider.void_of_the_unfathomable.game.items.key_items.FamilyPhoto;
import org.pintoschneider.void_of_the_unfathomable.game.map.Map;
import org.pintoschneider.void_of_the_unfathomable.game.map.SpatialProperty;
import org.pintoschneider.void_of_the_unfathomable.game.scenes.GameOver;
import org.pintoschneider.void_of_the_unfathomable.game.scenes.InGame;
import org.pintoschneider.void_of_the_unfathomable.game.turn_steps.TurnStep;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Color;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Paint;

import java.util.List;

public final class PlayerEntity extends DamageableEntity<Player> {
    public PlayerEntity(Offset position, Player associatedObject, Map map) {
        super(position, associatedObject, map);
    }

    @Override
    public Character representation() {
        return '@';
    }

    @Override
    public Paint paint() {
        final Paint basePaint = new Paint().withForegroundColor(ColorPalette.WHITE);
        final Paint equippedPaint = associatedObject().equippedPaint(basePaint);

        final Color color;
        if (damageAnimation.playing()) {
            color = Color.lerp(
                ColorPalette.VERMILION,
                equippedPaint.foregroundColor(),
                damageAnimation.progress()
            );
        } else {
            color = equippedPaint.foregroundColor();
        }

        return new Paint().withForegroundColor(color);
    }

    @Override
    public List<TurnStep> processTurn() {
        associatedObject().incrementTurnsWithoutFluoxetine();

        if (!associatedObject().hasItemOfType(FamilyPhoto.class)) {
            damage(1);
        }

        return List.of();
    }

    @Override
    public SpatialProperty spatialProperty() {
        return new SpatialProperty(false, false);
    }

    @Override
    protected void dispose() {
        Engine.context().sceneManager().push(
            new GameOver()
        ).thenRun(() -> {
            final Scene scene = Engine.context().sceneManager().currentScene();
            if (scene instanceof InGame gameScene) {
                final HighscoreEntry highscoreEntry = HighscoreEntry.fromGame(gameScene, RunStatus.DECEASED);
                Engine.context().sceneManager().pop(highscoreEntry);
            }
        });
    }
}

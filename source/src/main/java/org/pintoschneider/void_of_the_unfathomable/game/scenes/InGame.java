package org.pintoschneider.void_of_the_unfathomable.game.scenes;

import org.pintoschneider.void_of_the_unfathomable.game.core.Context;
import org.pintoschneider.void_of_the_unfathomable.game.core.Keys;
import org.pintoschneider.void_of_the_unfathomable.game.core.Scene;
import org.pintoschneider.void_of_the_unfathomable.game.map.Map;
import org.pintoschneider.void_of_the_unfathomable.game.map.MapComponent;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Component;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Offset;

public final class InGame implements Scene {
    static private final Offset verticalOffset = new Offset(0, 1);
    static private final Offset horizontalOffset = new Offset(1, 0);

    final Map map = new Map(100, 100);
    final Map.Entity player = map.new Entity(new Offset(10, 10), '@');
    Offset offset = Offset.ZERO;

    @Override
    public Component build(Context context) {
        centerOnPlayer(context);
        return new MapComponent(map, offset, null);
    }

    @Override
    public void onKeyPress(Context context, int keyCode) {
        switch (keyCode) {
            case Keys.UP -> player.moveBy(verticalOffset.multiply(-1));
            case Keys.DOWN -> player.moveBy(verticalOffset);
            case Keys.LEFT -> player.moveBy(horizontalOffset.multiply(-1));
            case Keys.RIGHT -> player.moveBy(horizontalOffset);
        }
    }

    void centerOnPlayer(Context context) {
        offset = new Offset(
            player.position().dx() - context.size().width() / 2,
            player.position().dy() - context.size().height() / 2
        );
    }
}

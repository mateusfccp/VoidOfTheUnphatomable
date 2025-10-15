package org.pintoschneider.void_of_the_unfathomable.game.scenes;

import org.pintoschneider.void_of_the_unfathomable.game.core.Context;
import org.pintoschneider.void_of_the_unfathomable.game.core.Keys;
import org.pintoschneider.void_of_the_unfathomable.game.core.Scene;
import org.pintoschneider.void_of_the_unfathomable.game.map.Map;
import org.pintoschneider.void_of_the_unfathomable.game.map.MapComponent;
import org.pintoschneider.void_of_the_unfathomable.game.map.MapTile;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Component;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Offset;

public final class InGame implements Scene {
    final Map map = new Map(100, 100);
    Offset offset = Offset.ZERO;
    Offset cursor = Offset.ZERO;

    static private final Offset verticalOffset = new Offset(0, 1);
    static private final Offset horizontalOffset = new Offset(1, 0);

    @Override
    public Component build(Context context) {
        return new MapComponent(map, offset, cursor);
    }

    @Override
    public void onKeyPress(Context context, int keyCode) {
//        switch (keyCode) {
//            case Keys.UP -> offset = offset.translate(0, -1);
//            case Keys.DOWN -> offset = offset.translate(0, 1);
//            case Keys.LEFT -> offset = offset.translate(-1, 0);
//            case Keys.RIGHT -> offset = offset.translate(1, 0);
//        }
        switch (keyCode) {
            case Keys.UP -> cursor = cursor.translate(0, -1);
            case Keys.DOWN -> cursor = cursor.translate(0, 1);
            case Keys.LEFT -> cursor = cursor.translate(-1, 0);
            case Keys.RIGHT -> cursor = cursor.translate(1, 0);
            case Keys.ENTER -> {
                if (map.getTile(cursor.dx(), cursor.dy()) == MapTile.FLOOR) {
                    map.setTile(cursor.dx(), cursor.dy(), MapTile.WALL);
                } else {
                    map.setTile(cursor.dx(), cursor.dy(), MapTile.FLOOR);
                }
            }
        }
    }
}

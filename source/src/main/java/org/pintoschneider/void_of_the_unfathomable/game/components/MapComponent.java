package org.pintoschneider.void_of_the_unfathomable.game.components;

import org.pintoschneider.void_of_the_unfathomable.core.Offset;
import org.pintoschneider.void_of_the_unfathomable.game.map.Map;
import org.pintoschneider.void_of_the_unfathomable.game.visibility.Visibility;
import org.pintoschneider.void_of_the_unfathomable.ui.core.*;

import java.util.Objects;

public final class MapComponent extends Component {
    final Map map;
    final Visibility visibility;
    final Offset offset;
    final Offset playerOffset;

    public MapComponent(Map map, Visibility visibility, Offset offset, Offset playerOffset) {
        this.map = Objects.requireNonNull(map);
        this.visibility = visibility;
        this.offset = Objects.requireNonNull(offset);
        this.playerOffset = playerOffset;
    }

    @Override
    public void layout(Constraints constraints) {
        size = constraints.biggest();
    }

    @Override
    public void draw(Canvas canvas) {
        final int minX = Math.max(0, offset.dx());
        final int minY = Math.max(0, offset.dy());
        final int maxX = Math.min(offset.dx() + size.width(), map.width());
        final int maxY = Math.min(offset.dy() + size.height(), map.height());

        final Character[][] tiles = map.toCharacterArray();
        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                if (!visibility.isVisible(playerOffset, new Offset(x, y))) {
                    continue;
                }
                canvas.draw(tiles[x][y], x - offset.dx(), y - offset.dy());
            }
        }
    }
}

package org.pintoschneider.void_of_the_unfathomable.game.components;

import org.pintoschneider.void_of_the_unfathomable.core.Offset;
import org.pintoschneider.void_of_the_unfathomable.game.map.Map;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Canvas;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Component;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Constraints;

import java.util.Objects;

/**
 * A component that renders a portion of the game map.
 */
public final class MapComponent extends Component {
    private final Map map;
    private final Offset offset;
    private final Offset playerOffset;

    /**
     * Creates a new MapComponent.
     *
     * @param map          The map to render.
     * @param offset       The offset of the map to render.
     * @param playerOffset The offset of the player on the map.
     */
    public MapComponent(Map map, Offset offset, Offset playerOffset) {
        this.map = Objects.requireNonNull(map);
        this.offset = Objects.requireNonNull(offset);
        this.playerOffset = Objects.requireNonNull(playerOffset);
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
                if (!map.visibility().isVisible(playerOffset, new Offset(x, y))) {
                    continue;
                }

                canvas.draw(tiles[x][y], x - offset.dx(), y - offset.dy());
            }
        }
    }
}

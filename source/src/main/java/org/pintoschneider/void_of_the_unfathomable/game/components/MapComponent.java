package org.pintoschneider.void_of_the_unfathomable.game.components;

import org.pintoschneider.void_of_the_unfathomable.core.Offset;
import org.pintoschneider.void_of_the_unfathomable.game.ColorPalette;
import org.pintoschneider.void_of_the_unfathomable.game.entities.Entity;
import org.pintoschneider.void_of_the_unfathomable.game.map.Map;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Canvas;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Component;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Constraints;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Paint;

import java.util.BitSet;
import java.util.Objects;

/**
 * A component that renders a portion of the game map.
 */
public final class MapComponent extends Component {
    static private final Paint fogPaint = new Paint().withForegroundColor(ColorPalette.GUNMETAL);
    private final Map map;
    private final Offset offset;
    private final Offset playerOffset;
    private final BitSet[] exploredTiles;

    /**
     * Creates a new MapComponent.
     *
     * @param map          The map to render.
     * @param offset       The offset of the map to render.
     * @param playerOffset The offset of the player on the map.
     */
    public MapComponent(Map map, Offset offset, Offset playerOffset, BitSet[] exploredTiles) {
        this.map = Objects.requireNonNull(map);
        this.offset = Objects.requireNonNull(offset);
        this.playerOffset = Objects.requireNonNull(playerOffset);
        this.exploredTiles = Objects.requireNonNull(exploredTiles);
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

        final char[][] tiles = map.toCharacterMatrix();
        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                if (map.visibility().isVisible(playerOffset, new Offset(x, y))) {
                    canvas.draw(tiles[x][y], x - offset.dx(), y - offset.dy());
                    exploredTiles[x].set(y, true);
                } else if (exploredTiles[x].get(y)) {
                    canvas.draw(tiles[x][y], x - offset.dx(), y - offset.dy(), fogPaint);
                }
            }
        }

        for (Entity<?> entity : map.entities()) {
            final int ex = entity.position().dx();
            final int ey = entity.position().dy();
            final boolean withinRenderArea = ex >= minX && ex < maxX && ey >= minY && ey < maxY;
            final boolean visible = withinRenderArea && map.visibility().isVisible(playerOffset, new Offset(ex, ey));

            if (visible) {
                canvas.draw(
                    entity.representation(),
                    ex - offset.dx(),
                    ey - offset.dy(),
                    entity.paint()
                );
            }
        }
    }
}

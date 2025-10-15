package org.pintoschneider.void_of_the_unfathomable.game.map;

import org.pintoschneider.void_of_the_unfathomable.ui.core.*;

public final class MapComponent extends Component {
    final Map map;
    final Offset offset;
    final Offset invertAt;

    public MapComponent(Map map, Offset offset, Offset invertAt) {
        this.map = map;
        this.offset = offset;
        this.invertAt = invertAt;

        assert map != null;
        assert offset != null;
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
                if (invertAt != null && x - offset.dx() == invertAt.dx() && y - offset.dy() == invertAt.dy()) {
                    final Paint invertedPaint = new Paint();
                    invertedPaint.inverted = true;

                    canvas.draw(tiles[x][y], (x - offset.dx()), (y - offset.dy()), invertedPaint);
                    continue;
                }
                canvas.draw(tiles[x][y], x - offset.dx(), y - offset.dy());
            }
        }
    }
}

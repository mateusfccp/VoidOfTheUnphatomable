package org.pintoschneider.void_of_the_unfathomable.game.scenes;

import org.pintoschneider.void_of_the_unfathomable.game.Player;
import org.pintoschneider.void_of_the_unfathomable.game.core.Context;
import org.pintoschneider.void_of_the_unfathomable.game.core.Keys;
import org.pintoschneider.void_of_the_unfathomable.game.core.Scene;
import org.pintoschneider.void_of_the_unfathomable.game.map.Map;
import org.pintoschneider.void_of_the_unfathomable.game.map.MapComponent;
import org.pintoschneider.void_of_the_unfathomable.ui.components.*;
import org.pintoschneider.void_of_the_unfathomable.ui.core.*;

public final class InGame implements Scene {
    static private final Offset verticalOffset = new Offset(0, 1);
    static private final Offset horizontalOffset = new Offset(1, 0);

    final Map map = new Map(100, 100);
    final Player player = new Player();
    final Map.Entity playerEntity = map.new Entity(new Offset(4, 4), '@');
    Offset offset = Offset.ZERO;

    @Override
    public Component build(Context context) {
        centerOnPlayer(context);
        final Paint boldPaint = new Paint();
        boldPaint.bold = true;

        return new Row(
            new Flexible(1, new MapComponent(map, offset, null)),
            new VerticalDivider(),
            new ConstrainedBox(
                new Constraints(12, 12, null, null),
                new Column(
                    new Padding(
                        EdgeInsets.all(1),
                        new Column(
                            new ConstrainedBox(
                                new Constraints(null, null, 1, 1),
                                new Row(
                                    new Text("HP:", boldPaint),
                                    new Text("%3d/%3d".formatted(player.currentHealth(), player.maximumHealth()))
                                )
                            ),
                            new ConstrainedBox(
                                new Constraints(null, null, 1, 1),
                                new Row(
                                    new Text("CP:", boldPaint),
                                    new Text("%3d/%3d".formatted(player.getCurrentColorPoints(), player.getMaximumColorPoints()))
                                )
                            )
                        ).crossAxisAlignment(CrossAxisAlignment.STRETCH)
                            .mainAxisSize(MainAxisSize.MIN)
                    ),
                    new SizedBox(new Size(0, 1), null),
                    new Column(
                        new Padding(
                            EdgeInsets.symmetric(1, 0),
                            new Text("Statuses:", boldPaint)
                        ),
                        new Border()
                    ).crossAxisAlignment(CrossAxisAlignment.STRETCH)
                        .mainAxisSize(MainAxisSize.MIN)
                ).crossAxisAlignment(CrossAxisAlignment.STRETCH)
            )
        ).crossAxisAlignment(CrossAxisAlignment.STRETCH);
    }

    @Override
    public void onKeyPress(Context context, int keyCode) {
        switch (keyCode) {
            case Keys.UP -> playerEntity.moveBy(verticalOffset.multiply(-1));
            case Keys.DOWN -> playerEntity.moveBy(verticalOffset);
            case Keys.LEFT -> playerEntity.moveBy(horizontalOffset.multiply(-1));
            case Keys.RIGHT -> playerEntity.moveBy(horizontalOffset);
        }
    }

    void centerOnPlayer(Context context) {
        offset = new Offset(
            playerEntity.position().dx() - context.size().width() / 2,
            playerEntity.position().dy() - context.size().height() / 2
        );
    }
}

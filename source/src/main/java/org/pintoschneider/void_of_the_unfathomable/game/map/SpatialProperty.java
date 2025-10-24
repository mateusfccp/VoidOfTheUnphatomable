package org.pintoschneider.void_of_the_unfathomable.game.map;

/**
 * A record representing the spatial properties of a map tile.
 *
 * @param walkable Whether the tile can be walked on.
 * @param opaque   Whether the tile blocks vision.
 */
public record SpatialProperty(boolean walkable, boolean opaque) {
}

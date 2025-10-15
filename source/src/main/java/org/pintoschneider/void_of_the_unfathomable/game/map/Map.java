package org.pintoschneider.void_of_the_unfathomable.game.map;

import org.pintoschneider.void_of_the_unfathomable.ui.core.Offset;

import java.util.ArrayList;
import java.util.List;

/**
 * A class representing a 2D map composed of tiles and entities.
 * <p>
 * The tiles describe the terrain of the map, while the entities represent dynamic objects such as players, NPCs, or
 * items.
 */
public class Map {
    private final MapTile[][] tiles;
    private final List<Entity> entities;

    private final int width;
    private final int height;

    public Map(int width, int height) {
        this.width = width;
        this.height = height;
        tiles = new MapTile[height][width];
        entities = new ArrayList<Entity>();

        // Temporary pregenerated map
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (x == 0 || x == width - 1 || y == 0 || y == height - 1) {
                    tiles[x][y] = MapTile.WALL;
                } else {
                    tiles[x][y] = MapTile.FLOOR;
                }
            }
        }
    }

    /**
     * Gets the width of the map in tiles.
     *
     * @return The width of the map.
     */
    public int width() {
        return width;
    }

    /**
     * Gets the height of the map in tiles.
     *
     * @return The height of the map in tiles.
     */
    public int height() {
        return height;
    }

    /**
     * Gets the tile at the specified coordinates.
     * <p>
     * If the coordinates are out of bounds, null is returned.
     *
     * @param x The x coordinate of the tile.
     * @param y The y coordinate of the tile.
     * @return The tile at the specified coordinates, or null if out of bounds.
     */
    public MapTile getTileAt(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return null;
        } else {
            return tiles[x][y];
        }
    }

    /**
     * Sets the tile at the specified coordinates.
     * <p>
     * If the coordinates are out of bounds, the method does nothing.
     *
     * @param x    The x coordinate of the tile.
     * @param y    The y coordinate of the tile.
     * @param tile The tile to set at the specified coordinates.
     */
    public void setTileAt(int x, int y, MapTile tile) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            tiles[x][y] = tile;
        }
    }

    /**
     * Converts the map to a 2D array of characters, taking into the tile definitions.
     *
     * @return A 2D array of characters representing the map.
     */
    public Character[][] toCharacterArray() {
        Character[][] charArray = new Character[height][width];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                charArray[x][y] = getTileCharacter(x, y);
            }
        }

        for (Entity entity : entities) {
            final int ex = entity.position().dx();
            final int ey = entity.position().dy();

            if (ex >= 0 && ex < width && ey >= 0 && ey < height) {
                charArray[ex][ey] = entity.representation();
            }
        }

        return charArray;
    }

    private Character getTileCharacter(int x, int y) {
        final MapTile tile = tiles[x][y];
        if (!tile.autoTile) {
            return tile.getCharacter(0);
        }

        int bitmask = 0;

        // Check left
        if (x > 0 && tiles[x - 1][y] == tile) {
            bitmask |= 0b0001;
        }

        // Check right
        if (x < width - 1 && tiles[x + 1][y] == tile) {
            bitmask |= 0b0010;
        }

        // Check top
        if (y > 0 && tiles[x][y - 1] == tile) {
            bitmask |= 0b0100;
        }

        // Check bottom
        if (y < height - 1 && tiles[x][y + 1] == tile) {
            bitmask |= 0b1000;
        }

        return tile.getCharacter(bitmask);
    }

    /**
     * A class representing an entity on the map.
     * <p>
     * An entity is any kind of object that can be placed in the map and have its position changed, such as the player,
     * enemies, items, etc.
     */
    public class Entity {
        private Offset position;
        private final Character representation;

        public Entity(Offset position, Character representation) {
            this.position = position;
            this.representation = representation;
            entities.add(this);
        }

        /**
         * Gets the position of the entity on the map.
         *
         * @return The position of the entity.
         */
        public Offset position() {
            return position;
        }

        /**
         * Gets the character representation of the entity.
         *
         * @return The character representation of the entity.
         */
        public Character representation() {
            return representation;
        }

        /**
         * Moves the entity by the specified offset.
         *
         * @param offset The offset to move the entity by.
         * @return True if the entity was moved, false if the move was blocked by a non-walkable tile.
         */
        public boolean moveBy(Offset offset) {
            final Offset newPosition = position.translate(offset.dx(), offset.dy());
            final MapTile tileAtNewPosition = getTileAt(newPosition.dx(), newPosition.dy());

            if (tileAtNewPosition != null && tileAtNewPosition.walkable()) {
                position = newPosition;
                return true;
            } else {
                return false;
            }
        }
    }
}


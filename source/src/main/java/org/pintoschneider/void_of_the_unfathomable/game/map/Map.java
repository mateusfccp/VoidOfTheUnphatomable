package org.pintoschneider.void_of_the_unfathomable.game.map;

import org.pintoschneider.void_of_the_unfathomable.core.Offset;
import org.pintoschneider.void_of_the_unfathomable.game.visibility.AdamMillazosVisibility;
import org.pintoschneider.void_of_the_unfathomable.game.visibility.Visibility;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A class representing a 2D map composed of tiles and entities.
 * <p>
 * The tiles describe the terrain of the map, while the entities represent dynamic objects such as players, NPCs, or
 * items.
 */
public class Map {
    private final MapTile[][] tiles;
    private final List<Entity<?>> entities;

    private final int width;
    private final int height;
    private final Visibility visibility;

    /**
     * Creates a new map.
     * <p>
     * Currently, this constructor creates a temporary hardcoded map for testing purposes.
     */
    public Map() {
        this.width = 31;
        this.height = 33;
        tiles = new MapTile[31][33];
        entities = new ArrayList<>();

        // Temporary map
        final int[][] map = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 2, 2, 2, 2, 2, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0},
            {0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 0},
            {0, 1, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0},
            {0, 1, 2, 2, 2, 2, 2, 2, 1, 0, 0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 0},
            {0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0, 0, 0, 0, 1, 2, 2, 1, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0, 0, 0, 0, 1, 1, 2, 1, 0},
            {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0, 0, 0, 0, 0, 1, 2, 1, 0},
            {0, 1, 2, 2, 2, 2, 2, 2, 2, 1, 0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0, 0, 0, 0, 0, 1, 2, 1, 0},
            {0, 1, 2, 2, 2, 2, 2, 2, 2, 1, 0, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 2, 1, 0},
            {0, 1, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0, 0, 0, 0, 1, 2, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 2, 1, 0},
            {0, 1, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 2, 1, 0, 0, 0, 1, 2, 2, 2, 2, 2, 2, 2, 1, 0},
            {0, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0, 0, 1, 2, 1, 1, 1, 1, 1, 2, 1, 0},
            {0, 0, 1, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 2, 1, 0, 0, 0, 1, 2, 1, 0},
            {0, 0, 1, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 1, 0, 0, 0, 1, 2, 1, 0},
            {0, 0, 1, 2, 2, 2, 2, 2, 2, 2, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 0, 0, 0, 1, 2, 1, 0},
            {0, 0, 1, 2, 2, 2, 2, 2, 2, 2, 1, 0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0, 0, 1, 2, 1, 0},
            {0, 0, 1, 2, 2, 2, 2, 2, 2, 2, 1, 0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0, 0, 1, 2, 1, 0},
            {0, 0, 1, 1, 2, 2, 2, 2, 2, 2, 1, 0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0, 0, 1, 2, 1, 0},
            {0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 0, 0, 1, 2, 1, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 0, 1, 2, 1, 0},
            {0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 1, 2, 1, 0},
            {0, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 1, 0, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 1, 2, 1, 0},
            {0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 1, 2, 1, 0},
            {0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 1, 0, 1, 2, 1, 0},
            {0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 2, 1, 0},
            {0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 1, 0},
            {0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 0},
            {0, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0},
            {0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[x].length; x++) {
                if (map[y][x] == 0) {
                    tiles[x][y] = MapTile.VOID;
                } else if (map[y][x] == 1) {
                    tiles[x][y] = MapTile.WALL;
                } else if (map[y][x] == 2) {
                    tiles[x][y] = MapTile.FLOOR;
                }
            }
        }

        visibility = new AdamMillazosVisibility(this);
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
     * Gets the tile at the specified position.
     * <p>
     * If the given positoin is out of bounds, null is returned.
     *
     * @param position The position of the tile.
     * @return The tile at the specified position, or null if out of bounds.
     */
    public MapTile getTileAt(Offset position) {
        return getTileAt(position.dx(), position.dy());
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
     * Gets the entity at the specified position.
     * <p>
     * If no entity is found at the given position, null is returned.
     *
     * @param position The position to check for an entity.
     * @return The entity at the specified position, or null if none is found.
     */
    public Entity<?> getEntityAt(Offset position) {
        for (Entity<?> entity : entities) {
            if (entity.position().equals(position)) {
                return entity;
            }
        }

        return null;
    }

    /**
     * Converts the map to a 2D array of characters, taking into the tile definitions.
     *
     * @return A 2D array of characters representing the map.
     */
    public Character[][] toCharacterArray() {
        Character[][] charArray = new Character[width][height];
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
     * Gets a list of all entities on the map.
     *
     * @return A list of all entities on the map.
     */
    public List<Entity<?>> entities() {
        return new ArrayList<>(entities);
    }

    /**
     * Gets a list of all entities of the specified type on the map.
     *
     * @param type The type of entities to retrieve.
     * @param <T>  The type of the associated object of the entities.
     * @return A list of all entities of the specified type on the map.
     */
    public <T> List<Entity<T>> entitiesOfType(Class<T> type) {
        final List<Entity<T>> result = new ArrayList<>();

        for (Entity<?> entity : entities) {
            if (type.isInstance(entity.associatedObject())) {
                //noinspection unchecked
                result.add((Entity<T>) entity);
            }
        }

        return result;
    }


    /**
     * Gets the visibility strategy used by the map.
     *
     * @return The visibility strategy used by the map.
     */
    public Visibility visibility() {
        return visibility;
    }

    /**
     * A class representing an entity on the map.
     * <p>
     * An entity is any kind of object that can be placed in the map and have its position changed, such as the player,
     * enemies, items, etc.
     */
    public class Entity<T> {
        private Offset position;
        private final Character representation;
        private final T associatedObject;

        public Entity(Offset position, Character representation, T associatedObject) {
            this.position = Objects.requireNonNull(position);
            this.representation = Objects.requireNonNull(representation);
            this.associatedObject = Objects.requireNonNull(associatedObject);
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
         * <p>
         * The entity can only move horizontally or vertically by one tile at a time. Diagonal movement is not allowed.
         *
         * @param offset The offset to move the entity by.
         * @return True if the entity was moved, false if the move was blocked by a non-walkable tile.
         */
        public boolean moveBy(Offset offset) {
            // Okay, this algorithm, if you can call it that, is completely sh*t. We have to implement any kind of
            // pathfinding later, but for now, this will have to do.
            // TODO(mateusfccp): Implement proper pathfinding (A*)
            final Offset targetPosition = position.translate(offset.dx(), offset.dy());

            if (Math.abs(targetPosition.dx()) + Math.abs(targetPosition.dy()) == 1) {
                if (canMoveTo(targetPosition)) {
                    position = targetPosition;
                    return true;
                } else {
                    return false;
                }
            }

            final Offset horizontalMove = new Offset(offset.dx(), 0);

            if (canMoveTo(position.translate(horizontalMove.dx(), horizontalMove.dy()))) {
                position = position.translate(horizontalMove.dx(), horizontalMove.dy());
                return true;
            }

            final Offset verticalMove = new Offset(0, offset.dy());
            if (canMoveTo(position.translate(verticalMove.dx(), verticalMove.dy()))) {
                position = position.translate(verticalMove.dx(), verticalMove.dy());
                return true;
            }

            return true;
        }

        private boolean canMoveTo(Offset targetPosition) {
            final MapTile tileAtNewPosition = getTileAt(targetPosition.dx(), targetPosition.dy());

            if (tileAtNewPosition != null && !tileAtNewPosition.walkable()) {
                return false;
            }

            final Entity<?> entityAtNewPosition = getEntityAt(targetPosition);
            return entityAtNewPosition == null;
        }

        /**
         * Moves the entity one step towards the target position.
         * <p>
         *
         * @param target The target position to move towards.
         * @return True if the entity was moved, false if the move was blocked by a non-walkable tile.
         */
        public boolean moveTowards(Offset target) {
            final int dx = Integer.compare(target.dx(), position.dx());
            final int dy = Integer.compare(target.dy(), position.dy());
            return moveBy(new Offset(dx, dy));
        }

        /**
         * Gets the map this entity belongs to.
         *
         * @return The map this entity belongs to.
         */
        public Map map() {
            return Map.this;
        }

        /**
         * Gets the associated object of the entity.
         *
         * @return The associated object of the entity.
         */
        public T associatedObject() {
            return associatedObject;
        }

        /**
         * Determines if this entity can see another entity based on the map's visibility rules and the positions of
         * both entities.
         *
         * @param other The other entity to check visibility against.
         * @return True if this entity can see the other entity, false otherwise.
         */
        public boolean canSee(Entity<?> other) {
            return visibility.isVisible(this.position, other.position);
        }

        /**
         * Calculates the Chebyshev distance to another entity.
         *
         * @param other The other entity to calculate the distance to.
         * @return The Chebyshev distance to the other entity.
         */
        public int distanceTo(Entity<?> other) {
            return this.position.manhattanDistanceTo(other.position);
        }
    }
}


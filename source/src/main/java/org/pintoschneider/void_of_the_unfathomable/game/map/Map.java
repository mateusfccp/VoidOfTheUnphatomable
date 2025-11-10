package org.pintoschneider.void_of_the_unfathomable.game.map;

import org.pintoschneider.void_of_the_unfathomable.core.Offset;
import org.pintoschneider.void_of_the_unfathomable.game.entities.Entity;
import org.pintoschneider.void_of_the_unfathomable.game.visibility.AdamMillazosVisibility;
import org.pintoschneider.void_of_the_unfathomable.game.visibility.Visibility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * A class representing a 2D map composed of tiles and entities.
 * <p>
 * The tiles describe the terrain of the map, while the entities represent dynamic objects such as players, NPCs, or
 * items.
 */
public final class Map {
    private final MapTile[][] tiles;
    private final Set<Entity<?>> entities = new HashSet<>();

    private final int width;
    private final int height;
    private final Visibility visibility;

    /**
     * Creates a new map.
     */
    public Map() {
        tiles = loadMap("map");
        this.width = tiles.length;
        this.height = tiles[0].length;

        visibility = new AdamMillazosVisibility(this);
    }

    /**
     * Loads and reads the map from the Map.txt file.
     *
     * @param mapName The name of the map within the maps' folder. For instance, if the map file is "map.schneidermap",
     *                the map name is "map".
     * @return The matrix of mapTiles
     */
    private MapTile[][] loadMap(String mapName) {
        final String resourcePath = "/%s.schneidermap".formatted(mapName);
        try (final InputStream inputStream = getClass().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                System.err.println("Cannot find resource: " + resourcePath);
                return new MapTile[0][0];
            }

            try (final InputStreamReader streamReader = new InputStreamReader(inputStream);
                 final BufferedReader reader = new BufferedReader(streamReader)) {

                final int width = Integer.parseInt(reader.readLine());
                final int height = Integer.parseInt(reader.readLine());
                final MapTile[][] map = new MapTile[width][height];

                int x = 0;
                int y = 0;
                int ch;
                while ((ch = reader.read()) != -1) {
                    if (x >= width) {
                        x = 0;
                        y = y + 1;
                        if (y >= height) break;
                    }

                    switch (ch) {
                        case '0':
                            map[x][y] = MapTile.VOID;
                            break;
                        case '1':
                            map[x][y] = MapTile.WALL;
                            break;
                        case '3':
                            map[x][y] = MapTile.DENSE_VOID;
                            break;
                        case '2':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                            map[x][y] = MapTile.FLOOR;
                            break;
                        default:
                            continue;
                    }

                    x = x + 1;
                }

                return map;
            }
        } catch (IOException exception) {
            System.err.printf("Error reading resource: %s%nException: %s", resourcePath, exception);
            return new MapTile[0][0];
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
     * Gets the tile at the specified position.
     * <p>
     * If the given position is out of bounds, null is returned.
     *
     * @param position The position of the tile.
     * @return The tile at the specified position, or null if out of bounds.
     */
    public MapTile getTileAt(Offset position) {
        return getTileAt(position.dx(), position.dy());
    }

    /**
     * Gets a list of all entities at the specified position.
     *
     * @param position The position to check for an entity.
     * @return A list of all entities at the specified position.
     */
    public List<Entity<?>> getEntitiesAt(Offset position) {
        return entities.stream().filter(entity -> entity.position().equals(position)).toList();
    }

    /**
     * Checks if the specified position is walkable.
     * <p>
     * A position is considered walkable if the tile at that position is walkable and all entities at that position
     * are also walkable.
     *
     * @param position The position to check.
     * @return True if the position is walkable, false otherwise.
     */
    public boolean walkable(Offset position) {
        final MapTile tile = getTileAt(position);

        if (tile == null || !tile.walkable()) return false;

        final List<Entity<?>> entitiesAtPosition = getEntitiesAt(position);
        return entitiesAtPosition.isEmpty() || entitiesAtPosition.stream().allMatch(entity -> entity.spatialProperty().walkable());
    }

    /**
     * Converts the map to a 2D array of characters, taking into the tile definitions.
     *
     * @return A 2D array of characters representing the map.
     */
    public char[][] toCharacterMatrix() {
        char[][] charArray = new char[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                charArray[x][y] = getTileCharacterAt(x, y);
            }
        }

        return charArray;
    }

    private Character getTileCharacterAt(int x, int y) {
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
    public <T extends Entity<?>> List<T> getEntitiesOfType(Class<T> type) {
        final List<T> result = new ArrayList<>();

        for (Entity<?> entity : entities) {
            if (type.isInstance(entity)) {
                result.add(type.cast(entity));
            }
        }

        return result;
    }

    /**
     * Adds an entity to the map.
     * <p>
     * This method should only be called by Entity.setMap(Map) to ensure consistency.
     *
     * @param entity The entity to add to the map.
     */
    public void addEntity(Entity<?> entity) {
        Objects.requireNonNull(entity);

        if (entity.map() != null) {
            throw new IllegalStateException(
                """
                    Entity is already associated with another map.%n
                    This means this method was called directly instead of using MapEntity.setMap(Map).%n
                    Current map: %s%n
                    """.formatted(entity.map())
            );
        }

        entities.add(entity);
    }

    /**
     * Removes an entity from the map.
     * <p>
     * This method should only be called by Entity.setMap(Map) to ensure consistency.
     *
     * @param entity The entity to remove from the map.
     */
    public void removeEntity(Entity<?> entity) {
        Objects.requireNonNull(entity);
        entities.remove(entity);
    }

    /**
     * Gets the visibility strategy used by the map.
     *
     * @return The visibility strategy used by the map.
     */
    public Visibility visibility() {
        return visibility;
    }
}

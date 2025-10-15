package org.pintoschneider.void_of_the_unfathomable.game.map;

public class Map {
    MapTile[][] tiles;
    private final int width;
    private final int height;

    public Map(int width, int height) {
        this.width = width;
        this.height = height;
        tiles = new MapTile[height][width];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = MapTile.FLOOR;
            }
        }
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public MapTile getTile(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return null;
        } else {
            return tiles[x][y];
        }
    }

    public void setTile(int x, int y, MapTile tile) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            tiles[x][y] = tile;
        }
    }

    public Character[][] toCharacterArray() {
        Character[][] charArray = new Character[height][width];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                charArray[x][y] = getTileCharacter(x, y);
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
}


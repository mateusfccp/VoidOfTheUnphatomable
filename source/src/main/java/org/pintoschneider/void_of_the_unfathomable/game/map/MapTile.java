package org.pintoschneider.void_of_the_unfathomable.game.map;

public enum MapTile {
    VOID(' ', false, true),
    WALL('┼',
        '─',
        '╴',
        '╶',
        '│',
        '╵',
        '╷',
        '┌',
        '┐',
        '└',
        '┘',
        '├',
        '┤',
        '┬',
        '┴',
        false,
        true),
    FLOOR('·', true, false);

    MapTile(Character symbol, boolean walkable, boolean opaque) {
        this.cross = symbol;
        this.horizontalFull = symbol;
        this.horizontalLeft = symbol;
        this.horizontalRight = symbol;
        this.verticalFull = symbol;
        this.verticalTop = symbol;
        this.verticalBottom = symbol;
        this.cornerNorthWest = symbol;
        this.cornerNorthEast = symbol;
        this.cornerSouthWest = symbol;
        this.cornerSouthEast = symbol;
        this.leftTee = symbol;
        this.rightTee = symbol;
        this.topTee = symbol;
        this.bottomTee = symbol;
        this.walkable = walkable;
        this.opaque = opaque;
        this.autoTile = false;
    }

    MapTile(Character cross,
            Character horizontalFull,
            Character horizontalLeft,
            Character horizontalRight,
            Character verticalFull,
            Character verticalTop,
            Character verticalBottom,
            Character cornerNorthWest,
            Character cornerNorthEast,
            Character cornerSouthWest,
            Character cornerSouthEast,
            Character leftTee,
            Character rightTee,
            Character topTee,
            Character bottomTee,
            boolean walkable,
            boolean opaque) {
        this.cross = cross;
        this.horizontalFull = horizontalFull;
        this.horizontalLeft = horizontalLeft;
        this.horizontalRight = horizontalRight;
        this.verticalFull = verticalFull;
        this.verticalTop = verticalTop;
        this.verticalBottom = verticalBottom;
        this.cornerNorthWest = cornerNorthWest;
        this.cornerNorthEast = cornerNorthEast;
        this.cornerSouthWest = cornerSouthWest;
        this.cornerSouthEast = cornerSouthEast;
        this.leftTee = leftTee;
        this.rightTee = rightTee;
        this.topTee = topTee;
        this.bottomTee = bottomTee;
        this.walkable = walkable;
        this.opaque = opaque;
        this.autoTile = true;
    }

    private final Character cross;
    private final Character horizontalFull;
    private final Character horizontalLeft;
    private final Character horizontalRight;
    private final Character verticalFull;
    private final Character verticalTop;
    private final Character verticalBottom;
    private final Character cornerNorthWest;
    private final Character cornerNorthEast;
    private final Character cornerSouthWest;
    private final Character cornerSouthEast;
    private final Character leftTee;
    private final Character rightTee;
    private final Character topTee;
    private final Character bottomTee;
    private final boolean walkable;
    private final boolean opaque;
    final boolean autoTile;

    /**
     * Returns whether the tile is walkable.
     *
     * @return True if the tile is walkable, false otherwise.
     */
    public boolean walkable() {
        return walkable;
    }

    /**
     * Returns whether the tile is opaque (blocks vision).
     *
     * @return True if the tile is opaque, false otherwise.
     */
    public boolean opaque() {
        return opaque;
    }

    /**
     * Gets the character representation of the tile based on the bitmask of neighboring tiles.
     *
     * @param bitmask The bitmask representing the presence of neighboring tiles.
     *                The bits are ordered as follows: 0bBTLR (Bottom, Top, Left, Right)
     * @return The character representation of the tile.
     */
    Character getCharacter(int bitmask) {
        return switch (bitmask) {
            case 0b0000 -> cross; // No neighbors
            case 0b0001 -> horizontalLeft; // Left neighbor
            case 0b0010 -> horizontalRight; // Right neighbor
            case 0b0011 -> horizontalFull; // Left and right neighbors
            case 0b0100 -> verticalTop; // Top neighbor
            case 0b0101 -> cornerSouthEast; // Top and left neighbors
            case 0b0110 -> cornerSouthWest; // Top and right neighbors
            case 0b0111 -> bottomTee; // Top, left, and right neighbors
            case 0b1000 -> verticalBottom; // Bottom neighbor
            case 0b1001 -> cornerNorthEast; // Bottom and left neighbors
            case 0b1010 -> cornerNorthWest; // Bottom and right neighbors
            case 0b1011 -> topTee; // Bottom, left, and right neighbors
            case 0b1100 -> verticalFull; // Top and bottom neighbors
            case 0b1101 -> rightTee; // Top, bottom, and left neighbors
            case 0b1110 -> leftTee; // Top, bottom, and right neighbors
            case 0b1111 -> cross; // All neighbors
            default -> cross;
        };
    }
}

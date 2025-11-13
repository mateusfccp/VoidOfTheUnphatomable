package org.pintoschneider.void_of_the_unfathomable.game.entities;

import org.pintoschneider.void_of_the_unfathomable.core.Offset;
import org.pintoschneider.void_of_the_unfathomable.game.items.Item;
import org.pintoschneider.void_of_the_unfathomable.game.map.Map;
import org.pintoschneider.void_of_the_unfathomable.game.map.SpatialProperty;
import org.pintoschneider.void_of_the_unfathomable.game.turn_steps.TurnStep;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Paint;

import java.util.*;

/**
 * A class that represents an entity on the map.
 * <p>
 * An entity is any kind of object that can be placed in the map and have its position changed, such as the player,
 * enemies, items, etc.
 *
 * @param <T> The type of the associated object with the entity.
 */
public abstract class Entity<T> {
    private final T associatedObject;
    protected Offset position;
    private Map map;

    protected Entity(Offset position, T associatedObject, Map map) {
        this.position = Objects.requireNonNull(position);
        this.associatedObject = associatedObject;
        setMap(map);
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
    public abstract Character representation();

    /**
     * Gets the paint used to render the entity.
     *
     * @return The paint used to render the entity.
     */
    public Paint paint() {
        return null;
    }

    /**
     * Gets the spatial property of the entity.
     *
     * @return The spatial property of the entity.
     */
    public abstract SpatialProperty spatialProperty();

    /**
     * Moves the entity by the specified offset.
     * <p>
     * The entity can only move horizontally or vertically by one tile at a time. Diagonal movement is not allowed.
     *
     * @param offset The offset to move the entity by.
     * @return True if the entity was moved, false if the move was blocked by a non-walkable tile.
     */
    public boolean moveBy(Offset offset) {
        final Offset targetPosition = position.translate(offset.dx(), offset.dy());

        final boolean didMove;

        final List<Entity<?>> entitiesToInteract;

        if (map.walkable(targetPosition)) {
            position = targetPosition;
            didMove = true;
            entitiesToInteract = map.getEntitiesAt(position);
        } else {
            didMove = false;
            entitiesToInteract = map.getEntitiesAt(targetPosition);
        }

        for (Entity<?> entity : entitiesToInteract) {
            if (entity != this) {
                entity.interact(this);
                this.interact(entity);
            }
        }

        return didMove;
    }

    /**
     * Moves the entity one step towards the target position.
     * <p>
     *
     * @param target The target position to move towards.
     * @return True if the entity was moved, false if the move was blocked by a non-walkable tile.
     */
    public boolean moveTowards(Offset target) {
        final List<Offset> path = getPathTo(target);
        final Offset nextStep = path.size() > 1 ? path.get(1) : null;

        if (nextStep == null) {
            return false;
        } else {
            final int dx = nextStep.dx() - position.dx();
            final int dy = nextStep.dy() - position.dy();
            final Offset offset = new Offset(dx, dy);

            return moveBy(offset);
        }
    }

    /**
     * Gets the map this entity belongs to.
     *
     * @return The map this entity belongs to.
     */
    public Map map() {
        return map;
    }

    /**
     * Sets the map this entity belongs to.
     *
     * @param map The map to set.
     */
    public void setMap(Map map) {
        Objects.requireNonNull(map);

        if (this.map != null) {
            this.map.removeEntity(this);
        }

        map.addEntity(this);
        this.map = map;
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
        return map.visibility().isVisible(this.position, other.position);
    }

    /**
     * Calculates the Chebyshev distance to another entity.
     *
     * @param other The other entity to calculate the distance to.
     * @return The Chebyshev distance to the other entity.
     */
    public int distanceTo(Entity<?> other) {
        return distanceTo(other.position());
    }

    /**
     * Calculates the Chebyshev distance to another position.
     *
     * @param otherPosition The other position to calculate the distance to.
     * @return The Chebyshev distance to the other position.
     */
    public int distanceTo(Offset otherPosition) {
        return position.manhattanDistanceTo(otherPosition);
    }

    /**
     * Processes the entity's turn and returns a list of turn steps to be executed.
     *
     * @return A list of turn steps for the entity's turn.
     */
    public List<TurnStep> processTurn() {
        return List.of();
    }

    /**
     * Interacts with another entity.
     *
     * @param entity The entity to interact with.
     */
    public void interact(Entity<?> entity) {}

    // We can optimize it if necessary by caching paths for unchanged maps
    private List<Offset> getPathTo(Offset target) {
        final Queue<PathNode<Offset>> openSet = new PriorityQueue<>();
        final Set<Offset> closedSet = new HashSet<>();

        final PathNode<Offset> startNode = new PathNode<>(position);
        startNode.gCost = 0;
        startNode.hCost = position.manhattanDistanceTo(target);
        openSet.add(startNode);

        while (!openSet.isEmpty()) {
            final PathNode<Offset> currentNode = openSet.poll();

            // Skip already evaluated nodes
            if (closedSet.contains(currentNode.tile)) continue;

            closedSet.add(currentNode.tile);

            if (currentNode.tile.equals(target)) {
                final List<Offset> path = new ArrayList<>();
                PathNode<Offset> pathNode = currentNode;

                while (pathNode != null) {
                    path.addFirst(pathNode.tile);
                    pathNode = pathNode.parentNode;
                }

                return path;
            }

            final List<Offset> neighbors = List.of(
                currentNode.tile.translate(-1, 0),
                currentNode.tile.translate(1, 0),
                currentNode.tile.translate(0, -1),
                currentNode.tile.translate(0, 1)
            );

            for (Offset neighbor : neighbors) {
                // Skip non-walkable tiles
                if (!neighbor.equals(target) && !map.walkable(neighbor)) continue;

                final PathNode<Offset> neighborNode = new PathNode<>(neighbor);
                neighborNode.gCost = currentNode.gCost + 1;
                neighborNode.hCost = neighbor.manhattanDistanceTo(target);
                neighborNode.parentNode = currentNode;

                openSet.add(neighborNode);
            }
        }

        return Collections.emptyList();
    }

    /**
     * Drops an item at the entity's current position on the map.
     *
     * @param droppedItem The item to drop.
     */
    protected void drop(Item droppedItem) {
        new ItemEntity(position(), droppedItem, map());
    }

    /**
     * Destroys the entity, removing it from the map.
     */
    public void destroy() {
        map().removeEntity(this);
        dispose();
    }

    /**
     * Disposes the entity and releases any resources associated with it.
     */
    protected void dispose() {}

    /**
     * A node used in the A* pathfinding algorithm.
     *
     * @param <T> The type of the tile associated with the node.
     */
    static final private class PathNode<T> implements Comparable<PathNode<T>> {
        final T tile;
        int gCost;
        int hCost;
        PathNode<T> parentNode;

        private PathNode(T tile) {
            this.tile = tile;
        }

        private int fCost() {
            return gCost + hCost;
        }

        @Override
        public int compareTo(PathNode<T> other) {
            return Integer.compare(fCost(), other.fCost());
        }
    }
}

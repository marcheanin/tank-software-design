package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

/**
 * Абстракция препятствия (дерева) на игровом поле
 */
public class Obstacle {
    private final GridPoint2 position;
    private final ObstacleType type;
    private final boolean isPassable;

    public Obstacle(GridPoint2 position, ObstacleType type) {
        this.position = new GridPoint2(position);
        this.type = type;
        this.isPassable = type != ObstacleType.TREE; // деревья непроходимы (баг баг баг был)
    }

    public GridPoint2 getPosition() {
        return new GridPoint2(position);
    }

    public ObstacleType getType() {
        return type;
    }

    public boolean isPassable() {
        return isPassable;
    }

    public boolean isTree() {
        return type == ObstacleType.TREE;
    }

    public boolean isWall() {
        return type == ObstacleType.WALL;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Obstacle obstacle = (Obstacle) obj;
        return position.equals(obstacle.position) && type == obstacle.type;
    }

    @Override
    public int hashCode() {
        return position.hashCode() * 31 + type.hashCode();
    }

    @Override
    public String toString() {
        return String.format("Obstacle{position=%s, type=%s}", position, type);
    }
}

package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

public class Bullet {
    private GridPoint2 position;
    private final Direction direction;
    private final float speed;
    private final float damage;
    private float movementProgress = 0f;

    public Bullet(GridPoint2 startPosition, Direction direction, float speed, float damage) {
        this.position = new GridPoint2(startPosition);
        this.direction = direction;
        this.speed = speed;
        this.damage = damage;
    }

    public GridPoint2 getPosition() {
        return new GridPoint2(position);
    }

    public Direction getDirection() {
        return direction;
    }

    public float getSpeed() {
        return speed;
    }

    public float getDamage() {
        return damage;
    }

    public float getMovementProgress() {
        return movementProgress;
    }

    public GridPoint2 getDestinationCoordinates() {
        GridPoint2 next = GridUtils.move(position, direction);
        return next;
    }

    public void update(float deltaTime) {
        if (movementProgress < 1f) {
            movementProgress = Math.min(1f, movementProgress + deltaTime / speed);
            if (movementProgress >= 1f) {
                position.set(GridUtils.move(position, direction));
                movementProgress = 0f;
            }
        }
    }

    public boolean isMoving() {
        return movementProgress < 1f;
    }
}


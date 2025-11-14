package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

/**
 * Абстракция танка игрока
 */
public class Player {
    private GridPoint2 coordinates;
    private GridPoint2 destinationCoordinates;
    private float movementProgress = 1f;
    private float rotation;
    private final float maxHealth;
    private float currentHealth;
    private final float movementSpeed;

    public Player(GridPoint2 startCoordinates, float maxHealth, float movementSpeed) {
        this.coordinates = new GridPoint2(startCoordinates);
        this.destinationCoordinates = new GridPoint2(startCoordinates);
        this.rotation = 0f;
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.movementSpeed = movementSpeed;
    }

    public GridPoint2 getCoordinates() {
        return new GridPoint2(coordinates);
    }

    public GridPoint2 getDestinationCoordinates() {
        return new GridPoint2(destinationCoordinates);
    }

    public float getMovementProgress() {
        return movementProgress;
    }

    public float getRotation() {
        return rotation;
    }

    public boolean isMoving() {
        return movementProgress < 1f;
    }

    public void setDestination(GridPoint2 destination) {
        this.destinationCoordinates.set(destination);
        this.movementProgress = 0f;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public void updateProgress(float deltaTime) {
        if (movementProgress < 1f) {
            movementProgress = Math.min(1f, movementProgress + deltaTime / movementSpeed);
            if (movementProgress >= 1f) {
                coordinates.set(destinationCoordinates);
            }
        }
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public float getCurrentHealth() {
        return currentHealth;
    }

    public float getMovementSpeed() {
        return movementSpeed;
    }

    public boolean isAlive() {
        return currentHealth > 0;
    }

    public void takeDamage(float damage) {
        currentHealth = Math.max(0, currentHealth - damage);
    }

    public void heal(float amount) {
        currentHealth = Math.min(maxHealth, currentHealth + amount);
    }

    public float getHealthPercentage() {
        return currentHealth / maxHealth;
    }

    public Direction getFacingDirection() {
        for (Direction dir : Direction.values()) {
            if (Math.abs(dir.rotation() - rotation) < 0.1f) {
                return dir;
            }
        }
        return Direction.RIGHT;
    }

    public Bullet shoot(float bulletSpeed, float bulletDamage) {
        Direction facing = getFacingDirection();
        GridPoint2 bulletStart = GridUtils.move(coordinates, facing);
        return new Bullet(bulletStart, facing, bulletSpeed, bulletDamage);
    }
}

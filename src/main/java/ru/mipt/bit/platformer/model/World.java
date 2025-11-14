package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class World {
    private final Player player;
    private final Set<Obstacle> obstacles;
    private final TileGrid tileGrid;
    private final Set<Player> aiTanks;
    private final Set<Bullet> bullets;
    private boolean healthBarsVisible;
    private final List<WorldObserver> observers;

    public World(Player player, Set<Obstacle> obstacles, TileGrid tileGrid) {
        this.player = player;
        this.obstacles = obstacles;
        this.tileGrid = tileGrid;
        this.aiTanks = java.util.Collections.newSetFromMap(new java.util.IdentityHashMap<>());
        this.bullets = new HashSet<>();
        this.healthBarsVisible = false;
        this.observers = new ArrayList<>();
    }

    public World(Player player, Set<Obstacle> obstacles, TileGrid tileGrid, Set<Player> aiTanks) {
        this.player = player;
        this.obstacles = obstacles;
        this.tileGrid = tileGrid;
        this.aiTanks = new HashSet<>(aiTanks);
        this.bullets = new HashSet<>();
        this.healthBarsVisible = false;
        this.observers = new ArrayList<>();
    }

    public void addObserver(WorldObserver observer) {
        observers.add(observer);
        notifyInitialObjects(observer);
    }

    public void removeObserver(WorldObserver observer) {
        observers.remove(observer);
    }

    private void notifyInitialObjects(WorldObserver observer) {
        observer.onObjectAdded(player);
        for (Obstacle obstacle : obstacles) {
            observer.onObjectAdded(obstacle);
        }
        for (Player bot : aiTanks) {
            observer.onObjectAdded(bot);
        }
        for (Bullet bullet : bullets) {
            observer.onObjectAdded(bullet);
        }
    }

    private void notifyObjectAdded(Object obj) {
        for (WorldObserver observer : observers) {
            observer.onObjectAdded(obj);
        }
    }

    private void notifyObjectRemoved(Object obj) {
        for (WorldObserver observer : observers) {
            observer.onObjectRemoved(obj);
        }
    }

    public Player getPlayer() {
        return player;
    }

    public Set<Obstacle> getObstacles() {
        return obstacles;
    }

    public Set<GridPoint2> getObstaclePositions() {
        return obstacles.stream()
                .map(Obstacle::getPosition)
                .collect(Collectors.toSet());
    }

    public TileGrid getTileGrid() {
        return tileGrid;
    }

    public Set<Player> getAiTanks() {
        return aiTanks;
    }

    public java.util.List<Player> getAllTanks() {
        java.util.ArrayList<Player> all = new java.util.ArrayList<>();
        all.add(player);
        all.addAll(aiTanks);
        return all;
    }

    public boolean isHealthBarsVisible() {
        return healthBarsVisible;
    }

    public void toggleHealthBars() {
        healthBarsVisible = !healthBarsVisible;
    }

    public void setHealthBarsVisible(boolean visible) {
        this.healthBarsVisible = visible;
    }

    public boolean hasObstacleAt(GridPoint2 position) {
        return obstacles.stream()
                .anyMatch(obstacle -> obstacle.getPosition().equals(position));
    }

    public Obstacle getObstacleAt(GridPoint2 position) {
        return obstacles.stream()
                .filter(obstacle -> obstacle.getPosition().equals(position))
                .findFirst()
                .orElse(null);
    }

    public Set<Bullet> getBullets() {
        return bullets;
    }

    public void addBullet(Bullet bullet) {
        bullets.add(bullet);
        notifyObjectAdded(bullet);
    }

    public void removeBullet(Bullet bullet) {
        bullets.remove(bullet);
        notifyObjectRemoved(bullet);
    }

    public Player getTankAt(GridPoint2 position) {
        if (player.getCoordinates().equals(position) && player.isAlive()) {
            return player;
        }
        for (Player tank : aiTanks) {
            if (tank.getCoordinates().equals(position) && tank.isAlive()) {
                return tank;
            }
        }
        return null;
    }
}
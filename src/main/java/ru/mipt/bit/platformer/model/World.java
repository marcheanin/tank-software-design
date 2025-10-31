package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import java.util.Set;
import java.util.stream.Collectors;

public class World {
    private final Player player;
    private final Set<Obstacle> obstacles;
    private final TileGrid tileGrid;
    private final Set<Player> aiTanks;

    public World(Player player, Set<Obstacle> obstacles, TileGrid tileGrid) {
        this.player = player;
        this.obstacles = obstacles;
        this.tileGrid = tileGrid;
        this.aiTanks = java.util.Collections.newSetFromMap(new java.util.IdentityHashMap<>());
    }

    public World(Player player, Set<Obstacle> obstacles, TileGrid tileGrid, Set<Player> aiTanks) {
        this.player = player;
        this.obstacles = obstacles;
        this.tileGrid = tileGrid;
        this.aiTanks = new java.util.HashSet<>(aiTanks);
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
}
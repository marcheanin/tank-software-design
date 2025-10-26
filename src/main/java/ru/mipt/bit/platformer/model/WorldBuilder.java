package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import java.util.HashSet;
import java.util.Set;

public class WorldBuilder {
    private Player player;
    private final Set<Obstacle> obstacles = new HashSet<>();
    private TileGrid tileGrid;
    
    public WorldBuilder setPlayer(Player player) {
        this.player = player;
        return this;
    }
    
    public WorldBuilder addObstacle(Obstacle obstacle) {
        this.obstacles.add(obstacle);
        return this;
    }
    
    public WorldBuilder setTileGrid(TileGrid tileGrid) {
        this.tileGrid = tileGrid;
        return this;
    }
    
    public World build() {
        if (player == null) {
            throw new IllegalStateException("Player must be set");
        }
        if (tileGrid == null) {
            throw new IllegalStateException("TileGrid must be set");
        }
        return new World(player, new HashSet<>(obstacles), tileGrid);
    }
}

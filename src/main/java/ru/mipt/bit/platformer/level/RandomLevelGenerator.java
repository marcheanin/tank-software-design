package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.model.*;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class RandomLevelGenerator implements LevelLoader {
    
    private final Random random;
    private final float playerMaxHealth;
    private final float playerMovementSpeed;
    private final int minObstacles;
    private final int maxObstacles;
    
    public RandomLevelGenerator() {
        this(new Random(), 100f, 0.4f, 3, 8);
    }
    
    public RandomLevelGenerator(Random random, float playerMaxHealth, float playerMovementSpeed, 
                               int minObstacles, int maxObstacles) {
        this.random = random;
        this.playerMaxHealth = playerMaxHealth;
        this.playerMovementSpeed = playerMovementSpeed;
        this.minObstacles = minObstacles;
        this.maxObstacles = maxObstacles;
    }
    
    @Override
    public World loadLevel(TileGrid tileGrid) throws LevelLoadingException {
        try {
            GridPoint2 gridSize = tileGrid.getGridSize();
            
            int obstacleCount = minObstacles + random.nextInt(maxObstacles - minObstacles + 1);
            
            Set<GridPoint2> obstaclePositions = generateRandomPositions(
                gridSize, obstacleCount, new HashSet<>()
            );
            
            Set<Obstacle> obstacles = new HashSet<>();
            for (GridPoint2 position : obstaclePositions) {
                obstacles.add(new Obstacle(position, ObstacleType.TREE, false));
            }
            
            Set<GridPoint2> playerPositions = generateRandomPositions(
                gridSize, 1, obstaclePositions
            );
            GridPoint2 playerPosition = playerPositions.iterator().next();
            
            Player player = new Player(playerPosition, playerMaxHealth, playerMovementSpeed);
            
            return new World(player, obstacles, tileGrid);
            
        } catch (Exception e) {
            throw new LevelLoadingException("Failed to generate random level", e);
        }
    }
    
    private Set<GridPoint2> generateRandomPositions(GridPoint2 gridSize, int count, 
                                                   Set<GridPoint2> excludePositions) throws LevelLoadingException {
        Set<GridPoint2> positions = new HashSet<>();
        int maxAttempts = gridSize.x * gridSize.y * 2;
        int attempts = 0;
        
        while (positions.size() < count && attempts < maxAttempts) {
            int x = random.nextInt(gridSize.x);
            int y = random.nextInt(gridSize.y);
            GridPoint2 position = new GridPoint2(x, y);
            
            if (!excludePositions.contains(position) && !positions.contains(position)) {
                positions.add(position);
            }
            attempts++;
        }
        
        if (positions.size() < count) {
            throw new LevelLoadingException(
                String.format("Could not generate %d unique positions in grid %dx%d", 
                            count, gridSize.x, gridSize.y)
            );
        }
        
        return positions;
    }
    
}

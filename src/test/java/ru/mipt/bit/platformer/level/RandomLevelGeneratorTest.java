package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.*;

import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RandomLevelGeneratorTest {

    private RandomLevelGenerator generator;
    private TileGrid tileGrid;

    @BeforeEach
    void setUp() {
        generator = new RandomLevelGenerator();
        tileGrid = new TileGrid(10, 8);
    }

    @Test
    void loadLevel_ShouldCreateWorldWithPlayerAndObstacles() throws LevelLoadingException {
        World world = generator.loadLevel(tileGrid);

        assertNotNull(world);
        assertNotNull(world.getPlayer());
        assertFalse(world.getObstacles().isEmpty());
    }

    @Test
    void loadLevel_ShouldCreatePlayerInValidPosition() throws LevelLoadingException {
        World world = generator.loadLevel(tileGrid);

        Player player = world.getPlayer();
        GridPoint2 playerPosition = player.getCoordinates();
        
        assertTrue(tileGrid.isValidPosition(playerPosition));
        assertFalse(world.hasObstacleAt(playerPosition));
    }

    @Test
    void loadLevel_ShouldCreateObstaclesInValidPositions() throws LevelLoadingException {
        World world = generator.loadLevel(tileGrid);

        for (Obstacle obstacle : world.getObstacles()) {
            GridPoint2 position = obstacle.getPosition();
            assertTrue(tileGrid.isValidPosition(position));
            assertEquals(ObstacleType.TREE, obstacle.getType());
            assertFalse(obstacle.isPassable());
        }
    }

    @Test
    void loadLevel_ShouldNotPlacePlayerOnObstacle() throws LevelLoadingException {
        World world = generator.loadLevel(tileGrid);

        GridPoint2 playerPosition = world.getPlayer().getCoordinates();
        assertFalse(world.hasObstacleAt(playerPosition));
    }

    @Test
    void loadLevel_ShouldGenerateDifferentLevels() throws LevelLoadingException {
        World world1 = generator.loadLevel(tileGrid);
        World world2 = generator.loadLevel(tileGrid);

        Set<GridPoint2> obstacles1 = world1.getObstaclePositions();
        Set<GridPoint2> obstacles2 = world2.getObstaclePositions();
        
        boolean playerPositionsDifferent = !world1.getPlayer().getCoordinates().equals(world2.getPlayer().getCoordinates());
        boolean obstaclesDifferent = !obstacles1.equals(obstacles2);
        
        assertTrue(playerPositionsDifferent || obstaclesDifferent, 
                  "Generated levels should be different (player positions or obstacles)");
    }

    @Test
    void loadLevel_WithCustomParameters_ShouldUseCorrectSettings() throws LevelLoadingException {
        Random fixedRandom = new Random(12345);
        RandomLevelGenerator customGenerator = new RandomLevelGenerator(
            fixedRandom, 150f, 0.5f, 2, 4
        );

        World world = customGenerator.loadLevel(tileGrid);

        Player player = world.getPlayer();
        assertEquals(150f, player.getMaxHealth());
        assertEquals(0.5f, player.getMovementSpeed());
        
        int obstacleCount = world.getObstacles().size();
        assertTrue(obstacleCount >= 2 && obstacleCount <= 4, 
                  "Obstacle count should be between 2 and 4, but was: " + obstacleCount);
    }

    @Test
    void loadLevel_WithSmallGrid_ShouldStillWork() throws LevelLoadingException {
        TileGrid smallGrid = new TileGrid(3, 3);
        RandomLevelGenerator smallGenerator = new RandomLevelGenerator(
            new Random(), 100f, 0.4f, 1, 2
        );

        assertDoesNotThrow(() -> {
            World world = smallGenerator.loadLevel(smallGrid);
            assertNotNull(world);
            assertNotNull(world.getPlayer());
        });
    }
}

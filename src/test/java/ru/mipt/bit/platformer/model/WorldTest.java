package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class WorldTest {

    private World world;
    private Player player;
    private Set<Obstacle> obstacles;
    private TileGrid tileGrid;

    @BeforeEach
    void setUp() {
        player = new Player(new GridPoint2(1, 1));
        obstacles = new HashSet<>();
        obstacles.add(new Obstacle(new GridPoint2(2, 2), ObstacleType.TREE));
        obstacles.add(new Obstacle(new GridPoint2(3, 3), ObstacleType.WALL));
        tileGrid = new TileGrid(10, 10);
        world = new World(player, obstacles, tileGrid);
    }

    @Test
    void getPlayer_shouldReturnCorrectPlayer() {
        assertEquals(player, world.getPlayer());
    }

    @Test
    void getObstacles_shouldReturnAllObstacles() {
        Set<Obstacle> returnedObstacles = world.getObstacles();
        assertEquals(2, returnedObstacles.size());
        assertTrue(returnedObstacles.containsAll(obstacles));
    }

    @Test
    void getObstaclePositions_shouldReturnAllPositions() {
        Set<GridPoint2> positions = world.getObstaclePositions();
        assertEquals(2, positions.size());
        assertTrue(positions.contains(new GridPoint2(2, 2)));
        assertTrue(positions.contains(new GridPoint2(3, 3)));
    }

    @Test
    void hasObstacleAt_shouldReturnTrueForObstaclePositions() {
        assertTrue(world.hasObstacleAt(new GridPoint2(2, 2)));
        assertTrue(world.hasObstacleAt(new GridPoint2(3, 3)));
    }

    @Test
    void hasObstacleAt_shouldReturnFalseForEmptyPositions() {
        assertFalse(world.hasObstacleAt(new GridPoint2(1, 1))); // позиция игрока
        assertFalse(world.hasObstacleAt(new GridPoint2(5, 5))); // пустая позиция
    }

    @Test
    void getObstacleAt_shouldReturnCorrectObstacle() {
        Obstacle obstacle = world.getObstacleAt(new GridPoint2(2, 2));
        assertNotNull(obstacle);
        assertEquals(ObstacleType.TREE, obstacle.getType());
        assertEquals(new GridPoint2(2, 2), obstacle.getPosition());
    }

    @Test
    void getObstacleAt_shouldReturnNullForEmptyPosition() {
        assertNull(world.getObstacleAt(new GridPoint2(5, 5)));
    }

    @Test
    void getTileGrid_shouldReturnCorrectTileGrid() {
        assertEquals(tileGrid, world.getTileGrid());
    }
}

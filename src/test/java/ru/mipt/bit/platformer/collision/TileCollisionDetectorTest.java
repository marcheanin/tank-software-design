package ru.mipt.bit.platformer.collision;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import ru.mipt.bit.platformer.model.*;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TileCollisionDetectorTest {

    private TileCollisionDetector detector;
    private World world;
    private Player player;
    private Set<Obstacle> obstacles;
    private TileGrid tileGrid;

    @BeforeEach
    void setUp(){
        detector = new TileCollisionDetector();
        player = new Player(new GridPoint2(2, 2), 100f, 0.4f);
        obstacles = new HashSet<>();
        obstacles.add(new Obstacle(new GridPoint2(3, 2), ObstacleType.TREE, false));
        obstacles.add(new Obstacle(new GridPoint2(1, 2), ObstacleType.WALL, true));

        tileGrid = new TileGrid(5, 5);

        world = new World(player, obstacles, tileGrid);
    }

    @Test
    void canMove_shouldReturnFalseWhenTargetIsOutOfBounds() {
        assertFalse(detector.canMove(world, new GridPoint2(0, 0), Direction.LEFT));
        assertFalse(detector.canMove(world, new GridPoint2(4, 0), Direction.RIGHT));
        assertFalse(detector.canMove(world, new GridPoint2(0, 0), Direction.DOWN));
        assertFalse(detector.canMove(world, new GridPoint2(0, 4), Direction.UP));
    }

    @Test
    void canMove_shouldReturnFalseWhenTargetHasObstacle() {
        assertFalse(detector.canMove(world, new GridPoint2(2, 2), Direction.RIGHT));
    }

    @Test
    void canMove_shouldReturnTrueWhenTargetIsFree() {
        assertTrue(detector.canMove(world, new GridPoint2(2, 2), Direction.UP));
        assertTrue(detector.canMove(world, new GridPoint2(2, 2), Direction.DOWN));
    }
}

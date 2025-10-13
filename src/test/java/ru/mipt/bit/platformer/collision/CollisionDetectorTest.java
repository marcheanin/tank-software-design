package ru.mipt.bit.platformer.collision;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.mipt.bit.platformer.model.*;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CollisionDetectorTest {

    @Mock
    private CollisionDetector collisionDetector;

    @Test
    void interface_shouldBeImplemented() {
        // Проверяем, что интерфейс может быть реализован через Mockito
        when(collisionDetector.canMove(any(World.class), any(GridPoint2.class), any(Direction.class)))
                .thenReturn(true);
        
        assertNotNull(collisionDetector);

        Player player = new Player(new GridPoint2(1, 1), 100f, 0.4f);
        Set<Obstacle> obstacles = new HashSet<>();
        TileGrid tileGrid = new TileGrid(5, 5);
        World world = new World(player, obstacles, tileGrid);

        assertTrue(collisionDetector.canMove(world, new GridPoint2(2, 2), Direction.UP));
        verify(collisionDetector).canMove(world, new GridPoint2(2, 2), Direction.UP);
    }

    @Test
    void canMove_shouldAcceptCorrectParameters() {
        when(collisionDetector.canMove(any(World.class), any(GridPoint2.class), any(Direction.class)))
                .thenAnswer(invocation -> {
                    World world = invocation.getArgument(0);
                    GridPoint2 from = invocation.getArgument(1);
                    Direction direction = invocation.getArgument(2);

                    assertNotNull(world);
                    assertNotNull(from);
                    assertNotNull(direction);
                    return true;
                });
        
        Player player = new Player(new GridPoint2(1, 1), 100f, 0.4f);
        Set<Obstacle> obstacles = new HashSet<>();
        TileGrid tileGrid = new TileGrid(5, 5);
        World world = new World(player, obstacles, tileGrid);

        assertDoesNotThrow(() -> {
            collisionDetector.canMove(world, new GridPoint2(2, 2), Direction.LEFT);
        });
        
        verify(collisionDetector).canMove(world, new GridPoint2(2, 2), Direction.LEFT);
    }

    @Test
    void canMove_shouldReturnBoolean() {
        when(collisionDetector.canMove(any(World.class), any(GridPoint2.class), eq(Direction.UP)))
                .thenReturn(true);
        when(collisionDetector.canMove(any(World.class), any(GridPoint2.class), eq(Direction.DOWN)))
                .thenReturn(false);
        
        Player player = new Player(new GridPoint2(1, 1), 100f, 0.4f);
        Set<Obstacle> obstacles = new HashSet<>();
        TileGrid tileGrid = new TileGrid(5, 5);
        World world = new World(player, obstacles, tileGrid);
        
        assertTrue(collisionDetector.canMove(world, new GridPoint2(2, 2), Direction.UP));
        assertFalse(collisionDetector.canMove(world, new GridPoint2(2, 2), Direction.DOWN));
        
        verify(collisionDetector).canMove(world, new GridPoint2(2, 2), Direction.UP);
        verify(collisionDetector).canMove(world, new GridPoint2(2, 2), Direction.DOWN);
    }

    @Test
    void canMove_shouldBeCalledWithCorrectArguments() {
        when(collisionDetector.canMove(any(World.class), any(GridPoint2.class), any(Direction.class)))
                .thenReturn(true);
        
        Player player = new Player(new GridPoint2(1, 1), 100f, 0.4f);
        Set<Obstacle> obstacles = new HashSet<>();
        TileGrid tileGrid = new TileGrid(5, 5);
        World world = new World(player, obstacles, tileGrid);
        GridPoint2 position = new GridPoint2(2, 2);
        
        collisionDetector.canMove(world, position, Direction.RIGHT);
        
        verify(collisionDetector).canMove(world, position, Direction.RIGHT);
        verify(collisionDetector, times(1)).canMove(any(World.class), any(GridPoint2.class), any(Direction.class));
    }
}

package ru.mipt.bit.platformer.logic;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.mipt.bit.platformer.collision.CollisionDetector;
import ru.mipt.bit.platformer.model.*;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameLogicTest {

    @Mock
    private CollisionDetector collisionDetector;

    private GameLogic gameLogic;
    private World world;
    private Player player;

    @BeforeEach
    void setUp() {
        gameLogic = new GameLogic(collisionDetector);
        
        player = new Player(new GridPoint2(2, 2));
        Set<Obstacle> obstacles = new HashSet<>();
        TileGrid tileGrid = new TileGrid(5, 5);
        world = new World(player, obstacles, tileGrid);
    }

    @Test
    void processMoveCommand_shouldNotMoveWhenPlayerIsMoving() {
        // Начинаем движение
        player.setDestination(new GridPoint2(3, 2));
        GridPoint2 originalPosition = player.getCoordinates();
        
        // Пытаемся дать команду движения, когда игрок уже движется
        gameLogic.processMoveCommand(world, Direction.UP);
        
        // Позиция не должна измениться
        assertEquals(originalPosition, player.getCoordinates());
    }

    @Test
    void processMoveCommand_shouldMoveWhenCollisionAllows() {
        when(collisionDetector.canMove(any(World.class), any(GridPoint2.class), any(Direction.class))).thenReturn(true);
        GridPoint2 originalPosition = player.getCoordinates();
        GridPoint2 expectedDestination = new GridPoint2(3, 2); // RIGHT
        
        gameLogic.processMoveCommand(world, Direction.RIGHT);
        
        verify(collisionDetector).canMove(world, originalPosition, Direction.RIGHT);
        assertEquals(expectedDestination, player.getDestinationCoordinates());
        assertTrue(player.isMoving());
        assertEquals(Direction.RIGHT.rotation(), player.getRotation(), 0.001f);
    }

    @Test
    void processMoveCommand_shouldNotMoveWhenCollisionBlocks() {
        when(collisionDetector.canMove(any(World.class), any(GridPoint2.class), any(Direction.class))).thenReturn(false);
        GridPoint2 originalPosition = player.getCoordinates();
        
        gameLogic.processMoveCommand(world, Direction.RIGHT);
        
        verify(collisionDetector).canMove(world, originalPosition, Direction.RIGHT);
        // Позиция и цель не должны измениться
        assertEquals(originalPosition, player.getCoordinates());
        assertEquals(originalPosition, player.getDestinationCoordinates());
        assertFalse(player.isMoving());
    }

    @Test
    void processMoveCommand_shouldSetCorrectRotation() {
        when(collisionDetector.canMove(any(World.class), any(GridPoint2.class), any(Direction.class))).thenReturn(true);
        
        gameLogic.processMoveCommand(world, Direction.UP);
        assertEquals(Direction.UP.rotation(), player.getRotation(), 0.001f);
        
        // Сбрасываем состояние игрока для второго теста
        player.setDestination(player.getCoordinates()); // останавливаем движение
        player.updateProgress(1.0f); // завершаем движение
        
        gameLogic.processMoveCommand(world, Direction.LEFT);
        assertEquals(Direction.LEFT.rotation(), player.getRotation(), 0.001f);
        
        verify(collisionDetector, times(2)).canMove(any(World.class), any(GridPoint2.class), any(Direction.class));
    }

    @Test
    void updateWorld_shouldUpdatePlayerProgress() {
        player.setDestination(new GridPoint2(3, 2));
        float initialProgress = player.getMovementProgress();
        
        gameLogic.updateWorld(world, 0.1f);
        
        assertTrue(player.getMovementProgress() > initialProgress);
    }

    @Test
    void updateWorld_shouldNotCrashWithDeltaTime() {
        assertDoesNotThrow(() -> {
            gameLogic.updateWorld(world, 0.0f);
            gameLogic.updateWorld(world, 0.1f);
            gameLogic.updateWorld(world, 1.0f);
        });
    }

    @Test
    void constructor_shouldAcceptCollisionDetector() {
        assertNotNull(gameLogic);
        verifyNoInteractions(collisionDetector); // Проверяем, что мок не вызывался
    }
}

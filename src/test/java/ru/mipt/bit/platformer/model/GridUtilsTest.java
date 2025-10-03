package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

class GridUtilsTest {

    @Test
    void move_shouldWorkCorrectlyForAllDirections() {
        GridPoint2 from = new GridPoint2(5, 3);
        
        assertEquals(new GridPoint2(5, 4), GridUtils.move(from, Direction.UP));
        assertEquals(new GridPoint2(4, 3), GridUtils.move(from, Direction.LEFT));
        assertEquals(new GridPoint2(5, 2), GridUtils.move(from, Direction.DOWN));
        assertEquals(new GridPoint2(6, 3), GridUtils.move(from, Direction.RIGHT));
    }

    @Test
    void move_shouldNotModifyOriginalPoint() {
        GridPoint2 original = new GridPoint2(2, 2);
        GridPoint2 originalCopy = new GridPoint2(original);
        
        GridUtils.move(original, Direction.UP);
        
        assertEquals(originalCopy, original); // оригинальная точка не изменилась
    }

    @ParameterizedTest
    @EnumSource(Direction.class)
    void move_shouldReturnNewPointForEachDirection(Direction direction) {
        GridPoint2 from = new GridPoint2(0, 0);
        GridPoint2 result = GridUtils.move(from, direction);
        
        assertNotSame(from, result); // возвращается новая точка
        assertNotNull(result);
    }
}

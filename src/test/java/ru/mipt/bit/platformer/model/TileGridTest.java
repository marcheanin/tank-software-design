package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TileGridTest {

    private TileGrid tileGrid;

    @BeforeEach
    void setUp() {
        tileGrid = new TileGrid(10, 8);
    }

    @Test
    void constructor_shouldSetWidthAndHeight() {
        assertEquals(new GridPoint2(10, 8), tileGrid.getGridSize());
    }

    @Test
    void isValidPosition_shouldReturnTrueForValidPositions() {
        assertTrue(tileGrid.isValidPosition(new GridPoint2(0, 0))); // левый нижний угол
        assertTrue(tileGrid.isValidPosition(new GridPoint2(9, 7))); // правый верхний угол
        assertTrue(tileGrid.isValidPosition(new GridPoint2(5, 4))); // середина
    }

    @Test
    void isValidPosition_shouldReturnFalseForInvalidPositions() {
        assertFalse(tileGrid.isValidPosition(new GridPoint2(-1, 0))); // отрицательный x
        assertFalse(tileGrid.isValidPosition(new GridPoint2(0, -1))); // отрицательный y
        assertFalse(tileGrid.isValidPosition(new GridPoint2(10, 0))); // x равен ширине
        assertFalse(tileGrid.isValidPosition(new GridPoint2(0, 8)));  // y равен высоте
        assertFalse(tileGrid.isValidPosition(new GridPoint2(15, 10))); // далеко за границами
    }

    @Test
    void isValidPosition_shouldReturnFalseForBoundaryValues() {
        assertFalse(tileGrid.isValidPosition(new GridPoint2(10, 5))); // x на границе
        assertFalse(tileGrid.isValidPosition(new GridPoint2(5, 8)));  // y на границе
    }
}

package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

class DirectionTest {

    @ParameterizedTest
    @EnumSource(Direction.class)
    void eachDirection_shouldHaveCorrectValues(Direction direction) {
        switch (direction) {
            case UP -> {
                assertEquals(0, direction.dx());
                assertEquals(1, direction.dy());
                assertEquals(90f, direction.rotation(), 0.001f);
            }
            case LEFT -> {
                assertEquals(-1, direction.dx());
                assertEquals(0, direction.dy());
                assertEquals(-180f, direction.rotation(), 0.001f);
            }
            case DOWN -> {
                assertEquals(0, direction.dx());
                assertEquals(-1, direction.dy());
                assertEquals(-90f, direction.rotation(), 0.001f);
            }
            case RIGHT -> {
                assertEquals(1, direction.dx());
                assertEquals(0, direction.dy());
                assertEquals(0f, direction.rotation(), 0.001f);
            }
        }
    }

    @Test
    void allDirections_shouldBeAvailable() {
        Direction[] values = Direction.values();
        assertEquals(4, values.length);
        
        assertTrue(java.util.Arrays.asList(values).contains(Direction.UP));
        assertTrue(java.util.Arrays.asList(values).contains(Direction.LEFT));
        assertTrue(java.util.Arrays.asList(values).contains(Direction.DOWN));
        assertTrue(java.util.Arrays.asList(values).contains(Direction.RIGHT));
    }
}

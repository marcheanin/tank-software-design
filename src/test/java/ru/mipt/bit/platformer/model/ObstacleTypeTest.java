package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ObstacleTypeTest {

    @Test
    void values_shouldContainExpectedTypes() {
        ObstacleType[] values = ObstacleType.values();
        assertEquals(3, values.length);
        
        assertTrue(java.util.Arrays.asList(values).contains(ObstacleType.TREE));
        assertTrue(java.util.Arrays.asList(values).contains(ObstacleType.WALL));
    }

    @Test
    void valueOf_shouldWorkForAllTypes() {
        assertEquals(ObstacleType.TREE, ObstacleType.valueOf("TREE"));
        assertEquals(ObstacleType.WALL, ObstacleType.valueOf("WALL"));
    }

    @Test
    void valueOf_shouldThrowExceptionForInvalidName() {
        assertThrows(IllegalArgumentException.class, () -> {
            ObstacleType.valueOf("INVALID");
        });
    }
}

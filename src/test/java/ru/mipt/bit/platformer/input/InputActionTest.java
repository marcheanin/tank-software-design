package ru.mipt.bit.platformer.input;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InputActionTest {

    @Test
    void values_shouldContainExpectedActions() {
        InputAction[] values = InputAction.values();
        assertEquals(2, values.length);
        
        assertTrue(java.util.Arrays.asList(values).contains(InputAction.MOVE));
        assertTrue(java.util.Arrays.asList(values).contains(InputAction.SHOOT));
    }

    @Test
    void valueOf_shouldWorkForAllActions() {
        assertEquals(InputAction.MOVE, InputAction.valueOf("MOVE"));
        assertEquals(InputAction.SHOOT, InputAction.valueOf("SHOOT"));
    }

    @Test
    void valueOf_shouldThrowExceptionForInvalidName() {
        assertThrows(IllegalArgumentException.class, () -> {
            InputAction.valueOf("INVALID");
        });
    }
}

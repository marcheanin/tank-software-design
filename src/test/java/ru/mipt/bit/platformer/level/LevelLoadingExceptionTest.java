package ru.mipt.bit.platformer.level;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LevelLoadingExceptionTest {

    @Test
    void constructor_WithMessage_ShouldSetMessage() {
        String message = "Test error message";

        LevelLoadingException exception = new LevelLoadingException(message);

        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void constructor_WithMessageAndCause_ShouldSetBoth() {
        String message = "Test error message";
        Throwable cause = new RuntimeException("Root cause");

        LevelLoadingException exception = new LevelLoadingException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void constructor_WithNullMessage_ShouldHandleGracefully() {
        assertDoesNotThrow(() -> {
            LevelLoadingException exception = new LevelLoadingException(null);
            assertNull(exception.getMessage());
        });
    }

    @Test
    void constructor_WithNullCause_ShouldHandleGracefully() {
        assertDoesNotThrow(() -> {
            LevelLoadingException exception = new LevelLoadingException("Message", null);
            assertEquals("Message", exception.getMessage());
            assertNull(exception.getCause());
        });
    }
}

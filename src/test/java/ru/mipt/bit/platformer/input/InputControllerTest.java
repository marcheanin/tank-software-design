package ru.mipt.bit.platformer.input;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InputControllerTest {

    @Test
    void interface_shouldBeImplemented() {
        InputController controller = new InputController() {
            @Override
            public java.util.Collection<InputEvent> poll() {
                return java.util.Collections.emptyList();
            }
        };
        
        assertNotNull(controller);
        assertTrue(controller.poll().isEmpty());
    }

    @Test
    void poll_shouldReturnCollectionOfInputEvents() {
        InputController controller = new InputController() {
            @Override
            public java.util.Collection<InputEvent> poll() {
                return java.util.Arrays.asList(
                    InputEvent.move(ru.mipt.bit.platformer.model.Direction.UP),
                    InputEvent.shoot()
                );
            }
        };
        
        var events = controller.poll();
        assertEquals(2, events.size());
        assertTrue(events.stream().anyMatch(e -> e.getAction() == InputAction.MOVE));
        assertTrue(events.stream().anyMatch(e -> e.getAction() == InputAction.SHOOT));
    }
}

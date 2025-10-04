package ru.mipt.bit.platformer.input;

import ru.mipt.bit.platformer.model.Direction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InputEventTest {

    @ParameterizedTest
    @EnumSource(Direction.class)
    void move_shouldCreateCorrectMoveEvent(Direction direction) {
        InputEvent event = InputEvent.move(direction);
        
        assertEquals(InputAction.MOVE, event.getAction());
        assertEquals(Optional.of(direction), event.getDirection());
    }

    @Test
    void shoot_shouldCreateCorrectShootEvent() {
        InputEvent event = InputEvent.shoot();
        
        assertEquals(InputAction.SHOOT, event.getAction());
        assertEquals(Optional.empty(), event.getDirection());
    }

    @Test
    void getDirection_shouldReturnEmptyOptionalForShootEvent() {
        InputEvent shootEvent = InputEvent.shoot();
        
        assertFalse(shootEvent.getDirection().isPresent());
    }

    @Test
    void getDirection_shouldReturnPresentOptionalForMoveEvent() {
        InputEvent moveEvent = InputEvent.move(Direction.UP);
        
        assertTrue(moveEvent.getDirection().isPresent());
        assertEquals(Direction.UP, moveEvent.getDirection().get());
    }

    @Test
    void events_shouldBeImmutable() {
        InputEvent moveEvent = InputEvent.move(Direction.LEFT);
        InputEvent shootEvent = InputEvent.shoot();

        assertEquals(InputAction.MOVE, moveEvent.getAction());
        assertEquals(InputAction.SHOOT, shootEvent.getAction());
    }
}

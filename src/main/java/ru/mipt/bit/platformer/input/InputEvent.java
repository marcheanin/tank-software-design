package ru.mipt.bit.platformer.input;

import ru.mipt.bit.platformer.model.Direction;
import java.util.Optional;

public final class InputEvent {
    private final InputAction action;
    private final Direction direction;

    private InputEvent(InputAction action, Direction direction) {
        this.action = action;
        this.direction = direction;
    }

    public static InputEvent move(Direction direction) {
        return new InputEvent(InputAction.MOVE, direction);
    }

    public static InputEvent shoot() {
        return new InputEvent(InputAction.SHOOT, null);
    }

    public static InputEvent toggleHealth() {
        return new InputEvent(InputAction.TOGGLE_HEALTH, null);
    }

    public InputAction getAction() {
        return action;
    }

    public Optional<Direction> getDirection() {
        return Optional.ofNullable(direction);
    }

}

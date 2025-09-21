package ru.mipt.bit.platformer.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import ru.mipt.bit.platformer.model.Direction;

import java.util.Optional;
import java.util.Map;

public final class GdxKeyboardInputController implements InputController {
    
    private static final Map<Direction, int[]> DIRECTION_KEYS = Map.of(
        Direction.UP, new int[]{Input.Keys.UP, Input.Keys.W},
        Direction.DOWN, new int[]{Input.Keys.DOWN, Input.Keys.S},
        Direction.LEFT, new int[]{Input.Keys.LEFT, Input.Keys.A},
        Direction.RIGHT, new int[]{Input.Keys.RIGHT, Input.Keys.D}
    );
    
    @Override
    public Optional<Direction> pollMove() {
        for (Map.Entry<Direction, int[]> entry : DIRECTION_KEYS.entrySet()) {
            if (isAnyKeyPressed(entry.getValue())) {
                return Optional.of(entry.getKey());
            }
        }
        return Optional.empty();
    }
    
    private boolean isAnyKeyPressed(int[] keys) {
        for (int key : keys) {
            if (Gdx.input.isKeyPressed(key)) {
                return true;
            }
        }
        return false;
    }
}

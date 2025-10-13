package ru.mipt.bit.platformer.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import ru.mipt.bit.platformer.model.Direction;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

public final class GdxKeyboardInputController implements InputController {
    
    @Override
    public Collection<InputEvent> poll() {
        List<InputEvent> events = new ArrayList<>();

        if (isAnyKeyPressed(Input.Keys.UP, Input.Keys.W)) {
            events.add(InputEvent.move(Direction.UP));
        }

        if (isAnyKeyPressed(Input.Keys.LEFT, Input.Keys.A)) {
            events.add(InputEvent.move(Direction.LEFT));
        }

        if (isAnyKeyPressed(Input.Keys.DOWN, Input.Keys.S)) {
            events.add(InputEvent.move(Direction.DOWN));
        }

        if (isAnyKeyPressed(Input.Keys.RIGHT, Input.Keys.D)) {
            events.add(InputEvent.move(Direction.RIGHT));
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            events.add(InputEvent.shoot());
        }

        return events;
    }
    
    private boolean isAnyKeyPressed(int... keys) {
        for (int key : keys) {
            if (Gdx.input.isKeyPressed(key)) {
                return true;
            }
        }
        return false;
    }
}

package ru.mipt.bit.platformer.input;

import java.util.Collection;

public interface InputController {
    Collection <InputEvent> poll();
}

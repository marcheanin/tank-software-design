package ru.mipt.bit.platformer.input;

import ru.mipt.bit.platformer.model.Direction;
import java.util.Optional;
 

public interface InputController {
    Optional<Direction> pollMove();
}

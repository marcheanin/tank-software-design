package ru.mipt.bit.platformer.collision;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.World;

public interface CollisionDetector {
    boolean canMove(World world, GridPoint2 from, Direction direction);
}

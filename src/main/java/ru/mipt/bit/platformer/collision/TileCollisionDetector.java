package ru.mipt.bit.platformer.collision;

import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.World;
import com.badlogic.gdx.math.GridPoint2;
import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public class TileCollisionDetector implements CollisionDetector {

    @Override
    public boolean canMove(World world, GridPoint2 from, Direction direction) {
        GridPoint2 target = getTargetPosition(from, direction);
        if (!world.getTileGrid().isValidPosition(target)) {
            return false;
        }
        return !world.hasObstacleAt(target);
    }

    private GridPoint2 getTargetPosition(GridPoint2 from, Direction direction) {
        return switch (direction) {
            case UP -> incrementedY(from);
            case LEFT -> decrementedX(from);
            case DOWN -> decrementedY(from);
            case RIGHT -> incrementedX(from);
        };
    }
}
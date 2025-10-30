package ru.mipt.bit.platformer.command;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GridUtils;
import ru.mipt.bit.platformer.model.Player;
import ru.mipt.bit.platformer.model.World;

public class MoveCommand implements Command {
    private final Player player;
    private final Direction direction;

    public MoveCommand(Player player, Direction direction) {
        this.player = player;
        this.direction = direction;
    }

    @Override
    public void execute(World world, CommandContext context) {
        if (player.isMoving()) {
            return;
        }
        GridPoint2 from = player.getCoordinates();
        GridPoint2 to = GridUtils.move(from, direction);

        // проверки валидности
        if (!world.getTileGrid().isValidPosition(to)) {
            return;
        }
        if (world.hasObstacleAt(to)) {
            return;
        }
        if (!context.isFree(to)) {
            return;
        }
        player.setDestination(to);
        player.setRotation(direction.rotation());
        context.reserve(from);
        context.reserve(to);
    }
}



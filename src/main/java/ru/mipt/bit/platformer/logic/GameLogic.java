package ru.mipt.bit.platformer.logic;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.collision.CollisionDetector;
import ru.mipt.bit.platformer.command.Command;
import ru.mipt.bit.platformer.command.CommandContext;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GridUtils;
import ru.mipt.bit.platformer.model.Player;
import ru.mipt.bit.platformer.model.World;
import java.util.Collection;

public class GameLogic {
    private final CollisionDetector collisionDetector;

    public GameLogic(CollisionDetector collisionDetector) {
        this.collisionDetector = collisionDetector;
    }

    public void processMoveCommand(World world, Direction direction) {
        if (world.getPlayer().isMoving()) {
            return;
        }

        GridPoint2 currentPos = world.getPlayer().getCoordinates();
        GridPoint2 target = GridUtils.move(currentPos, direction);

        if (collisionDetector.canMove(world, currentPos, direction)) {
            world.getPlayer().setDestination(target);
            world.getPlayer().setRotation(direction.rotation());
        }
    }

    public void updateWorld(World world, float deltaTime) {
        for (Player tank : world.getAllTanks()) {
            tank.updateProgress(deltaTime);
        }
    }

    public void processShootCommand(World world) {} // для будущей стрельбы

    public void processCommands(World world, Collection<Command> commands) {
        CommandContext context = new CommandContext(world);
        for (Command command : commands) {
            command.execute(world, context);
        }
    }
}
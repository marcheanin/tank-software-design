package ru.mipt.bit.platformer.logic;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.command.Command;
import ru.mipt.bit.platformer.command.MoveCommand;
import ru.mipt.bit.platformer.model.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CommandAndOccupancyTest {

    private World emptyWorld(int width, int height, GridPoint2 playerPos, Set<GridPoint2> botPositions) {
        TileGrid grid = new TileGrid(width, height);
        Player player = new Player(playerPos, 100f, 0.4f);
        Set<Obstacle> obstacles = new HashSet<>();
        Set<Player> bots = new HashSet<>();
        for (GridPoint2 pos : botPositions) {
            bots.add(new Player(pos, 100f, 0.4f));
        }
        return new World(player, obstacles, grid, bots);
    }

    @Test
    void twoBotsCannotMoveIntoSameTargetInSameTick() {
        World world = emptyWorld(3, 3, new GridPoint2(0, 0), Set.of(new GridPoint2(1, 1), new GridPoint2(1, 2)));
        GameLogic logic = new GameLogic((w, from, dir) -> true);

        Player[] bots = world.getAiTanks().toArray(new Player[0]);
        Player b1 = bots[0];
        Player b2 = bots[1];

        List<Command> commands = new ArrayList<>();
        commands.add(new MoveCommand(b1, Direction.UP));    // (1,1)->(1,2)
        commands.add(new MoveCommand(b2, Direction.DOWN));  // (1,2)->(1,1)

        logic.processCommands(world, commands);

        int movingCount = 0;
        for (Player p : world.getAiTanks()) {
            if (p.isMoving()) movingCount++;
        }
        assertTrue(movingCount <= 1);
    }

    @Test
    void cannotMoveOutsideGrid() {
        World world = emptyWorld(2, 2, new GridPoint2(0, 0), Set.of());
        GameLogic logic = new GameLogic((w, from, dir) -> true);

        List<Command> commands = new ArrayList<>();
        commands.add(new MoveCommand(world.getPlayer(), Direction.LEFT));
        commands.add(new MoveCommand(world.getPlayer(), Direction.DOWN));

        logic.processCommands(world, commands);

        assertFalse(world.getPlayer().isMoving());
        assertEquals(new GridPoint2(0, 0), world.getPlayer().getCoordinates());
    }

    @Test
    void cannotEnterObstacle() {
        TileGrid grid = new TileGrid(3, 3);
        Player player = new Player(new GridPoint2(1, 1), 100f, 0.4f);
        Set<Obstacle> obstacles = new HashSet<>();
        obstacles.add(new Obstacle(new GridPoint2(2, 1), ObstacleType.TREE, false));
        World world = new World(player, obstacles, grid);
        GameLogic logic = new GameLogic((w, from, dir) -> true);

        List<Command> commands = new ArrayList<>();
        commands.add(new MoveCommand(world.getPlayer(), Direction.RIGHT));
        logic.processCommands(world, commands);

        assertFalse(world.getPlayer().isMoving());
        assertEquals(new GridPoint2(1, 1), world.getPlayer().getCoordinates());
    }
}



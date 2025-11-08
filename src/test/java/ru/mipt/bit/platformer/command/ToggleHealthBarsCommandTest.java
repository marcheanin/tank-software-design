package ru.mipt.bit.platformer.command;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Player;
import ru.mipt.bit.platformer.model.TileGrid;
import ru.mipt.bit.platformer.model.World;
import ru.mipt.bit.platformer.model.Obstacle;
import com.badlogic.gdx.math.GridPoint2;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class ToggleHealthBarsCommandTest {

    @Test
    void execute_ShouldToggleVisibility() {
        Player player = new Player(new GridPoint2(0, 0), 100f, 0.4f);
        World world = new World(player, new HashSet<Obstacle>(), new TileGrid(5, 5));
        ToggleHealthBarsCommand command = new ToggleHealthBarsCommand();
        CommandContext context = new CommandContext(world);

        assertFalse(world.isHealthBarsVisible());

        command.execute(world, context);
        assertTrue(world.isHealthBarsVisible());

        command.execute(world, context);
        assertFalse(world.isHealthBarsVisible());
    }
}



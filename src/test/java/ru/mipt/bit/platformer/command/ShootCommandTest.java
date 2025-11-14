package ru.mipt.bit.platformer.command;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.*;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ShootCommandTest {

    private World world;
    private Player player;

    @BeforeEach
    void setUp() {
        TileGrid grid = new TileGrid(10, 8);
        player = new Player(new GridPoint2(5, 5), 100f, 0.4f);
        player.setRotation(Direction.RIGHT.rotation());
        Set<Obstacle> obstacles = new HashSet<>();
        world = new World(player, obstacles, grid);
    }

    @Test
    void execute_ShouldCreateBullet() {
        ShootCommand command = new ShootCommand(player, 0.2f, 25f);
        CommandContext context = new CommandContext(world);

        assertEquals(0, world.getBullets().size());
        command.execute(world, context);
        assertEquals(1, world.getBullets().size());
    }

    @Test
    void execute_ShouldCreateBulletAtCorrectPosition() {
        player.setRotation(Direction.RIGHT.rotation());
        ShootCommand command = new ShootCommand(player, 0.2f, 25f);
        CommandContext context = new CommandContext(world);

        command.execute(world, context);

        Bullet bullet = world.getBullets().iterator().next();
        GridPoint2 expectedPos = GridUtils.move(player.getCoordinates(), Direction.RIGHT);
        assertEquals(expectedPos, bullet.getPosition());
        assertEquals(Direction.RIGHT, bullet.getDirection());
    }

    @Test
    void execute_ShouldNotCreateBulletIfPlayerDead() {
        player.takeDamage(100f);
        assertFalse(player.isAlive());

        ShootCommand command = new ShootCommand(player, 0.2f, 25f);
        CommandContext context = new CommandContext(world);

        command.execute(world, context);
        assertEquals(0, world.getBullets().size());
    }
}


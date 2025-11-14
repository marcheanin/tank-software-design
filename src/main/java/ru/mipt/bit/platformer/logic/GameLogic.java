package ru.mipt.bit.platformer.logic;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.collision.BulletCollisionDetector;
import ru.mipt.bit.platformer.collision.CollisionDetector;
import ru.mipt.bit.platformer.command.Command;
import ru.mipt.bit.platformer.command.CommandContext;
import ru.mipt.bit.platformer.model.Bullet;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GridUtils;
import ru.mipt.bit.platformer.model.Player;
import ru.mipt.bit.platformer.model.World;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GameLogic {
    private final CollisionDetector collisionDetector;
    private final BulletCollisionDetector bulletCollisionDetector;
    private final float bulletSpeed;
    private final float bulletDamage;

    public GameLogic(CollisionDetector collisionDetector) {
        this(collisionDetector, 0.2f, 25f);
    }

    public GameLogic(CollisionDetector collisionDetector, float bulletSpeed, float bulletDamage) {
        this.collisionDetector = collisionDetector;
        this.bulletCollisionDetector = new BulletCollisionDetector();
        this.bulletSpeed = bulletSpeed;
        this.bulletDamage = bulletDamage;
    }

    public float getBulletSpeed() {
        return bulletSpeed;
    }

    public float getBulletDamage() {
        return bulletDamage;
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

        List<Bullet> bulletsToRemove = new ArrayList<>();
        for (Bullet bullet : world.getBullets()) {
            bullet.update(deltaTime);
            List<BulletCollisionDetector.CollisionResult> collisions = bulletCollisionDetector.checkCollisions(world, bullet);
            if (!collisions.isEmpty()) {
                BulletCollisionDetector.CollisionResult collision = collisions.get(0);
                bulletsToRemove.add(bullet);

                if (collision.getType() == BulletCollisionDetector.CollisionType.TANK && collision.getHitTank() != null) {
                    Player hitTank = collision.getHitTank();
                    hitTank.takeDamage(bullet.getDamage());
                    if (!hitTank.isAlive()) {
                        if (hitTank == world.getPlayer()) {
                            world.getPlayer().takeDamage(0);
                        } else {
                            world.getAiTanks().remove(hitTank);
                        }
                    }
                }

                if (collision.getType() == BulletCollisionDetector.CollisionType.BULLET) {
                    for (Bullet other : world.getBullets()) {
                        if (other != bullet && other.getPosition().equals(bullet.getPosition())) {
                            bulletsToRemove.add(other);
                        }
                    }
                }
            }
        }

        for (Bullet bullet : bulletsToRemove) {
            world.removeBullet(bullet);
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
package ru.mipt.bit.platformer.collision;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.model.Bullet;
import ru.mipt.bit.platformer.model.Player;
import ru.mipt.bit.platformer.model.World;

import java.util.ArrayList;
import java.util.List;

public class BulletCollisionDetector {

    public List<CollisionResult> checkCollisions(World world, Bullet bullet) {
        List<CollisionResult> results = new ArrayList<>();
        GridPoint2 bulletPos = bullet.getPosition();

        if (!world.getTileGrid().isValidPosition(bulletPos)) {
            results.add(new CollisionResult(CollisionType.BOUNDARY, null));
            return results;
        }

        if (world.hasObstacleAt(bulletPos)) {
            results.add(new CollisionResult(CollisionType.OBSTACLE, null));
            return results;
        }

        Player hitTank = world.getTankAt(bulletPos);
        if (hitTank != null) {
            results.add(new CollisionResult(CollisionType.TANK, hitTank));
            return results;
        }

        for (Bullet otherBullet : world.getBullets()) {
            if (otherBullet != bullet && otherBullet.getPosition().equals(bulletPos)) {
                results.add(new CollisionResult(CollisionType.BULLET, null));
                return results;
            }
        }

        return results;
    }

    public enum CollisionType {
        BOUNDARY,
        OBSTACLE,
        TANK,
        BULLET
    }

    public static class CollisionResult {
        private final CollisionType type;
        private final Player hitTank;

        public CollisionResult(CollisionType type, Player hitTank) {
            this.type = type;
            this.hitTank = hitTank;
        }

        public CollisionType getType() {
            return type;
        }

        public Player getHitTank() {
            return hitTank;
        }
    }
}


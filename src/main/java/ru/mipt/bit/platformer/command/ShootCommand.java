package ru.mipt.bit.platformer.command;

import ru.mipt.bit.platformer.model.Bullet;
import ru.mipt.bit.platformer.model.Player;
import ru.mipt.bit.platformer.model.World;

public class ShootCommand implements Command {
    private final Player player;
    private final float bulletSpeed;
    private final float bulletDamage;

    public ShootCommand(Player player, float bulletSpeed, float bulletDamage) {
        this.player = player;
        this.bulletSpeed = bulletSpeed;
        this.bulletDamage = bulletDamage;
    }

    @Override
    public void execute(World world, CommandContext context) {
        if (!player.isAlive()) {
            return;
        }
        Bullet bullet = player.shoot(bulletSpeed, bulletDamage);
        world.addBullet(bullet);
    }
}


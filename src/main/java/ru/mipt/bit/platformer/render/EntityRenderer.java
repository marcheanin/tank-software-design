package ru.mipt.bit.platformer.render;

import com.badlogic.gdx.graphics.g2d.Batch;
import ru.mipt.bit.platformer.model.Obstacle;
import ru.mipt.bit.platformer.model.Player;
import ru.mipt.bit.platformer.model.World;
import ru.mipt.bit.platformer.util.TileMovement;

public class EntityRenderer {

    private final PlayerRenderer playerRenderer;
    private final PlayerRenderer botRenderer;
    private final ObstacleRenderer obstacleRenderer;
    private final TileMovement tileMovement;

    public EntityRenderer(TileMovement tileMovement,
                          PlayerRenderer playerRenderer,
                          PlayerRenderer botRenderer,
                          ObstacleRenderer obstacleRenderer) {
        this.tileMovement = tileMovement;
        this.playerRenderer = playerRenderer;
        this.botRenderer = botRenderer;
        this.obstacleRenderer = obstacleRenderer;
    }

    public void render(World world, Batch batch) {
        batch.begin();
        Player player = world.getPlayer();
        playerRenderer.render(player, batch, tileMovement);

        for (Player bot : world.getAiTanks()) {
            botRenderer.render(bot, batch, tileMovement);
        }

        for (Obstacle obstacle : world.getObstacles()) {
            obstacleRenderer.render(obstacle.getPosition(), batch);
        }

        batch.end();
    }
}

package ru.mipt.bit.platformer.render;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.Player;
import ru.mipt.bit.platformer.util.TileMovement;
import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public class PlayerRenderer {
    private final TextureRegion playerGraphics;
    private final Rectangle playerRectangle;

    public PlayerRenderer(TextureRegion playerGraphics) {
        this.playerGraphics = playerGraphics;
        this.playerRectangle = createBoundingRectangle(playerGraphics);
    }

    public void render(Player player, Batch batch, TileMovement tileMovement) {
        tileMovement.moveRectangleBetweenTileCenters(
                playerRectangle,
                player.getCoordinates(),
                player.getDestinationCoordinates(),
                player.getMovementProgress()
        );

        drawTextureRegionUnscaled(batch, playerGraphics, playerRectangle, player.getRotation());
    }
}
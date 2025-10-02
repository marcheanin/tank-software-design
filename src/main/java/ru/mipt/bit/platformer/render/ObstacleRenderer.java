package ru.mipt.bit.platformer.render;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public class ObstacleRenderer {
    private final TextureRegion obstacleGraphics;
    private final Rectangle obstacleRectangle;
    private final TiledMapTileLayer tileLayer;

    public ObstacleRenderer(TextureRegion obstacleGraphics, TiledMapTileLayer tileLayer) {
        this.obstacleGraphics = obstacleGraphics;
        this.obstacleRectangle = createBoundingRectangle(obstacleGraphics);
        this.tileLayer = tileLayer;
    }

    public void render(GridPoint2 obstaclePosition, Batch batch) {
        moveRectangleAtTileCenter(tileLayer, obstacleRectangle, obstaclePosition);
        drawTextureRegionUnscaled(batch, obstacleGraphics, obstacleRectangle, 0f);
    }
}
package ru.mipt.bit.platformer.render;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.TileGrid;
import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public class ObstacleRenderer {
    private final TextureRegion obstacleGraphics;
    private final Rectangle obstacleRectangle;

    public ObstacleRenderer(TextureRegion obstacleGraphics) {
        this.obstacleGraphics = obstacleGraphics;
        this.obstacleRectangle = createBoundingRectangle(obstacleGraphics);
    }

    public void render(GridPoint2 obstaclePosition, Batch batch, TileGrid tileGrid) {
        // Позиционируем прямоугольник по центру тайла
        moveRectangleAtTileCenter(tileGrid.getTileLayer(), obstacleRectangle, obstaclePosition);

        // Рендерим препятствие
        drawTextureRegionUnscaled(batch, obstacleGraphics, obstacleRectangle, 0f);
    }
}
package ru.mipt.bit.platformer.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import ru.mipt.bit.platformer.model.Player;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.function.BooleanSupplier;

public class HealthBarRenderer implements TankRenderer, Disposable {
    private final TankRenderer delegate;
    private final BooleanSupplier visibleSupplier;
    private final TextureRegion pixelRegion;
    private final Texture ownedTexture;

    public HealthBarRenderer(TankRenderer delegate, BooleanSupplier visibleSupplier) {
        this(delegate, visibleSupplier, null);
    }

    HealthBarRenderer(TankRenderer delegate, BooleanSupplier visibleSupplier, TextureRegion pixelRegion) {
        this.delegate = delegate;
        this.visibleSupplier = visibleSupplier;
        if (pixelRegion == null) {
            Texture texture = createPixelTexture();
            this.pixelRegion = new TextureRegion(texture);
            this.ownedTexture = texture;
        } else {
            this.pixelRegion = pixelRegion;
            this.ownedTexture = null;
        }
    }

    @Override
    public Rectangle render(Player player, Batch batch, TileMovement tileMovement) {
        Rectangle bounds = delegate.render(player, batch, tileMovement);
        if (!visibleSupplier.getAsBoolean()) {
            return bounds;
        }
        float barHeight = bounds.height * 0.1f;
        float y = bounds.y + bounds.height + barHeight * 0.3f;
        float width = bounds.width;
        float healthPercent = player.getHealthPercentage();

        Color previous = new Color(batch.getColor());
        batch.setColor(0f, 0f, 0f, previous.a);
        batch.draw(pixelRegion, bounds.x, y, width, barHeight);

        batch.setColor(0.95f, 0.15f, 0.15f, previous.a);
        batch.draw(pixelRegion, bounds.x, y, width * healthPercent, barHeight);

        batch.setColor(previous);
        return bounds;
    }

    @Override
    public void dispose() {
        if (ownedTexture != null) {
            ownedTexture.dispose();
        }
    }

    private static Texture createPixelTexture() {
        com.badlogic.gdx.graphics.Pixmap pixmap =
                new com.badlogic.gdx.graphics.Pixmap(1, 1, com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }
}



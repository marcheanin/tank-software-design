package ru.mipt.bit.platformer.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.mipt.bit.platformer.model.Player;
import ru.mipt.bit.platformer.util.TileMovement;
import com.badlogic.gdx.math.GridPoint2;

import java.util.function.BooleanSupplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class HealthBarRendererTest {

    @Test
    void render_ShouldDrawHealthBarWhenVisible() {
        TankRenderer delegate = mock(TankRenderer.class);
        Rectangle rect = new Rectangle(10f, 20f, 30f, 40f);
        when(delegate.render(any(), any(), any())).thenReturn(rect);
        TextureRegion pixelRegion = new TextureRegion(mock(Texture.class));
        BooleanSupplier visibleSupplier = () -> true;
        HealthBarRenderer renderer = new HealthBarRenderer(delegate, visibleSupplier, pixelRegion);

        Batch batch = mock(Batch.class);
        when(batch.getColor()).thenReturn(new Color(1f, 1f, 1f, 1f));
        Player player = new Player(new GridPoint2(0, 0), 100f, 0.4f);
        player.takeDamage(50f); // 50% health

        TileMovement movement = mock(TileMovement.class);
        renderer.render(player, batch, movement);

        verify(delegate).render(player, batch, movement);
        verify(batch, times(2)).draw(eq(pixelRegion), eq(rect.x), anyFloat(), anyFloat(), anyFloat());
    }

    @Test
    void render_ShouldNotDrawWhenHidden() {
        TankRenderer delegate = mock(TankRenderer.class);
        when(delegate.render(any(), any(), any())).thenReturn(new Rectangle());
        HealthBarRenderer renderer = new HealthBarRenderer(delegate, () -> false, new TextureRegion(mock(Texture.class)));

        Batch batch = mock(Batch.class);
        when(batch.getColor()).thenReturn(new Color(1f, 1f, 1f, 1f));
        Player player = new Player(new GridPoint2(0, 0), 100f, 0.4f);

        TileMovement movement = mock(TileMovement.class);
        renderer.render(player, batch, movement);

        verify(delegate).render(player, batch, movement);
        verify(batch, never()).draw(any(TextureRegion.class), anyFloat(), anyFloat(), anyFloat(), anyFloat());
    }
}



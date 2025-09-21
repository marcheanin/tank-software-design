package ru.mipt.bit.platformer.render;

import com.badlogic.gdx.maps.MapRenderer;

public class LevelRenderer implements Renderer{

    private final MapRenderer mapRenderer;

    public LevelRenderer(MapRenderer mapRenderer) {
        this.mapRenderer = mapRenderer;
    }

    @Override
    public void render() {
        mapRenderer.render();
    }
}

package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;

public class TileGrid {
    private final TiledMapTileLayer tileLayer;

    public TileGrid(TiledMapTileLayer tileLayer) {
        this.tileLayer = tileLayer;
    }

    public GridPoint2 getGridSize() {
        return new GridPoint2(tileLayer.getWidth(), tileLayer.getHeight());
    }

    public boolean isValidPosition(GridPoint2 position) {
        return position.x >= 0 && position.x < tileLayer.getWidth() &&
                position.y >= 0 && position.y < tileLayer.getHeight();
    }

    public TiledMapTileLayer getTileLayer() {
        return tileLayer;
    }
}

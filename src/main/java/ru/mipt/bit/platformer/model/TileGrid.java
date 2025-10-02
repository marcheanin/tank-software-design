package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

public class TileGrid {
    private final int width;
    private final int height;

    public TileGrid(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public GridPoint2 getGridSize() {
        return new GridPoint2(width, height);
    }

    public boolean isValidPosition(GridPoint2 position) {
        return position.x >= 0 && position.x < width &&
                position.y >= 0 && position.y < height;
    }
}
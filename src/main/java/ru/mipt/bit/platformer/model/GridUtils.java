package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

public final class GridUtils {
    private GridUtils() {}

    public static GridPoint2 move(GridPoint2 from, Direction direction) {
        return new GridPoint2(from).add(direction.dx(), direction.dy());
    }
}

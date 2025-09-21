package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

public final class GridUtils {
    private GridUtils() {}

    public static GridPoint2 move(GridPoint2 from, Direction direction) {
        return switch (direction) {
            case UP -> new GridPoint2(from).add(0, 1);
            case LEFT -> new GridPoint2(from).sub(1, 0);
            case DOWN -> new GridPoint2(from).sub(0, 1);
            case RIGHT -> new GridPoint2(from).add(1, 0);
        };
    }
}

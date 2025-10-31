package ru.mipt.bit.platformer.command;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.model.Player;
import ru.mipt.bit.platformer.model.World;

import java.util.HashSet;
import java.util.Set;

public class CommandContext {
    private final Set<GridPoint2> reservedCells = new HashSet<>();

    public CommandContext(World world) {
        for (Player tank : world.getAllTanks()) {
            if (tank.isMoving()) {
                reservedCells.add(tank.getCoordinates());
                reservedCells.add(tank.getDestinationCoordinates());
            } else {
                reservedCells.add(tank.getCoordinates());
            }
        }
    }

    public boolean isFree(GridPoint2 cell) {
        return !reservedCells.contains(cell);
    }

    public void reserve(GridPoint2 cell) {
        reservedCells.add(new GridPoint2(cell));
    }
}



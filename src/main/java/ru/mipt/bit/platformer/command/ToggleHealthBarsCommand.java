package ru.mipt.bit.platformer.command;

import ru.mipt.bit.platformer.model.World;

public class ToggleHealthBarsCommand implements Command {
    @Override
    public void execute(World world, CommandContext context) {
        world.toggleHealthBars();
    }
}



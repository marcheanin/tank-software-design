package ru.mipt.bit.platformer.command;

import ru.mipt.bit.platformer.model.World;

public interface Command {
    void execute(World world, CommandContext context);
}



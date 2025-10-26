package ru.mipt.bit.platformer.level;

import ru.mipt.bit.platformer.model.World;

public interface LevelLoader {
    
    World loadLevel(ru.mipt.bit.platformer.model.TileGrid tileGrid) throws LevelLoadingException;
}

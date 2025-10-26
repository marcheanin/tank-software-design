package ru.mipt.bit.platformer.app;

import ru.mipt.bit.platformer.level.LevelLoader;
import ru.mipt.bit.platformer.level.RandomLevelGenerator;
import ru.mipt.bit.platformer.level.FileLevelLoader;

public enum GameConfiguration {
    
    RANDOM_LEVEL {
        @Override
        public LevelLoader createLevelLoader() {
            return new RandomLevelGenerator();
        }
    },
    
    FILE_LEVEL {
        @Override
        public LevelLoader createLevelLoader() {
            return new FileLevelLoader("levels/level1.txt");
        }
    };
    
    public abstract LevelLoader createLevelLoader();
    
    public static GameConfiguration getDefault() {
        return FILE_LEVEL;
    }
}

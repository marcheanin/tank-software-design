package ru.mipt.bit.platformer.app;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.level.LevelLoader;
import ru.mipt.bit.platformer.level.RandomLevelGenerator;
import ru.mipt.bit.platformer.level.FileLevelLoader;

import static org.junit.jupiter.api.Assertions.*;

class GameConfigurationTest {

    @Test
    void getDefault_ShouldReturnFileLevel() {
        GameConfiguration defaultConfig = GameConfiguration.getDefault();

        assertEquals(GameConfiguration.FILE_LEVEL, defaultConfig);
    }

    @Test
    void createLevelLoader_RandomLevel_ShouldReturnRandomLevelGenerator() {
        LevelLoader loader = GameConfiguration.RANDOM_LEVEL.createLevelLoader();

        assertNotNull(loader);
        assertTrue(loader instanceof RandomLevelGenerator);
    }

    @Test
    void createLevelLoader_FileLevel_ShouldReturnFileLevelLoader() {
        LevelLoader loader = GameConfiguration.FILE_LEVEL.createLevelLoader();

        assertNotNull(loader);
        assertTrue(loader instanceof FileLevelLoader);
    }

    @Test
    void createLevelLoader_ShouldCreateNewInstanceEachTime() {
        LevelLoader loader1 = GameConfiguration.RANDOM_LEVEL.createLevelLoader();
        LevelLoader loader2 = GameConfiguration.RANDOM_LEVEL.createLevelLoader();

        assertNotSame(loader1, loader2);
    }
}

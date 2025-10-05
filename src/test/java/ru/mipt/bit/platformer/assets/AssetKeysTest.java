package ru.mipt.bit.platformer.assets;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AssetKeysTest {

    @Test
    void level_shouldHaveCorrectPath() {
        assertEquals("level.tmx", AssetKeys.LEVEL);
    }

    @Test
    void playerTank_shouldHaveCorrectPath() {
        assertEquals("images/tank_blue.png", AssetKeys.PLAYER_TANK);
    }

    @Test
    void treeObstacle_shouldHaveCorrectPath() {
        assertEquals("images/greenTree.png", AssetKeys.TREE_OBSTACLE);
    }

    @Test
    void allKeys_shouldBeNonNull() {
        assertNotNull(AssetKeys.LEVEL);
        assertNotNull(AssetKeys.PLAYER_TANK);
        assertNotNull(AssetKeys.TREE_OBSTACLE);
    }

    @Test
    void allKeys_shouldBeNonEmpty() {
        assertFalse(AssetKeys.LEVEL.isEmpty());
        assertFalse(AssetKeys.PLAYER_TANK.isEmpty());
        assertFalse(AssetKeys.TREE_OBSTACLE.isEmpty());
    }

    @Test
    void paths_shouldHaveCorrectExtensions() {
        assertTrue(AssetKeys.LEVEL.endsWith(".tmx"));
        assertTrue(AssetKeys.PLAYER_TANK.endsWith(".png"));
        assertTrue(AssetKeys.TREE_OBSTACLE.endsWith(".png"));
    }
}

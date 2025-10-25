package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileLevelLoaderTest {

    private TileGrid tileGrid;

    @BeforeEach
    void setUp() {
        tileGrid = new TileGrid(10, 8);
    }

    @Test
    void loadLevel_WithValidFile_ShouldCreateCorrectWorld() throws LevelLoadingException {
        FileLevelLoader loader = new FileLevelLoader("levels/level1.txt");

        World world = loader.loadLevel(tileGrid);

        assertNotNull(world);
        assertNotNull(world.getPlayer());
        
        GridPoint2 playerPosition = world.getPlayer().getCoordinates();
        assertTrue(tileGrid.isValidPosition(playerPosition));
        
        assertFalse(world.getObstacles().isEmpty());
        
        for (Obstacle obstacle : world.getObstacles()) {
            assertEquals(ObstacleType.TREE, obstacle.getType());
            assertFalse(obstacle.isPassable());
            assertTrue(tileGrid.isValidPosition(obstacle.getPosition()));
        }
    }

    @Test
    void loadLevel_WithNonExistentFile_ShouldThrowException() {
        FileLevelLoader loader = new FileLevelLoader("nonexistent/level.txt");

        assertThrows(LevelLoadingException.class, 
            () -> loader.loadLevel(tileGrid));
    }

    @Test
    void loadLevel_WithEmptyFile_ShouldThrowException() throws IOException {
        Path tempFile = Files.createTempFile("empty_level", ".txt");
        Files.write(tempFile, "".getBytes());
        
        FileLevelLoader loader = new FileLevelLoader(tempFile.toString());

        assertThrows(LevelLoadingException.class, 
            () -> loader.loadLevel(tileGrid));
        
        Files.deleteIfExists(tempFile);
    }

    @Test
    void loadLevel_WithInvalidCharacter_ShouldThrowException() throws IOException {
        Path tempFile = Files.createTempFile("invalid_level", ".txt");
        Files.write(tempFile, "T_X\nT_A\n___".getBytes());
        
        FileLevelLoader loader = new FileLevelLoader(tempFile.toString());
        TileGrid smallGrid = new TileGrid(3, 3);

        assertThrows(LevelLoadingException.class, 
            () -> loader.loadLevel(smallGrid));
        
        Files.deleteIfExists(tempFile);
    }

    @Test
    void loadLevel_WithMultiplePlayerPositions_ShouldThrowException() throws IOException {
        Path tempFile = Files.createTempFile("multiple_players", ".txt");
        Files.write(tempFile, "X_T\nT_X\n___".getBytes());
        
        FileLevelLoader loader = new FileLevelLoader(tempFile.toString());
        TileGrid smallGrid = new TileGrid(3, 3);

        assertThrows(LevelLoadingException.class, 
            () -> loader.loadLevel(smallGrid));
        
        Files.deleteIfExists(tempFile);
    }

    @Test
    void loadLevel_WithoutPlayerPosition_ShouldThrowException() throws IOException {
        Path tempFile = Files.createTempFile("no_player", ".txt");
        Files.write(tempFile, "T_T\nT_T\n___".getBytes());
        
        FileLevelLoader loader = new FileLevelLoader(tempFile.toString());
        TileGrid smallGrid = new TileGrid(3, 3);

        assertThrows(LevelLoadingException.class, 
            () -> loader.loadLevel(smallGrid));
        
        Files.deleteIfExists(tempFile);
    }

    @Test
    void loadLevel_WithWrongDimensions_ShouldThrowException() throws IOException {
        Path tempFile = Files.createTempFile("wrong_dimensions", ".txt");
        Files.write(tempFile, "T_X\nT_T".getBytes());
        
        FileLevelLoader loader = new FileLevelLoader(tempFile.toString());
        TileGrid smallGrid = new TileGrid(3, 3);

        assertThrows(LevelLoadingException.class, 
            () -> loader.loadLevel(smallGrid));
        
        Files.deleteIfExists(tempFile);
    }

    @Test
    void loadLevel_WithWrongLineLength_ShouldThrowException() throws IOException {
        Path tempFile = Files.createTempFile("wrong_line_length", ".txt");
        Files.write(tempFile, "T_X\nT_T_T\n___".getBytes());
        
        FileLevelLoader loader = new FileLevelLoader(tempFile.toString());
        TileGrid smallGrid = new TileGrid(3, 3);

        assertThrows(LevelLoadingException.class, 
            () -> loader.loadLevel(smallGrid));
        
        Files.deleteIfExists(tempFile);
    }

    @Test
    void loadLevel_WithCustomPlayerSettings_ShouldUseCorrectValues() throws IOException, LevelLoadingException {
        Path tempFile = Files.createTempFile("custom_player", ".txt");
        Files.write(tempFile, "T_X\nT_T\n___".getBytes());
        
        FileLevelLoader loader = new FileLevelLoader(tempFile.toString(), 200f, 0.6f);
        TileGrid smallGrid = new TileGrid(3, 3);

        World world = loader.loadLevel(smallGrid);

        Player player = world.getPlayer();
        assertEquals(200f, player.getMaxHealth());
        assertEquals(0.6f, player.getMovementSpeed());
        
        Files.deleteIfExists(tempFile);
    }

    @Test
    void loadLevel_WithOnlyPlayer_ShouldCreateWorldWithOnlyPlayer() throws IOException, LevelLoadingException {
        Path tempFile = Files.createTempFile("only_player", ".txt");
        Files.write(tempFile, "___\n_X_\n___".getBytes());
        
        FileLevelLoader loader = new FileLevelLoader(tempFile.toString());
        TileGrid smallGrid = new TileGrid(3, 3);

        World world = loader.loadLevel(smallGrid);

        assertNotNull(world.getPlayer());
        assertTrue(world.getObstacles().isEmpty());
        assertEquals(new GridPoint2(1, 1), world.getPlayer().getCoordinates());
        
        Files.deleteIfExists(tempFile);
    }

    @Test
    void loadLevel_WithOnlyObstacles_ShouldCreateWorldWithOnlyObstacles() throws IOException {
        Path tempFile = Files.createTempFile("only_obstacles", ".txt");
        Files.write(tempFile, "T_T\nT_T\n___".getBytes());
        
        FileLevelLoader loader = new FileLevelLoader(tempFile.toString());
        TileGrid smallGrid = new TileGrid(3, 3);

        assertThrows(LevelLoadingException.class, 
            () -> loader.loadLevel(smallGrid));
        
        Files.deleteIfExists(tempFile);
    }
}

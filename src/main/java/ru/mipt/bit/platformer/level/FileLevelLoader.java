package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FileLevelLoader implements LevelLoader {
    
    private final String filePath;
    private final float playerMaxHealth;
    private final float playerMovementSpeed;
    
    public FileLevelLoader(String filePath) {
        this(filePath, 100f, 0.4f);
    }
    
    public FileLevelLoader(String filePath, float playerMaxHealth, float playerMovementSpeed) {
        this.filePath = filePath;
        this.playerMaxHealth = playerMaxHealth;
        this.playerMovementSpeed = playerMovementSpeed;
    }
    
    @Override
    public World loadLevel(TileGrid tileGrid) throws LevelLoadingException {
        try {
            List<String> lines = readLevelFile();
            LevelData levelData = parseLevelData(lines, tileGrid);
            
            // Создаем игрока
            Player player = new Player(levelData.playerPosition, playerMaxHealth, playerMovementSpeed);
            
            // Создаем и возвращаем мир
            return new World(player, levelData.obstacles, tileGrid);
            
        } catch (IOException e) {
            throw new LevelLoadingException("Failed to read level file: " + filePath, e);
        } catch (Exception e) {
            throw new LevelLoadingException("Failed to parse level file: " + filePath, e);
        }
    }
    
    private List<String> readLevelFile() throws IOException {
        List<String> lines = new ArrayList<>();
        
        if (filePath.startsWith("/") || filePath.contains(":")) {
            try (BufferedReader reader = Files.newBufferedReader(java.nio.file.Paths.get(filePath), StandardCharsets.UTF_8)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line.trim());
                }
            }
        } else {
            try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                
                if (inputStream == null) {
                    throw new IOException("Level file not found: " + filePath);
                }
                
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line.trim());
                }
            }
        }
        
        if (lines.isEmpty()) {
            throw new IOException("Level file is empty: " + filePath);
        }
        
        return lines;
    }
    
    private LevelData parseLevelData(List<String> lines, TileGrid tileGrid) throws LevelLoadingException {
        GridPoint2 gridSize = tileGrid.getGridSize();
        int expectedHeight = gridSize.y;
        int expectedWidth = gridSize.x;
        
        if (lines.size() != expectedHeight) {
            throw new LevelLoadingException(
                String.format("Level height mismatch: expected %d, got %d", expectedHeight, lines.size())
            );
        }
        
        Set<Obstacle> obstacles = new HashSet<>();
        GridPoint2 playerPosition = null;
        
        for (int y = 0; y < lines.size(); y++) {
            String line = lines.get(y);
            
            if (line.length() != expectedWidth) {
                throw new LevelLoadingException(
                    String.format("Level width mismatch at line %d: expected %d, got %d", 
                                y + 1, expectedWidth, line.length())
                );
            }
            
            for (int x = 0; x < line.length(); x++) {
                char symbol = line.charAt(x);
                GridPoint2 position = new GridPoint2(x, y);
                
                switch (symbol) {
                    case 'T':
                        obstacles.add(new Obstacle(position, ObstacleType.TREE, false));
                        break;
                    case 'X':
                        if (playerPosition != null) {
                            throw new LevelLoadingException(
                                String.format("Multiple player positions found: (%d,%d) and (%d,%d)", 
                                            playerPosition.x, playerPosition.y, x, y)
                            );
                        }
                        playerPosition = position;
                        break;
                    case '_':
                        break;
                    default:
                        throw new LevelLoadingException(
                            String.format("Invalid character '%c' at position (%d,%d). " +
                                        "Valid characters: T (tree), X (player), _ (empty)", 
                                        symbol, x, y)
                        );
                }
            }
        }
        
        if (playerPosition == null) {
            throw new LevelLoadingException("Player position (X) not found in level file");
        }
        
        return new LevelData(obstacles, playerPosition);
    }
    
    private static class LevelData {
        final Set<Obstacle> obstacles;
        final GridPoint2 playerPosition;
        
        LevelData(Set<Obstacle> obstacles, GridPoint2 playerPosition) {
            this.obstacles = obstacles;
            this.playerPosition = playerPosition;
        }
    }
}

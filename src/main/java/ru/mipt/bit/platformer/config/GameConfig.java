package ru.mipt.bit.platformer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.mipt.bit.platformer.assets.GdxAssetManager;
import ru.mipt.bit.platformer.assets.IAssetManager;
import ru.mipt.bit.platformer.collision.CollisionDetector;
import ru.mipt.bit.platformer.collision.TileCollisionDetector;
import ru.mipt.bit.platformer.input.GdxKeyboardInputController;
import ru.mipt.bit.platformer.input.InputController;
import ru.mipt.bit.platformer.level.FileLevelLoader;
import ru.mipt.bit.platformer.level.LevelLoader;
import ru.mipt.bit.platformer.logic.GameLogic;

@Configuration
@ComponentScan(basePackages = "ru.mipt.bit.platformer")
public class GameConfig {

    @Bean
    public IAssetManager assetManager() {
        return new GdxAssetManager();
    }

    @Bean
    public CollisionDetector collisionDetector() {
        return new TileCollisionDetector();
    }

    @Bean
    public GameLogic gameLogic(CollisionDetector collisionDetector) {
        return new GameLogic(collisionDetector);
    }

    @Bean
    public InputController inputController() {
        return new GdxKeyboardInputController();
    }

    @Bean
    public LevelLoader levelLoader() {
        return new FileLevelLoader("levels/level1.txt");
    }
}


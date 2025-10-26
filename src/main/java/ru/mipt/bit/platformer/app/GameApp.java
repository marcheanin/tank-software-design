package ru.mipt.bit.platformer.app;

import com.badlogic.gdx.Game;
import ru.mipt.bit.platformer.assets.GdxAssetManager;
import ru.mipt.bit.platformer.assets.IAssetManager;
import ru.mipt.bit.platformer.collision.TileCollisionDetector;
import ru.mipt.bit.platformer.input.GdxKeyboardInputController;
import ru.mipt.bit.platformer.input.InputController;
import ru.mipt.bit.platformer.level.LevelLoader;
import ru.mipt.bit.platformer.logic.GameLogic;
import ru.mipt.bit.platformer.screen.GameScreen;

public class GameApp extends Game {

    private final GameConfiguration configuration;

    public GameApp() {
        this(GameConfiguration.getDefault());
    }

    public GameApp(GameConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void create() {
        // Создание зависимостей
        IAssetManager assetManager = new GdxAssetManager();
        GameLogic gameLogic = new GameLogic(new TileCollisionDetector());
        InputController input = new GdxKeyboardInputController();
        LevelLoader levelLoader = configuration.createLevelLoader();
        
        // Внедрение зависимостей в GameScreen
        setScreen(new GameScreen(assetManager, gameLogic, input, levelLoader));
    }
}
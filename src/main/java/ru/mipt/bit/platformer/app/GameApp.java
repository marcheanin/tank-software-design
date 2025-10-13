package ru.mipt.bit.platformer.app;

import com.badlogic.gdx.Game;
import ru.mipt.bit.platformer.assets.GdxAssetManager;
import ru.mipt.bit.platformer.assets.IAssetManager;
import ru.mipt.bit.platformer.collision.TileCollisionDetector;
import ru.mipt.bit.platformer.input.GdxKeyboardInputController;
import ru.mipt.bit.platformer.input.InputController;
import ru.mipt.bit.platformer.logic.GameLogic;
import ru.mipt.bit.platformer.screen.GameScreen;

public class GameApp extends Game {

    @Override
    public void create() {
        // Создание зависимостей
        IAssetManager assetManager = new GdxAssetManager();
        GameLogic gameLogic = new GameLogic(new TileCollisionDetector());
        InputController input = new GdxKeyboardInputController();
        
        // Внедрение зависимостей в GameScreen
        setScreen(new GameScreen(assetManager, gameLogic, input));
    }
}
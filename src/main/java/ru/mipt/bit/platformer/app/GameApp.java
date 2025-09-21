package ru.mipt.bit.platformer.app;

import com.badlogic.gdx.Game;
import ru.mipt.bit.platformer.screen.GameScreen;

public class GameApp extends Game {

    @Override
    public void create() {
        setScreen(new GameScreen());
    }
}
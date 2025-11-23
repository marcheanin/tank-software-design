package ru.mipt.bit.platformer.app;

import com.badlogic.gdx.Game;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.mipt.bit.platformer.config.GameConfig;
import ru.mipt.bit.platformer.screen.GameScreen;

public class GameApp extends Game {

    private ApplicationContext applicationContext;

    @Override
    public void create() {
        applicationContext = new AnnotationConfigApplicationContext(GameConfig.class);
        GameScreen gameScreen = applicationContext.getBean(GameScreen.class);
        setScreen(gameScreen);
    }
}
package ru.mipt.bit.platformer.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import ru.mipt.bit.platformer.assets.IAssetManager;
import ru.mipt.bit.platformer.util.TileMovement;

import ru.mipt.bit.platformer.input.InputController;
import ru.mipt.bit.platformer.model.*;
import ru.mipt.bit.platformer.logic.GameLogic;
import ru.mipt.bit.platformer.render.*;
import ru.mipt.bit.platformer.assets.AssetKeys;
import java.util.Set;
import java.util.HashSet;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public class GameScreen implements Screen {

    private Batch batch;

    // Игровые ресурсы
    private TiledMap level;
    private Texture blueTankTexture;
    private Texture greenTreeTexture;

    // Игровая логика
    private final InputController input;
    private World world;
    private final GameLogic gameLogic;

    // Рендеринг
    private LevelRenderer levelRenderer;
    private EntityRenderer entityRenderer;

    // Управление ресурсами
    private final IAssetManager assetManager;

    public GameScreen(IAssetManager assetManager, GameLogic gameLogic, InputController input) {
        this.assetManager = assetManager;
        this.gameLogic = gameLogic;
        this.input = input;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();

        // Загрузка ресурсов
        assetManager.loadAssets();

        // Получение ресурсов
        level = assetManager.getTiledMap(AssetKeys.LEVEL);
        blueTankTexture = assetManager.getTexture(AssetKeys.PLAYER_TANK);
        greenTreeTexture = assetManager.getTexture(AssetKeys.TREE_OBSTACLE);

        // Создание игрового мира
        TiledMapTileLayer groundLayer = getSingleLayer(level);
        TileMovement tileMovement = new TileMovement(groundLayer, Interpolation.smooth);
        TileGrid tileGrid = new TileGrid(groundLayer.getWidth(), groundLayer.getHeight());

        Set<Obstacle> obstacles = new HashSet<>();
        obstacles.add(new Obstacle(new GridPoint2(1, 3), ObstacleType.TREE, false)); // дерево непроходимо

        Player player = new Player(new GridPoint2(1, 1), 100f, 0.4f);
        world = new World(player, obstacles, tileGrid);

        // Создание рендереров
        levelRenderer = new LevelRenderer(createSingleLayerMapRenderer(level, batch));

        PlayerRenderer playerRenderer = new PlayerRenderer(new TextureRegion(blueTankTexture));
        ObstacleRenderer obstacleRenderer = new ObstacleRenderer(new TextureRegion(greenTreeTexture), groundLayer);
        entityRenderer = new EntityRenderer(tileMovement, playerRenderer, obstacleRenderer);
    }

    @Override
    public void render(float delta) {
        // Очистка экрана
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        // Обработка ввода и игровой логики
        for (var event : input.poll()) {
            switch(event.getAction()) {
                case MOVE -> event.getDirection().ifPresent(dir -> gameLogic.processMoveCommand(world, dir));
                case SHOOT -> gameLogic.processShootCommand(world);
            }
        }
        gameLogic.updateWorld(world, delta);

        // Рендеринг
        levelRenderer.render();
        entityRenderer.render(world, batch);
    }

    @Override
    public void resize(int width, int height) {
        // do not react to window resizing
    }

    @Override
    public void pause() {
        // not used
    }

    @Override
    public void resume() {
        // not used
    }

    @Override
    public void hide() {
        // not used
    }

    @Override
    public void dispose() {
        assetManager.dispose();
        batch.dispose();
    }
}
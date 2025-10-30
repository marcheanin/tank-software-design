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
import ru.mipt.bit.platformer.level.LevelLoader;
import ru.mipt.bit.platformer.level.LevelLoadingException;
import ru.mipt.bit.platformer.model.*;
import ru.mipt.bit.platformer.logic.GameLogic;
import ru.mipt.bit.platformer.render.*;
import ru.mipt.bit.platformer.assets.AssetKeys;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public class GameScreen implements Screen {

    private Batch batch;

    private TiledMap level;
    private Texture blueTankTexture;
    private Texture greenTreeTexture;

    private final InputController input;
    private World world;
    private final GameLogic gameLogic;
    private final LevelLoader levelLoader;

    // Рендеринг
    private LevelRenderer levelRenderer;
    private EntityRenderer entityRenderer;

    // Управление ресурсами
    private final IAssetManager assetManager;
    private final Random random = new Random();

    public GameScreen(IAssetManager assetManager, GameLogic gameLogic, InputController input, LevelLoader levelLoader) {
        this.assetManager = assetManager;
        this.gameLogic = gameLogic;
        this.input = input;
        this.levelLoader = levelLoader;
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

        try {
            world = levelLoader.loadLevel(tileGrid);
        } catch (LevelLoadingException e) {
            throw new RuntimeException("Failed to load level", e);
        }

        int botsToCreate = 3;
        float botMaxHealth = world.getPlayer().getMaxHealth();
        float botSpeed = world.getPlayer().getMovementSpeed();
        for (int i = 0; i < botsToCreate; i++) {
            GridPoint2 pos = findFreeCell(tileGrid);
            if (pos == null) break;
            world.getAiTanks().add(new Player(pos, botMaxHealth, botSpeed));
        }

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

        List<ru.mipt.bit.platformer.command.Command> commands = new ArrayList<>();
        for (var event : input.poll()) {
            switch(event.getAction()) {
                case MOVE -> event.getDirection().ifPresent(dir ->
                        commands.add(new ru.mipt.bit.platformer.command.MoveCommand(world.getPlayer(), dir))
                );
                case SHOOT -> gameLogic.processShootCommand(world);
            }
        }

        for (Player bot : world.getAiTanks()) {
            if (!bot.isMoving()) {
                Direction[] dirs = Direction.values();
                Direction dir = dirs[random.nextInt(dirs.length)];
                commands.add(new ru.mipt.bit.platformer.command.MoveCommand(bot, dir));
            }
        }

        gameLogic.processCommands(world, commands);
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

    private GridPoint2 findFreeCell(TileGrid grid) {
        GridPoint2 size = grid.getGridSize();
        for (int attempts = 0; attempts < size.x * size.y * 2; attempts++) {
            int x = random.nextInt(size.x);
            int y = random.nextInt(size.y);
            GridPoint2 p = new GridPoint2(x, y);
            if (!grid.isValidPosition(p)) continue;
            if (world.hasObstacleAt(p)) continue;
            if (world.getPlayer().getCoordinates().equals(p)) continue;
            boolean occupied = false;
            for (Player bot : world.getAiTanks()) {
                if (bot.getCoordinates().equals(p)) { occupied = true; break; }
            }
            if (!occupied) return p;
        }
        return null;
    }
}
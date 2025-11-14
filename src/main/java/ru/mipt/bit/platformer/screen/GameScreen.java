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
    private Texture redTankTexture;
    private Texture greenTreeTexture;

    private final InputController input;
    private World world;
    private final GameLogic gameLogic;
    private final LevelLoader levelLoader;

    // Рендеринг
    private LevelRenderer levelRenderer;
    private EntityRenderer entityRenderer;
    private HealthBarRenderer playerHealthRenderer;
    private HealthBarRenderer botHealthRenderer;

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

        loadAssets();

        TiledMapTileLayer groundLayer = getSingleLayer(level);
        TileMovement tileMovement = new TileMovement(groundLayer, Interpolation.smooth);
        TileGrid tileGrid = new TileGrid(groundLayer.getWidth(), groundLayer.getHeight());

        initWorld(tileGrid);
        spawnBots(tileGrid);
        buildRenderers(groundLayer, tileMovement);
    }

    @Override
    public void render(float delta) {
        // Очистка экрана
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        List<ru.mipt.bit.platformer.command.Command> commands = collectCommands();
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
        if (playerHealthRenderer != null) {
            playerHealthRenderer.dispose();
        }
        if (botHealthRenderer != null) {
            botHealthRenderer.dispose();
        }
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

    private void loadAssets() {
        assetManager.loadAssets();
        level = assetManager.getTiledMap(AssetKeys.LEVEL);
        blueTankTexture = assetManager.getTexture(AssetKeys.PLAYER_TANK);
        redTankTexture = assetManager.getTexture(AssetKeys.PLAYER_TANK_RED);
        greenTreeTexture = assetManager.getTexture(AssetKeys.TREE_OBSTACLE);
    }

    private void initWorld(TileGrid tileGrid) {
        try {
            world = levelLoader.loadLevel(tileGrid);
        } catch (LevelLoadingException e) {
            throw new RuntimeException("Failed to load level", e);
        }
    }

    private void spawnBots(TileGrid tileGrid) {
        int botsToCreate = 3;
        float botMaxHealth = world.getPlayer().getMaxHealth();
        float botSpeed = world.getPlayer().getMovementSpeed();
        for (int i = 0; i < botsToCreate; i++) {
            GridPoint2 pos = findFreeCell(tileGrid);
            if (pos == null) break;
            world.getAiTanks().add(new Player(pos, botMaxHealth, botSpeed));
        }
    }

    private void buildRenderers(TiledMapTileLayer groundLayer, TileMovement tileMovement) {
        levelRenderer = new LevelRenderer(createSingleLayerMapRenderer(level, batch));
        PlayerRenderer basePlayerRenderer = new PlayerRenderer(new TextureRegion(redTankTexture));
        PlayerRenderer baseBotRenderer = new PlayerRenderer(new TextureRegion(blueTankTexture));
        playerHealthRenderer = new HealthBarRenderer(basePlayerRenderer, world::isHealthBarsVisible);
        botHealthRenderer = new HealthBarRenderer(baseBotRenderer, world::isHealthBarsVisible);
        ObstacleRenderer obstacleRenderer = new ObstacleRenderer(new TextureRegion(greenTreeTexture), groundLayer);
        BulletRenderer bulletRenderer = new BulletRenderer(32f);
        entityRenderer = new EntityRenderer(tileMovement, playerHealthRenderer, botHealthRenderer, obstacleRenderer, bulletRenderer);
    }

    private List<ru.mipt.bit.platformer.command.Command> collectCommands() {
        List<ru.mipt.bit.platformer.command.Command> commands = new ArrayList<>();
        for (var event : input.poll()) {
            switch(event.getAction()) {
                case MOVE -> event.getDirection().ifPresent(dir ->
                        commands.add(new ru.mipt.bit.platformer.command.MoveCommand(world.getPlayer(), dir))
                );
                case SHOOT -> commands.add(new ru.mipt.bit.platformer.command.ShootCommand(
                        world.getPlayer(), gameLogic.getBulletSpeed(), gameLogic.getBulletDamage()));
                case TOGGLE_HEALTH -> commands.add(new ru.mipt.bit.platformer.command.ToggleHealthBarsCommand());
            }
        }

        for (Player bot : world.getAiTanks()) {
            if (!bot.isMoving() && bot.isAlive()) {
                if (random.nextFloat() < 0.3f) {
                    commands.add(new ru.mipt.bit.platformer.command.ShootCommand(
                            bot, gameLogic.getBulletSpeed(), gameLogic.getBulletDamage()));
                } else {
                    Direction[] dirs = Direction.values();
                    Direction dir = dirs[random.nextInt(dirs.length)];
                    commands.add(new ru.mipt.bit.platformer.command.MoveCommand(bot, dir));
                }
            }
        }
        return commands;
    }
}
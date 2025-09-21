package ru.mipt.bit.platformer.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class GdxAssetManager implements IAssetManager {

    private final AssetManager gdxAssetManager;

    public GdxAssetManager() {
        this.gdxAssetManager = new AssetManager();
        gdxAssetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
    }

    @Override
    public void loadAssets() {
        // Загружаем текстуры
        gdxAssetManager.load(AssetKeys.PLAYER_TANK, Texture.class);
        gdxAssetManager.load(AssetKeys.TREE_OBSTACLE, Texture.class);

        // Загружаем карту
        gdxAssetManager.load(AssetKeys.LEVEL, TiledMap.class);

        // Ждем завершения загрузки
        gdxAssetManager.finishLoading();
    }

    @Override
    public Texture getTexture(String key) {
        return gdxAssetManager.get(key, Texture.class);
    }

    @Override
    public TiledMap getTiledMap(String key) {
        return gdxAssetManager.get(key, TiledMap.class);
    }

    @Override
    public void dispose() {
        gdxAssetManager.dispose();
    }
}
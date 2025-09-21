package ru.mipt.bit.platformer.assets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;

public interface IAssetManager {

    /**
     * Загружает все ресурсы игры
     */
    void loadAssets();

    /**
     * Возвращает текстуру по ключу
     */
    Texture getTexture(String key);

    /**
     * Возвращает карту по ключу
     */
    TiledMap getTiledMap(String key);

    /**
     * Освобождает все ресурсы
     */
    void dispose();
}
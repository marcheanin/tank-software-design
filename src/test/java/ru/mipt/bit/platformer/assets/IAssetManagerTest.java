package ru.mipt.bit.platformer.assets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IAssetManagerTest {

    @Mock
    private IAssetManager assetManager;

    @Mock
    private TiledMap tiledMap;

    @Mock
    private Texture texture;

    @Test
    void interface_shouldBeImplementable() {
        // Проверяем, что интерфейс может быть реализован через Mockito
        assertNotNull(assetManager);
    }

    @Test
    void loadAssets_shouldBeCallable() {
        doNothing().when(assetManager).loadAssets();
        
        assertDoesNotThrow(() -> assetManager.loadAssets());
        verify(assetManager).loadAssets();
    }

    @Test
    void getTiledMap_shouldAcceptStringParameter() {
        when(assetManager.getTiledMap(anyString())).thenReturn(tiledMap);
        
        TiledMap result = assetManager.getTiledMap("test");
        
        assertNotNull(result);
        verify(assetManager).getTiledMap("test");
    }

    @Test
    void getTiledMap_shouldReturnTiledMap() {
        when(assetManager.getTiledMap("level.tmx")).thenReturn(tiledMap);
        
        TiledMap result = assetManager.getTiledMap("level.tmx");
        
        assertEquals(tiledMap, result);
        verify(assetManager).getTiledMap("level.tmx");
    }

    @Test
    void getTexture_shouldAcceptStringParameter() {
        when(assetManager.getTexture(anyString())).thenReturn(texture);
        
        Texture result = assetManager.getTexture("test");
        
        assertNotNull(result);
        verify(assetManager).getTexture("test");
    }

    @Test
    void getTexture_shouldReturnTexture() {
        when(assetManager.getTexture("images/tank_blue.png")).thenReturn(texture);
        
        Texture result = assetManager.getTexture("images/tank_blue.png");
        
        assertEquals(texture, result);
        verify(assetManager).getTexture("images/tank_blue.png");
    }

    @Test
    void dispose_shouldBeCallable() {
        doNothing().when(assetManager).dispose();
        
        assertDoesNotThrow(() -> assetManager.dispose());
        verify(assetManager).dispose();
    }

    @Test
    void interface_shouldHaveCorrectMethodSignatures() {
        // Проверяем, что методы интерфейса имеют правильные сигнатуры
        var methods = IAssetManager.class.getDeclaredMethods();
        
        // Должно быть 4 метода
        assertEquals(4, methods.length);
        
        // Проверяем наличие методов
        boolean hasLoadAssets = false;
        boolean hasGetTiledMap = false;
        boolean hasGetTexture = false;
        boolean hasDispose = false;
        
        for (var method : methods) {
            switch (method.getName()) {
                case "loadAssets" -> hasLoadAssets = true;
                case "getTiledMap" -> hasGetTiledMap = true;
                case "getTexture" -> hasGetTexture = true;
                case "dispose" -> hasDispose = true;
            }
        }
        
        assertTrue(hasLoadAssets);
        assertTrue(hasGetTiledMap);
        assertTrue(hasGetTexture);
        assertTrue(hasDispose);
    }

    @Test
    void methods_shouldBeCalledCorrectly() {
        when(assetManager.getTiledMap("level")).thenReturn(tiledMap);
        when(assetManager.getTexture("tank")).thenReturn(texture);
        doNothing().when(assetManager).loadAssets();
        doNothing().when(assetManager).dispose();
        
        // Вызываем все методы
        assetManager.loadAssets();
        assetManager.getTiledMap("level");
        assetManager.getTexture("tank");
        assetManager.dispose();
        
        // Проверяем, что все методы были вызваны
        verify(assetManager).loadAssets();
        verify(assetManager).getTiledMap("level");
        verify(assetManager).getTexture("tank");
        verify(assetManager).dispose();
        
        verify(assetManager, times(1)).loadAssets();
        verify(assetManager, times(1)).getTiledMap(anyString());
        verify(assetManager, times(1)).getTexture(anyString());
        verify(assetManager, times(1)).dispose();
    }
}

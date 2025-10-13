package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private Player player;
    private GridPoint2 startPosition;

    @BeforeEach
    void setUp() {
        startPosition = new GridPoint2(2, 3);
        player = new Player(startPosition, 100f, 0.4f);
    }

    @Test
    void constructor_shouldSetInitialState() {
        assertEquals(startPosition, player.getCoordinates());
        assertEquals(startPosition, player.getDestinationCoordinates());
        assertEquals(1.0f, player.getMovementProgress(), 0.001f);
        assertEquals(0.0f, player.getRotation(), 0.001f);
        assertFalse(player.isMoving());
        assertTrue(player.isAlive());
        assertEquals(100.0f, player.getMaxHealth(), 0.001f);
        assertEquals(100.0f, player.getCurrentHealth(), 0.001f);
        assertEquals(1.0f, player.getHealthPercentage(), 0.001f);
    }

    @Test
    void setDestination_shouldResetProgressAndSetTarget() {
        GridPoint2 destination = new GridPoint2(4, 5);
        
        player.setDestination(destination);
        
        assertEquals(destination, player.getDestinationCoordinates());
        assertEquals(0.0f, player.getMovementProgress(), 0.001f);
        assertTrue(player.isMoving());
    }

    @Test
    void setRotation_shouldUpdateRotation() {
        float newRotation = 45.5f;
        
        player.setRotation(newRotation);
        
        assertEquals(newRotation, player.getRotation(), 0.001f);
    }

    @Test
    void updateProgress_shouldNotChangeProgressWhenNotMoving() {
        float initialProgress = player.getMovementProgress();
        
        player.updateProgress(0.5f);
        
        assertEquals(initialProgress, player.getMovementProgress(), 0.001f);
        assertEquals(startPosition, player.getCoordinates());
    }

    @Test
    void updateProgress_shouldIncreaseProgressWhenMoving() {
        GridPoint2 destination = new GridPoint2(3, 3);
        player.setDestination(destination);
        float initialProgress = player.getMovementProgress();
        
        player.updateProgress(0.1f);
        
        assertTrue(player.getMovementProgress() > initialProgress);
        assertTrue(player.getMovementProgress() < 1.0f);
        assertEquals(startPosition, player.getCoordinates()); // ещё не достиг цели
    }

    @Test
    void updateProgress_shouldCompleteMovementWhenProgressReachesOne() {
        GridPoint2 destination = new GridPoint2(3, 3);
        player.setDestination(destination);
        
        // Симулируем достаточное время для завершения движения
        player.updateProgress(1.0f);
        
        assertEquals(1.0f, player.getMovementProgress(), 0.001f);
        assertEquals(destination, player.getCoordinates());
        assertFalse(player.isMoving());
    }

    @Test
    void updateProgress_shouldNotExceedOne() {
        GridPoint2 destination = new GridPoint2(3, 3);
        player.setDestination(destination);
        
        // Обновляем больше, чем нужно для завершения
        player.updateProgress(2.0f);
        
        assertEquals(1.0f, player.getMovementProgress(), 0.001f);
        assertEquals(destination, player.getCoordinates());
        assertFalse(player.isMoving());
    }

    @Test
    void isMoving_shouldReturnTrueWhenProgressLessThanOne() {
        GridPoint2 destination = new GridPoint2(3, 3);
        player.setDestination(destination);
        
        assertTrue(player.isMoving());
        
        // Обновляем прогресс, но не завершаю движение
        // movementSpeed = 0.4f, так что deltaTime = 0.1f добавит 0.1/0.4 = 0.25 к прогрессу
        player.updateProgress(0.1f);
        assertTrue(player.isMoving());
        
        // Ещё один шаг, но всё ещё не завершаю
        player.updateProgress(0.1f); // добавит ещё 0.25, итого 0.5
        assertTrue(player.isMoving());
    }

    @Test
    void isMoving_shouldReturnFalseWhenProgressEqualsOne() {
        GridPoint2 destination = new GridPoint2(3, 3);
        player.setDestination(destination);
        
        // Завершаем движение
        player.updateProgress(2.0f);
        
        assertFalse(player.isMoving());
    }

    @Test
    void takeDamage_shouldReduceHealth() {
        float damage = 30.0f;
        float expectedHealth = player.getCurrentHealth() - damage;
        
        player.takeDamage(damage);
        
        assertEquals(expectedHealth, player.getCurrentHealth(), 0.001f);
        assertTrue(player.isAlive());
        assertEquals(expectedHealth / player.getMaxHealth(), player.getHealthPercentage(), 0.001f);
    }

    @Test
    void takeDamage_shouldNotGoBelowZero() {
        float excessiveDamage = 150.0f;
        
        player.takeDamage(excessiveDamage);
        
        assertEquals(0.0f, player.getCurrentHealth(), 0.001f);
        assertFalse(player.isAlive());
        assertEquals(0.0f, player.getHealthPercentage(), 0.001f);
    }

    @Test
    void heal_shouldIncreaseHealth() {
        player.takeDamage(50.0f); // health = 50
        float healAmount = 20.0f;
        float expectedHealth = 50.0f + healAmount;
        
        player.heal(healAmount);
        
        assertEquals(expectedHealth, player.getCurrentHealth(), 0.001f);
        assertTrue(player.isAlive());
        assertEquals(expectedHealth / player.getMaxHealth(), player.getHealthPercentage(), 0.001f);
    }

    @Test
    void heal_shouldNotExceedMaxHealth() {
        player.takeDamage(30.0f); // health = 70
        float excessiveHeal = 50.0f;
        
        player.heal(excessiveHeal);
        
        assertEquals(player.getMaxHealth(), player.getCurrentHealth(), 0.001f);
        assertTrue(player.isAlive());
        assertEquals(1.0f, player.getHealthPercentage(), 0.001f);
    }

    @Test
    void isAlive_shouldReturnFalseWhenHealthIsZero() {
        player.takeDamage(player.getMaxHealth());
        
        assertFalse(player.isAlive());
        assertEquals(0.0f, player.getCurrentHealth(), 0.001f);
    }

    @Test
    void getCoordinates_shouldReturnCopy() {
        GridPoint2 coordinates = player.getCoordinates();
        coordinates.x = 999; // мутируем копию
        
        assertEquals(startPosition, player.getCoordinates()); // оригинал не изменился
    }

    @Test
    void getDestinationCoordinates_shouldReturnCopy() {
        GridPoint2 destination = new GridPoint2(5, 6);
        player.setDestination(destination);
        
        GridPoint2 returnedDestination = player.getDestinationCoordinates();
        returnedDestination.x = 999; // мутируем копию
        
        assertEquals(destination, player.getDestinationCoordinates()); // оригинал не изменился
    }

    @Test
    void getMovementSpeed_shouldReturnCorrectValue() {
        assertEquals(0.4f, player.getMovementSpeed(), 0.001f);
    }
}

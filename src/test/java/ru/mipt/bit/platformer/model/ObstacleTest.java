package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ObstacleTest {

    @Test
    void constructor_shouldSetPositionAndType() {
        GridPoint2 position = new GridPoint2(2, 3);
        Obstacle obstacle = new Obstacle(position, ObstacleType.TREE);
        
        assertEquals(position, obstacle.getPosition());
        assertEquals(ObstacleType.TREE, obstacle.getType());
    }

    @Test
    void isPassable_shouldReturnFalseForTree() {
        Obstacle tree = new Obstacle(new GridPoint2(1, 1), ObstacleType.TREE);
        
        assertFalse(tree.isPassable());
        assertTrue(tree.isTree());
        assertFalse(tree.isWall());
    }

    @Test
    void isPassable_shouldReturnTrueForWall() {
        Obstacle wall = new Obstacle(new GridPoint2(1, 1), ObstacleType.WALL);
        
        assertTrue(wall.isPassable());
        assertFalse(wall.isTree());
        assertTrue(wall.isWall());
    }

    @Test
    void getPosition_shouldReturnCopy() {
        GridPoint2 originalPosition = new GridPoint2(5, 7);
        Obstacle obstacle = new Obstacle(originalPosition, ObstacleType.TREE);
        
        GridPoint2 returnedPosition = obstacle.getPosition();
        returnedPosition.x = 999; // мутируем копию
        
        assertEquals(originalPosition, obstacle.getPosition()); // оригинал не изменился
    }

    @Test
    void equals_shouldWorkCorrectly() {
        GridPoint2 position1 = new GridPoint2(2, 3);
        GridPoint2 position2 = new GridPoint2(2, 3);
        GridPoint2 position3 = new GridPoint2(4, 5);
        
        Obstacle obstacle1 = new Obstacle(position1, ObstacleType.TREE);
        Obstacle obstacle2 = new Obstacle(position2, ObstacleType.TREE);
        Obstacle obstacle3 = new Obstacle(position3, ObstacleType.TREE);
        Obstacle obstacle4 = new Obstacle(position1, ObstacleType.WALL);
        
        assertEquals(obstacle1, obstacle2); // одинаковые позиция и тип
        assertNotEquals(obstacle1, obstacle3); // разные позиции
        assertNotEquals(obstacle1, obstacle4); // разные типы
        assertNotEquals(obstacle1, null);
        assertNotEquals(obstacle1, "not an obstacle");
    }

    @Test
    void hashCode_shouldBeConsistentWithEquals() {
        GridPoint2 position = new GridPoint2(2, 3);
        Obstacle obstacle1 = new Obstacle(position, ObstacleType.TREE);
        Obstacle obstacle2 = new Obstacle(position, ObstacleType.TREE);
        
        assertEquals(obstacle1.hashCode(), obstacle2.hashCode());
    }

    @Test
    void toString_shouldContainPositionAndType() {
        GridPoint2 position = new GridPoint2(2, 3);
        Obstacle obstacle = new Obstacle(position, ObstacleType.TREE);
        String toString = obstacle.toString();
        
        assertTrue(toString.contains("2, 3"));
        assertTrue(toString.contains("TREE"));
    }
}

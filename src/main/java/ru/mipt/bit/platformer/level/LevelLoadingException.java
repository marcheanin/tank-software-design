package ru.mipt.bit.platformer.level;

public class LevelLoadingException extends Exception {
    
    public LevelLoadingException(String message) {
        super(message);
    }
    
    public LevelLoadingException(String message, Throwable cause) {
        super(message, cause);
    }
}

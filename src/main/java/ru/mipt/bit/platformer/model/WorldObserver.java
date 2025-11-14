package ru.mipt.bit.platformer.model;

public interface WorldObserver {
    void onObjectAdded(Object obj);
    void onObjectRemoved(Object obj);
}


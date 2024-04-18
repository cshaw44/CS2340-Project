package com.example.a2340project;

/**
 * Methods for managing observers
 */
public interface Observable {
    void registerObserver(Observer o);
    void removeObserver(Observer o);
    void notifyObservers();
}

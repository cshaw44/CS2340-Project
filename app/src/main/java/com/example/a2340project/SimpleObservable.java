package com.example.a2340project;

import java.util.ArrayList;
import java.util.List;

/**
 * This is only for junit4 testing purposes.
 */
public class SimpleObservable implements Observable {
    private List<Observer> observers = new ArrayList<>();

    @Override
    public void registerObserver(Observer o) {
        if (!observers.contains(o)) {
            observers.add(o);
        }
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }

    // For testing purposes: check the size of the observer list
    public int getObserverCount() {
        return observers.size();
    }
}

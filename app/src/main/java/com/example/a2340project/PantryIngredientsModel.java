package com.example.a2340project;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Concrete Observable
 * responsible for managing the data related to pantry ingredients
 * and notifying observers of any changes
 */
public class PantryIngredientsModel implements Observable {

    private List<Observer> observers = new ArrayList<>();
    private List<Recipe> recipes = new ArrayList<>();

    private Map<String, Ingredient> ingredients;

    public PantryIngredientsModel() {

    }

    // Observable methods
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

    public List<Recipe> getUpdatedRecipes() {
        return recipes;
    }

    // Method to be called when data changes
    public void ingredientUpdated() {
        notifyObservers();
    }
}

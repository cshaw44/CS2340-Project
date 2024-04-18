package com.example.a2340project;

import androidx.annotation.NonNull;

import java.util.Map;

// Also uses SOLID cause it is a recipe object
public class Recipe {

    private String name;
    private Map<String, Double> ingredientMap; // Ingredient and Amount

    private boolean canMake;

    
    public Recipe() {

    }

    public Recipe(String name, Map<String, Double> ingredientsMap) {
        this.name = name;
        this.ingredientMap = ingredientsMap;
    }

    public String getName() {
        return name;
    }

    public Map<String, Double> getIngredientMap() {
        return ingredientMap;
    }

    @NonNull
    public String toString() {
        return name + (canMake ? " - Have Ingredients" : " - Do Not Have Ingredients");
    }

    public void setCanMake(boolean canMake) {
        this.canMake = canMake;
    }
    public boolean getCanMake() {
        return canMake;
    }
}

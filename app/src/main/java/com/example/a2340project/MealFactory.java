package com.example.a2340project;

/**
 * MealFactory is responsible for creating meal objects based on given types of meal.
 * Encapsulates the instantiation logic
 */
public class MealFactory {
    public static MealPrep createMeal(Recipe recipe, String mealType) {
        switch (mealType.toLowerCase()) {
        case "breakfast":
            return new BreakfastMeal(recipe);
        case "dinner":
            return new DinnerMeal(recipe);
        default:
            throw new IllegalArgumentException("Unsupported meal type: " + mealType);
        }
    }
}

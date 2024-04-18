package com.example.a2340project;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class FactoryPatternTest {
    @Test
    public void dinnerGetRecipe() {
        Map<String, Double> map = new HashMap();
        map.put("Rice", 2d);
        map.put("Veggie Mix", 1d);
        map.put("Soy Sauce", 1d);
        Recipe recipe = new Recipe("Fried Rice", map);
        DinnerMeal meal = new DinnerMeal(recipe);
        assertEquals(recipe, meal.getRecipe());
    }
    @Test
    public void breakfastGetRecipe() {
        Map<String, Double> map = new HashMap();
        map.put("Rice", 2d);
        map.put("Veggie Mix", 1d);
        map.put("Soy Sauce", 1d);
        Recipe recipe = new Recipe("Fried Rice", map);
        BreakfastMeal meal = new BreakfastMeal(recipe);
        assertEquals(recipe, meal.getRecipe());
    }
    @Test
    public void dinnerSetRecipe() {
        Map<String, Double> map = new HashMap();
        map.put("Rice", 2d);
        map.put("Veggie Mix", 1d);
        map.put("Soy Sauce", 1d);
        Recipe recipe = new Recipe("Fried Rice", map);
        DinnerMeal meal = new DinnerMeal(null);
        meal.setRecipe(recipe);
        assertEquals(recipe, meal.getRecipe());
    }
    @Test
    public void breakfastSetRecipe() {
        Map<String, Double> map = new HashMap();
        map.put("Rice", 2d);
        map.put("Veggie Mix", 1d);
        map.put("Soy Sauce", 1d);
        Recipe recipe = new Recipe("Fried Rice", map);
        BreakfastMeal meal = new BreakfastMeal(null);
        meal.setRecipe(recipe);
        assertEquals(recipe, meal.getRecipe());
    }
}

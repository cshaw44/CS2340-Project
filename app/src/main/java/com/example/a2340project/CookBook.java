package com.example.a2340project;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// Use this class cause it uses SOLID principles cause it only operates on the cookbook

public class CookBook {

    private ArrayList<Recipe> cookBook;

    public CookBook(DatabaseReference ref)  {
        this.cookBook = getCookBookDatabase(ref);
    }


    public CookBook() {
        ArrayList<Recipe> testCookBook = new ArrayList<>();
        Map<String, Double> testIngredients = new HashMap<>();
        testIngredients.put("Fake Ingredient", 3.0);
        testCookBook.add(new Recipe("test", testIngredients));
        testCookBook.add(new Recipe("constructor", testIngredients));
        this.cookBook = testCookBook;
    }

    public ArrayList<Recipe> getCookBookDatabase(DatabaseReference ref) {
        /*
            Make call to firebase here using the "CookBook Database" reference sent
            from recipe screen(could put this in a view model)
            then store on cookBook variable
         */
        ArrayList<Recipe> recipes = new ArrayList<>();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap: snapshot.getChildren()) {
                    Recipe recipe = snap.getValue(Recipe.class);
                    recipes.add(recipe);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return recipes;
    }

    public ArrayList<Recipe> getCookBook() {
        return cookBook;
    }

    public ArrayList<String> cookBookToRecipeNames() {
        ArrayList<String> recipeNames = new ArrayList<>();
        for (Recipe recipe : cookBook) {
            recipeNames.add(recipe.getName());
        }
        return recipeNames;
    }

}

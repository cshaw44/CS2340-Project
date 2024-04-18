package com.example.a2340project;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

/**
 * A specific class that implements the MealPrep interface
 */
public class DinnerMeal implements MealPrep {
    private Recipe recipe;

    public DinnerMeal(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public void cook() {
        Map<String, Double> map = recipe.getIngredientMap();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference()
                .child("users").child(currentUser.getUid()).child("ingredients");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int calorieCount = 0;
                for (DataSnapshot snap: snapshot.getChildren()) {
                    Ingredient ing = snap.getValue(Ingredient.class);
                    for (Map.Entry<String, Double> entry: map.entrySet()) {
                        String ingredient = entry.getKey();
                        Double quantity = entry.getValue();
                        if (ing.getName().equals(ingredient)) {
                            calorieCount += (ing.getCaloriesPerServing() * quantity);
                            double subQuantity = ing.getQuantity() - quantity;
                            if (subQuantity <= 0) {
                                snap.getRef().removeValue();
                            } else {
                                snap.getRef().child("quantity").setValue(subQuantity);
                            }
                        }
                    }
                }
                Meal meal = new Meal(recipe.getName(), calorieCount);
                mDatabase.getParent().child("meal").push().setValue(meal);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public Recipe getRecipe() {
        return recipe;
    }


    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}

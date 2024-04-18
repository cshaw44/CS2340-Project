package com.example.a2340project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Class for the placeholder page for listing ingredients
 */
public class IngredientScreen extends AppCompatActivity implements RecyclerViewInterface {
    // recycle view stuff
    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;
    private IngredientListAdapter adapter;
    private ArrayList<Ingredient> ingredientArr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient);

        // recycle view stuff
        recyclerView = findViewById(R.id.pantryList);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference()
                .child("users").child(currentUser.getUid()).child("ingredients");
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ingredientArr = new ArrayList<>();
        adapter = new IngredientListAdapter(this, ingredientArr, this);
        recyclerView.setAdapter(adapter);


        //nav buttons
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView2);
        bottomNavigationView.setSelectedItemId(R.id.bottom_ingredients);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Ingredient ingredient = dataSnapshot.getValue(Ingredient.class);
                    String name = ingredient.getName();
                    boolean contains = false;
                    for (Ingredient ingred: ingredientArr) {
                        if (ingred.getName().equals(name)) {
                            contains = true;
                        }
                    }
                    if (!contains) {
                        ingredientArr.add(ingredient);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int buttonID = item.getItemId();
            if (buttonID == R.id.bottom_meals) {
                startActivity(new Intent(IngredientScreen.this, InputMealScreen.class));
                return true;
            } else if (buttonID == R.id.bottom_recipes) {
                startActivity(new Intent(IngredientScreen.this, RecipeScreen.class));
                return true;
            } else if (buttonID == R.id.bottom_shopping) {
                startActivity(new Intent(IngredientScreen.this, ShoppingListScreen.class));
                return true;
            } else if (buttonID == R.id.bottom_profile) {
                startActivity(new Intent(IngredientScreen.this, ProfileScreen.class));
                return true;
            } else {
                return buttonID == R.id.bottom_ingredients;
            }
        });

        findViewById(R.id.addIngredientButton).setOnClickListener(v -> showAddIngredientDialog());

        findViewById(R.id.addIngredientButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddIngredientDialog();
            }
        });
    }

    private void showChangeIngredientDialog(Ingredient ingredient, int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change Ingredient Quantity");

        // Inflate the custom layout
        final View customLayout = getLayoutInflater()
                .inflate(R.layout.activity_ingredient_change_dialog, null);
        builder.setView(customLayout);

        // EditText variables initialization
        TextView name = customLayout.findViewById(R.id.ingredientNameChange);
        name.setText(ingredient.getName());
        EditText quantityField = customLayout.findViewById(R.id.ingredientQuantityChange);

        // Add action button
        builder.setPositiveButton("Save", (dialog, id) -> {
            double quantity = Double.parseDouble(quantityField.getText().toString());
            double ingredientQuantity = ingredient.getQuantity();
            boolean delete = false;
            if (quantity != ingredientQuantity) {
                if (quantity <= 0d) {
                    delete = true;
                }
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference ingredientsRef = FirebaseDatabase.getInstance().getReference()
                        .child("users").child(currentUser.getUid()).child("ingredients");
                Query query = ingredientsRef.orderByChild("name").equalTo(ingredient.getName());
                boolean finalDelete = delete;
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (finalDelete) {
                            for (DataSnapshot snap: snapshot.getChildren()) {
                                Ingredient dataIngredient = snap.getValue(Ingredient.class);
                                if (dataIngredient.getName().equals(ingredient.getName())) {
                                    snap.getRef().removeValue();
                                    ingredientArr.remove(pos);
                                }
                            }
                        } else {
                            for (DataSnapshot snap: snapshot.getChildren()) {
                                Ingredient dataIngredient = snap.getValue(Ingredient.class);
                                if (dataIngredient.getName().equals(ingredient.getName())) {
                                    snap.getRef().child("quantity").setValue(quantity);
                                    ingredient.setQuantity(quantity);
                                    ingredientArr.set(pos, ingredient);
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

        });

        builder.setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showAddIngredientDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Ingredient");

        // Inflate the custom layout
        final View customLayout = getLayoutInflater()
                .inflate(R.layout.activity_add_ingredient_dialog, null);
        builder.setView(customLayout);

        // EditText variables initialization
        EditText nameField = customLayout.findViewById(R.id.ingredientNameEditText);
        EditText quantityField = customLayout.findViewById(R.id.ingredientQuantityEditText);
        EditText caloriesField = customLayout.findViewById(R.id.caloriesPerServingEditText);
        EditText expirationDateField = customLayout.findViewById(R.id.expirationDateEditText);

        // Add action button
        builder.setPositiveButton("Add", (dialog, id) -> {
            String ingredientName = nameField.getText().toString().trim();
            try {
                double quantity = Double.parseDouble(quantityField.getText().toString().trim());
                double calories = Double.parseDouble(caloriesField.getText().toString().trim());
                String expirationDate = expirationDateField.getText().toString().trim();

                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null && quantity > 0) {
                    DatabaseReference ingredientsRef = FirebaseDatabase.getInstance().getReference()
                            .child("users").child(currentUser.getUid()).child("ingredients");
                    Query query = ingredientsRef.orderByChild("name").equalTo(ingredientName);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()) {
                                // Ingredient does not exist, add new ingredient
                                ingredientsRef.push().setValue(new Ingredient(ingredientName,
                                        quantity, calories, expirationDate));
                            }
                            // If ingredient exists, do nothing
                            // maybe later add error pop up?? but not necessary right now
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // nothing needed at the moment
                        }

                    });
                }
            } catch (NumberFormatException e) {
                // nothing needed at the moment
            }
        });

        builder.setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public ArrayList<Ingredient> getIngredientArr() {
        return ingredientArr;
    }


    @Override
    public void onItemClick(int pos) {
        Ingredient ingredient = ingredientArr.get(pos);
        showChangeIngredientDialog(ingredient, pos);
    }
}

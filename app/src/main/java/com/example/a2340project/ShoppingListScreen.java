package com.example.a2340project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * twenty one pilots is releasing new music on the 29th apparently so that is hype
 */
public class ShoppingListScreen extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference mDatabase;
    private EditText shoppingListItem;
    private EditText shoppingListQuantity;
    private Button addButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        // get text and buttons
        shoppingListItem = findViewById(R.id.addShoppingListItem);
        shoppingListQuantity = findViewById(R.id.addShoppingListQuantity);
        addButton = findViewById(R.id.shoppingListAddButton);
        TextView error = findViewById(R.id.ShoppingListError);

        // getting firebase instance
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        currentUser = mAuth.getCurrentUser();

        //nav buttons
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView2);
        bottomNavigationView.setSelectedItemId(R.id.bottom_shopping);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int buttonID = item.getItemId();
            if (buttonID == R.id.bottom_meals) {
                startActivity(new Intent(ShoppingListScreen.this, InputMealScreen.class));
                return true;
            } else if (buttonID == R.id.bottom_recipes) {
                startActivity(new Intent(ShoppingListScreen.this, RecipeScreen.class));
                return true;
            } else if (buttonID == R.id.bottom_shopping) {
                return true;
            } else if (buttonID == R.id.bottom_ingredients) {
                startActivity(new Intent(ShoppingListScreen.this, IngredientScreen.class));
                return true;
            } else if (buttonID == R.id.bottom_profile) {
                startActivity(new Intent(ShoppingListScreen.this, ProfileScreen.class));
                return true;
            } else {
                return buttonID == R.id.bottom_shopping;
            }
        });

        // add to shopping list
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemName = shoppingListItem.getText().toString();
                String quantity = shoppingListQuantity.getText().toString();
                if (itemName.contains("\\S+") || quantity.contains("\\S+")
                        || itemName.equals("") || quantity.equals("")) {
                    itemName = "";
                    quantity = "";
                    error.setVisibility(View.VISIBLE);
                } else {
                    if (currentUser != null) {
                        /*
                        for item in user pantry
                            if item equals itemName
                            we do stuff

                         */
                        // else { }
                        String userID = currentUser.getUid();
                        DatabaseReference userRef = mDatabase.child("users").child(userID);
                        int quantityAmount = Integer.parseInt(quantity);
                        // check to see if item is already in shopping list,
                        // if it is we will update its quantity with the new input.
                        String finalItemName = itemName;
                        final boolean[] foundItem = {false};
                        mDatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    ShoppingListItem shoppingListItemSearch = dataSnapshot
                                            .getValue(ShoppingListItem.class);
                                    String searchName = shoppingListItemSearch.getName();
                                    if (finalItemName.equals(searchName)) {
                                        shoppingListItemSearch.setQuantity(quantityAmount);
                                        foundItem[0] = true;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // some kind of error
                                return;
                            }
                        });
                        if (!foundItem[0]) {
                            userRef.child("Shopping List").push()
                                    .setValue(new ShoppingListItem(itemName, quantityAmount));
                        }
                        shoppingListItem.setText("");
                        shoppingListQuantity.setText("");


                        //get rid of anything with zero or less quantity (lazy)
                        mDatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    ShoppingListItem shoppingListItemCheck = dataSnapshot
                                            .getValue(ShoppingListItem.class);
                                    if (shoppingListItemCheck.getQuantity() <= 0) {
                                        dataSnapshot.getRef().removeValue();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                return; //idk
                            }
                        });
                    }
                }
            }
        });
    }
}

package com.example.a2340project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.util.Log;

import android.widget.EditText;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

/**
 * Class for the placeholder page for Inputting meals
 */

public class InputMealScreen extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference mDatabase;
    private EditText mealInputText;
    private EditText calorieInputText;
    private Button addMealButton;
    private InputMealViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_input_meal);

        //generate data structure buttons
        Button dailyIntakeDailyGoal = findViewById(R.id.dailyIntakeDailyGoal);
        Button dailyIntakeOverMonth = findViewById(R.id.dailyIntakeOverMonth);
        TextView heightText = findViewById(R.id.tvUserHeight);
        TextView weightText = findViewById(R.id.tvUserWeight);
        TextView genderText = findViewById(R.id.tvUserGender);
        TextView dailyGoal = findViewById(R.id.tvDailyGoal);
        addMealButton = findViewById(R.id.addMealButton);
        viewModel = InputMealViewModel.getInstance();
        mealInputText = findViewById(R.id.inputMealName);
        calorieInputText = findViewById(R.id.inputCalorieEstimate);
        TextView dailyCalories = findViewById(R.id.tvDailyCalorieIntake);
        TextView error = findViewById(R.id.Error);


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        currentUser = mAuth.getCurrentUser();
        dailyIntakeDailyGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        dailyIntakeOverMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mealInputText = findViewById(R.id.inputMealName);
        calorieInputText = findViewById(R.id.inputCalorieEstimate);

        //nav buttons
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView2);
        bottomNavigationView.setSelectedItemId(R.id.bottom_meals);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int buttonID = item.getItemId();
            if (buttonID == R.id.bottom_meals) {
                return true;
            } else if (buttonID == R.id.bottom_recipes) {
                startActivity(new Intent(InputMealScreen.this, RecipeScreen.class));
                return true;
            } else if (buttonID == R.id.bottom_shopping) {
                startActivity(new Intent(InputMealScreen.this, ShoppingListScreen.class));
                return true;
            } else if (buttonID == R.id.bottom_ingredients) {
                startActivity(new Intent(InputMealScreen.this, IngredientScreen.class));
                return true;
            } else if (buttonID == R.id.bottom_profile) {
                startActivity(new Intent(InputMealScreen.this, ProfileScreen.class));
                return true;
            } else {
                return false;
            }
        });

        // add meal button WIP
        addMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String meal = mealInputText.getText().toString();
                String calories = calorieInputText.getText().toString();
                if (meal.contains("\\S+") || calories.contains("\\S+")
                        || meal.equals("") || calories.equals("")) {
                    meal = "";
                    calories = "";
                    error.setVisibility(View.VISIBLE);
                } else {
                    // logic for adding a meal and calories given the user is signed in
                    if (currentUser != null) {
                        String userID = currentUser.getUid();
                        DatabaseReference userRef = mDatabase.child("users").child(userID);
                        int calorie = Integer.parseInt(calories);
                        userRef.child("meal").push().setValue(new Meal(meal, calorie));
                        mealInputText.setText("");
                        calorieInputText.setText("");
                    }

                }
            }
        });

        String userID = currentUser.getUid();
        DatabaseReference userRef = mDatabase.child("users").child(userID);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("feet").getValue() != null
                        || snapshot.child("inches").getValue() != null
                        || snapshot.child("pounds").getValue() != null
                        || snapshot.child("gender").getValue() != null) {
                    int feet = Integer.parseInt(snapshot.child("feet").getValue().toString());
                    int inches = Integer.parseInt(snapshot.child("inches").getValue().toString());
                    int weight = Integer.parseInt(snapshot.child("pounds").getValue().toString());
                    String gender = snapshot.child("gender").getValue().toString();
                    viewModel.updateData(feet, inches, weight, gender);
                    setDailyCalorieGoal(viewModel, dailyGoal);
                    String height = String.format("%d\'%d\"", feet, inches);
                    heightText.setText("Height: " + height);
                    weightText.setText("Weight: " + weight);
                    genderText.setText("Gender: " + gender);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference userMealsRef = userRef.child("meal");
        Calendar cal = Calendar.getInstance();
        // Resetting time to start of the day for accurate comparison
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long startOfDay = cal.getTimeInMillis();
        cal.add(Calendar.DATE, 1);
        long startOfNextDay = cal.getTimeInMillis();
        userMealsRef.orderByChild("date").startAt(startOfDay).endAt(startOfNextDay).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int dailyIntake = 0;
                for (DataSnapshot data: snapshot.getChildren()) {
                    Meal meal = data.getValue(Meal.class);
                    if (meal != null) {
                        dailyIntake += meal.getCalories();
                    }
                }
                dailyCalories.setText("Daily Calorie Intake: " + dailyIntake);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setDailyCalorieGoal(InputMealViewModel viewModel, TextView dailyGoal) {
        int feet = viewModel.getUserData().getHeightFeet();
        int inches = (feet * 12) + viewModel.getUserData().getHeightInches();
        int height = (int) (inches * 2.54);
        int weight = viewModel.getUserData().getWeight();
        String gender = viewModel.getUserData().getGender();
        int goal;
        switch (gender) {
        case "Male": goal = (int) ((13.397 * weight) + (4.799 * height) - 25.178);
            break;
        case "Female": goal = (int) ((9.247 * weight) + (3.098 * height) - 360.963);
            break;
        default: goal = (int) ((11.322 * weight) + (3.949 * height) - 193.071);
        }
        dailyGoal.setText("Daily Calorie Goal: " + goal);
    }
//    private void calculateAndDisplayDailyCalorieIntake() {
//        if (currentUser != null) {
//            String userID = currentUser.getUid();
//            DatabaseReference userMealsRef = mDatabase.child("users").child(userID).child("meals");
//            Calendar cal = Calendar.getInstance();
//            // Resetting time to start of the day for accurate comparison
//            cal.set(Calendar.HOUR_OF_DAY, 0);
//            cal.set(Calendar.MINUTE, 0);
//            cal.set(Calendar.SECOND, 0);
//            cal.set(Calendar.MILLISECOND, 0);
//            long startOfDay = cal.getTimeInMillis();
//
//            cal.add(Calendar.DATE, 1);
//            long startOfNextDay = cal.getTimeInMillis();
//
//            userMealsRef.orderByChild("date").startAt(startOfDay).endAt(startOfNextDay)
//                    .addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            int totalCalories = 0;
//                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                                Meal meal = snapshot.getValue(Meal.class);
//                                if (meal != null) {
//                                    totalCalories += meal.getCalories();
//                                }
//                            }
//                            TextView dailyCalorieIntakeTextView =
//                                    findViewById(R.id.tvDailyCalorieIntake);
//                            dailyCalorieIntakeTextView.setText("Daily Calorie Intake: "
//                                    + totalCalories);
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//                            Log.w("DBError", "loadMeal:onCancelled", databaseError.toException());
//                        }
//                    });
//        }
//    }

}

package com.example.a2340project;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
public class IngredientScreenTest {

    private IngredientScreen ingredientScreen;

    @Before
    public void setUp() {
        ingredientScreen = new IngredientScreen();
    }

    // these are incredibly simple tests, this is my first time making J units.
    @Test
    public void testIngredientArrInitialization() {
        assertNotNull(ingredientScreen.getIngredientArr());
        assertEquals(0, ingredientScreen.getIngredientArr().size());
    }

    @Test
    public void testAddIngredientButtonClick() {
        // Simulate button click and test change
        int initialSize = ingredientScreen.getIngredientArr().size();
        ingredientScreen.findViewById(R.id.addIngredientButton).performClick();
        assertEquals(initialSize, ingredientScreen.getIngredientArr().size());
    }
}

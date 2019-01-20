package com.udacity.heather.timetobake.utilities;

import com.udacity.heather.timetobake.models.Ingredient;
import com.udacity.heather.timetobake.models.Recipe;
import com.udacity.heather.timetobake.models.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private static final String RECIPE_ID_KEY = "id";
    private static final String NAME_KEY = "name";
    private static final String INGREDIENTS_KEY = "ingredients";
    private static final String STEPS_KEY = "steps";
    private static final String SERVINGS_KEY = "servings";
    private static final String IMAGE_KEY = "image";

    private static final String QUANTITY_KEY = "quantity";
    private static final String MEASURE_KEY = "measure";
    private static final String INGREDIENT_KEY = "ingredient";

    private static final String STEP_ID_KEY = "id";
    private static final String SHORT_DESCRIPTION_KEY = "shortDescription";
    private static final String DESCRIPTION_KEY = "description";
    private static final String VIDEO_URL_KEY = "videoURL";

    // Constructor
    private JsonUtils() {
        throw new AssertionError();
    }

    // Extract data Strings and return a list of "Recipe"
    public static List<Recipe> extractRecipesData(String jsonResponse) {
        if (jsonResponse == null) {
            return null;
        }
        List<Recipe> recipes = new ArrayList<>();
        try {
            JSONArray baseJsonResponseArray = new JSONArray(jsonResponse);
            for (int i = 0; i < baseJsonResponseArray.length(); i++) {
                JSONObject currentRecipe = baseJsonResponseArray.getJSONObject(i);

               int recipeId = currentRecipe.optInt(RECIPE_ID_KEY);
                String recipeName = currentRecipe.optString(NAME_KEY);

               List<Ingredient> recipeIngredients = new ArrayList<>();
                List<Step> recipeSteps = new ArrayList<>();

                int recipeServings = currentRecipe.optInt(SERVINGS_KEY);
                String recipeImage = currentRecipe.optString(IMAGE_KEY);

                JSONArray ingredientsJSONArray = currentRecipe.optJSONArray(INGREDIENTS_KEY);
                JSONArray stepsJSONArray = currentRecipe.optJSONArray(STEPS_KEY);

               for (int j = 0; j < ingredientsJSONArray.length(); j++) {
                    Ingredient currentRecipeIngredient = new Ingredient();
                    JSONObject currentIngredientsJSON = ingredientsJSONArray.getJSONObject(j);
                    float quantity = currentIngredientsJSON.optInt(QUANTITY_KEY);
                    String measure = currentIngredientsJSON.optString(MEASURE_KEY);
                    String ingredient = currentIngredientsJSON.optString(INGREDIENT_KEY);
                    currentRecipeIngredient.setQuantity(quantity);
                    currentRecipeIngredient.setIngredient(ingredient);
                    currentRecipeIngredient.setMeasure(measure);
                    recipeIngredients.add(currentRecipeIngredient);
                }

                for (int k = 0; k < stepsJSONArray.length(); k++) {
                    Step currentRecipeStep = new Step();
                    JSONObject currentStepsJSON = stepsJSONArray.getJSONObject(k);
                    int stepId = currentStepsJSON.optInt(STEP_ID_KEY);
                    String shortDescription = currentStepsJSON.optString(SHORT_DESCRIPTION_KEY);
                    String description = currentStepsJSON.optString(DESCRIPTION_KEY);
                    String videoURL = currentStepsJSON.optString(VIDEO_URL_KEY);
                    currentRecipeStep.setId(stepId);
                    currentRecipeStep.setShortDescription(shortDescription);
                    currentRecipeStep.setDescription(description);
                    currentRecipeStep.setVideoURL(videoURL);
                    recipeSteps.add(currentRecipeStep);
                }


                Recipe recipe = new Recipe(recipeId, recipeName, recipeIngredients,
                        recipeSteps, recipeServings, recipeImage);
                recipes.add(recipe);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return recipes;
    }
}



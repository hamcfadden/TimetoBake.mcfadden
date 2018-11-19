package com.udacity.heather.timetobake.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.udacity.heather.timetobake.Constants;
import com.udacity.heather.timetobake.R;
import com.udacity.heather.timetobake.fragments.IngredientFragment;
import com.udacity.heather.timetobake.fragments.RecipeFragment;
import com.udacity.heather.timetobake.fragments.StepFragment;
import com.udacity.heather.timetobake.models.Recipe;


public class RecipeStepActivity extends AppCompatActivity implements RecipeFragment.OnCurrentRecipeClickListener {

    private Recipe currentRecipe = new Recipe();

    public boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);


        if (findViewById(R.id.detail_fragment_container) != null) {
            // Tablet
            mTwoPane = true;

            if (savedInstanceState == null) {
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    currentRecipe = extras.getParcelable(Constants.CURRENT_RECIPE);

                    FragmentManager fragmentManager = getSupportFragmentManager();

                    RecipeFragment recipeFragment = new RecipeFragment();
                    Bundle recipeBundle = new Bundle();
                    recipeBundle.putParcelable(Constants.CURRENT_RECIPE, currentRecipe);
                    recipeFragment.setArguments(recipeBundle);
                    fragmentManager.beginTransaction().add(R.id.recipe_fragment_container, recipeFragment)
                            .commit();

                    IngredientFragment ingredientsFragment = new IngredientFragment();
                    Bundle ingredientsBundle = new Bundle();
                    ingredientsBundle.putParcelable(Constants.CURRENT_RECIPE, currentRecipe);
                    ingredientsFragment.setArguments(ingredientsBundle);
                    fragmentManager.beginTransaction().add(R.id.detail_fragment_container, ingredientsFragment)
                            .commit();
                }

            } else {
                currentRecipe = savedInstanceState.getParcelable(Constants.CURRENT_RECIPE);
            }
            if (currentRecipe != null) setTitle(currentRecipe.getName());
             } else {

            // Smartphone
            mTwoPane = false;
            if (savedInstanceState == null) {
                Bundle extras = getIntent().getExtras();
                if (extras != null) {

                    currentRecipe = extras.getParcelable(Constants.CURRENT_RECIPE);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    RecipeFragment recipeFragment = new RecipeFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(Constants.CURRENT_RECIPE, currentRecipe);
                    recipeFragment.setArguments(bundle);
                    fragmentManager.beginTransaction().add(R.id.recipe_fragment_container, recipeFragment)
                            .commit();
                }
            } else {
                currentRecipe = savedInstanceState.getParcelable(Constants.CURRENT_RECIPE);
            }
            if (currentRecipe != null) setTitle(currentRecipe.getName());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(Constants.CURRENT_RECIPE, currentRecipe);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStepSelected(int position) {
        // Tablet
        if (mTwoPane) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            StepFragment stepFragment = new StepFragment();
            Bundle stepBundle = new Bundle();
            stepBundle.putParcelable(Constants.CURRENT_RECIPE, currentRecipe);
            stepBundle.putInt(Constants.CURRENT_STEP_POSITION_KEY, position);
            stepBundle.putBoolean(Constants.TWO_PANE_KEY, true);
            stepFragment.setArguments(stepBundle);
            fragmentManager.beginTransaction().replace(R.id.detail_fragment_container, stepFragment)
                    .commit();
        } else {
            // Smartphone
            Intent intent = new Intent(this, RecipeDetailActivity.class);
            intent.putExtra(Constants.CURRENT_RECIPE, currentRecipe);
            intent.putExtra(Constants.CURRENT_STEP_POSITION_KEY, position);
            intent.setAction(Constants.RECIPE_ACTION_STEP);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
            Log.w(RecipeStepActivity.class.getSimpleName(), ">>>>>>>>>>>>>>>>>>>>>>>>>onInstructionSelected Activity");
        }
    }

    @Override
    public void onIngredientSelected() {
        // Tablet
        if (mTwoPane) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            IngredientFragment ingredientFragment = new IngredientFragment();
            Bundle ingredientsBundle = new Bundle();
            ingredientsBundle.putParcelable(Constants.CURRENT_RECIPE, currentRecipe);
            ingredientFragment.setArguments(ingredientsBundle);
            fragmentManager.beginTransaction().replace(R.id.detail_fragment_container, ingredientFragment)
                    .commit();
        } else {
            // Smartphone
            Intent intent = new Intent(this, RecipeDetailActivity.class);
            intent.putExtra(Constants.CURRENT_RECIPE, currentRecipe);
            intent.setAction(Constants.RECIPE_ACTION_INGREDIENTS);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recipe_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.back:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}


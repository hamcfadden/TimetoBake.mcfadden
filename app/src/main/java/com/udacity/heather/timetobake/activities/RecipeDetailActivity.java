package com.udacity.heather.timetobake.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.udacity.heather.timetobake.utilities.Constants;
import com.udacity.heather.timetobake.R;
import com.udacity.heather.timetobake.fragments.IngredientFragment;
import com.udacity.heather.timetobake.fragments.StepFragment;
import com.udacity.heather.timetobake.fragments.StepFragment.NextPreviousClickListener;
import com.udacity.heather.timetobake.models.Recipe;


public class RecipeDetailActivity extends AppCompatActivity implements StepFragment.NextPreviousClickListener {
    private Recipe currentRecipe;
    private int stepPosition;
    FragmentManager fragmentManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        if (savedInstanceState == null) {
            fragmentManager = getSupportFragmentManager();
            Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            if (extras != null) {
                currentRecipe = extras.getParcelable(Constants.CURRENT_RECIPE);
                stepPosition = extras.getInt(Constants.CURRENT_STEP_POSITION_KEY);

                if (intent.getAction().equals(Constants.ACTION_RECIPE_INGREDIENTS)) {
                    setTitle(currentRecipe.getName());
                    IngredientFragment ingredientFragment = new IngredientFragment();
                    Bundle ingredientsBundle = new Bundle();
                    ingredientsBundle.putParcelable(Constants.CURRENT_RECIPE, currentRecipe);
                    ingredientFragment.setArguments(ingredientsBundle);
                    fragmentManager.beginTransaction().add(R.id.ingredient_fragment_container, ingredientFragment)
                            .commit();
                } else {
                    setTitle(currentRecipe.getSteps().get(stepPosition).getShortDescription());
                    StepFragment stepFragment = new StepFragment();
                    Bundle stepBundle = new Bundle();
                    stepBundle.putParcelable(Constants.CURRENT_RECIPE, currentRecipe);
                    stepBundle.putInt(Constants.CURRENT_STEP_POSITION_KEY, stepPosition);
                    stepBundle.putBoolean(Constants.TWO_PANE_KEY, false);
                    stepFragment.setArguments(stepBundle);
                    fragmentManager.beginTransaction().add(R.id.step_fragment_container, stepFragment)
                            .commit();
                }
            }
        } else {
            currentRecipe = savedInstanceState.getParcelable(Constants.CURRENT_RECIPE);
            stepPosition = savedInstanceState.getInt(Constants.CURRENT_STEP_POSITION_KEY);
        }
    }
    @Override
    protected void onSaveInstanceState (Bundle outState){
        outState.putParcelable(Constants.CURRENT_RECIPE, currentRecipe);
        outState.putInt(Constants.CURRENT_STEP_POSITION_KEY, stepPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onNextClicked () {
        setTitle(currentRecipe.getSteps().get(stepPosition).getShortDescription());
        int stepCount = currentRecipe.getSteps().size();
        if (stepPosition < stepCount - 1) {
            stepPosition++;
            replaceStepFragment();
        } else {
            Toast.makeText(this, getString(R.string.last_step_toast_message), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPreviousClicked() {
        setTitle(currentRecipe.getSteps().get(stepPosition).getShortDescription());
        if (stepPosition > 0) {
            stepPosition--;
            replaceStepFragment();
        } else {
            Toast.makeText(this, getString(R.string.first_step_toast_message), Toast.LENGTH_SHORT).show();
        }
    }

    private void replaceStepFragment() {
        StepFragment stepFragment = new StepFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.CURRENT_RECIPE, currentRecipe);
        bundle.putInt(Constants.CURRENT_STEP_POSITION_KEY, stepPosition);
        bundle.putBoolean(Constants.TWO_PANE_KEY, false);
        stepFragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.step_fragment_container, stepFragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recipe_detail_menu, menu);
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
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}

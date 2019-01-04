package com.udacity.heather.timetobake.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import com.udacity.heather.timetobake.R;
import com.udacity.heather.timetobake.RecipeIdlingResource;
import com.udacity.heather.timetobake.RecipeViewModel;
import com.udacity.heather.timetobake.adapters.RecipeListAdapter;
import com.udacity.heather.timetobake.databinding.ActivityRecipeListBinding;
import com.udacity.heather.timetobake.models.Recipe;
import com.udacity.heather.timetobake.utilities.Constants;
import com.udacity.heather.timetobake.utilities.NetworkUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.IdlingResource;


public class RecipeListActivity extends AppCompatActivity implements RecipeListAdapter.RecipeListAdapterOnClickHandler {
    private ActivityRecipeListBinding mainBinding;
    private RecipeListAdapter recipeListAdapter;

    @Nullable
    private RecipeIdlingResource mIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new RecipeIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      mainBinding = DataBindingUtil.setContentView(this,R.layout.activity_recipe_list);

        //  if (savedInstanceState != null) {
        RecyclerView.LayoutManager layoutManager;


        int orientation = getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // In landscape
            layoutManager = new LinearLayoutManager(this);
        } else {
            // In portrait
            layoutManager = new LinearLayoutManager(this);
        }

        mainBinding.rvRecipeList.setLayoutManager(layoutManager);
        mainBinding.rvRecipeList.setHasFixedSize(true);
        recipeListAdapter = new RecipeListAdapter(this);
        mainBinding.rvRecipeList.setAdapter(recipeListAdapter);


        setupViewModel();
        getIdlingResource();
    }


    private void setupViewModel() {
        RecipeViewModel recipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
        if (NetworkUtils.isConnected(this) && recipeViewModel.getRecipes() != null) {
            showRecipesDataView();
            recipeViewModel.getRecipes().observe(this, recipeList -> {

                recipeListAdapter.setRecipesData(recipeList);
                recipeListAdapter.notifyDataSetChanged();
            });
        } else {
            showErrorMessage();
        }
    }

    private void showRecipesDataView() {

        mainBinding.rvRecipeList.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mainBinding.rvRecipeList.setVisibility(View.INVISIBLE);

    }

  //  @Override
   // public void onClick(Recipe clickedItemIndex) {
       // Bundle selectedRecipeBundle = new Bundle();
     //   ArrayList<Recipe> selectedRecipe = new ArrayList<>(); {


      //  };
      //  selectedRecipe.add(clickedItemIndex);
      //  selectedRecipeBundle.putParcelableArrayList("recipe", selectedRecipe);

       // final Intent intent = new Intent(this, RecipeDetailActivity.class);
       // intent.putExtras(selectedRecipeBundle);
       // startActivity(intent);
   // }
//}


      public void onClick(Recipe currentRecipe) {
      Intent intent = new Intent(RecipeListActivity.this, RecipeStepActivity.class);
      intent.putExtra(Constants.CURRENT_RECIPE, currentRecipe);
      if (intent.resolveActivity(getPackageManager()) != null) {
      startActivity(intent);
      }
      }
      }









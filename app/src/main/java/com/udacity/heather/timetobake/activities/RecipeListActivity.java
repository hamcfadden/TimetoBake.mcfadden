package com.udacity.heather.timetobake.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.udacity.heather.timetobake.utilities.Constants;
import com.udacity.heather.timetobake.R;
import com.udacity.heather.timetobake.RecipeIdlingResource;
import com.udacity.heather.timetobake.ViewModel;
import com.udacity.heather.timetobake.adapters.RecipeListAdapter;
import com.udacity.heather.timetobake.databinding.ActivityRecipeListBinding;
import com.udacity.heather.timetobake.models.Recipe;
import com.udacity.heather.timetobake.utilities.NetworkUtils;
import com.udacity.heather.timetobake.utilities.RecipeLibraryService;

import java.util.List;


public class RecipeListActivity extends AppCompatActivity implements RecipeListAdapter.RecipeListAdapterOnClickHandler {
    private ActivityRecipeListBinding mainBinding;
    private RecipeListAdapter recipeListAdapter;
    private Boolean mTwoPane;


    RecipeLibraryService recipeLibraryService;

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
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_list);

        RecyclerView.LayoutManager layoutManager;
      //  if(savedInstanceState != null) {

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
        ViewModel viewModel = ViewModelProviders.of(this).get(ViewModel.class);
        if (NetworkUtils.isConnected(this) && viewModel.getRecipeList() != null) {
            showRecipesDataView();
            viewModel.getRecipeList().observe(this, new Observer<List<Recipe>>() {


                @Override
                public void onChanged(@Nullable List<Recipe> recipeList) {

                    recipeListAdapter.setRecipesData(recipeList);
                    recipeListAdapter.notifyDataSetChanged();
                }

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

    @Override
    public void onClick(Recipe currentRecipe) {

        Intent intent = new Intent(RecipeListActivity.this, RecipeStepActivity.class);
        intent.putExtra(Constants.CURRENT_RECIPE, currentRecipe);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}








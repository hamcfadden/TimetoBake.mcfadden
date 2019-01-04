package com.udacity.heather.timetobake;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.udacity.heather.timetobake.models.Recipe;
import com.udacity.heather.timetobake.utilities.NetworkUtils;
import com.udacity.heather.timetobake.utilities.RecipeLibraryCallback;
import com.udacity.heather.timetobake.utilities.RecipeLibraryManager;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class RecipeViewModel extends AndroidViewModel {

    @SuppressLint("StaticFieldLeak")
    private Context mContext;
    private MutableLiveData<List<Recipe>> recipes
            ;

    public RecipeViewModel(@NonNull Application application) {
        super(application);
        mContext = application.getApplicationContext();
        if (recipes == null) {
            recipes = new MutableLiveData<>();
            loadRecipes();
        }
    }

    public MutableLiveData<List<Recipe>> getRecipes() {
        return recipes;
    }

    private void loadRecipes() {

        if (NetworkUtils.isConnected(mContext)) {
            RecipeLibraryManager.getInstance().getRecipes(new RecipeLibraryCallback<List<Recipe>>() {
                @Override
                public void onResponse(final List<Recipe> recipes) {
                    if (recipes != null && recipes.size() > 0) {
                        setLoadedRecipesToLiveDataObject(recipes);
                    } else {
                        Toast.makeText(mContext, mContext.getResources().getString(R.string.no_data_to_display), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancel() {
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.loading_canceled), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(mContext, mContext.getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }
    }

    private void setLoadedRecipesToLiveDataObject(List<Recipe> recipe) {
        recipes.setValue(recipe);
    }

}
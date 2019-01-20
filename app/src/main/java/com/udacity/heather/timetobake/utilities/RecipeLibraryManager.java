package com.udacity.heather.timetobake.utilities;

import android.util.Log;

import com.udacity.heather.timetobake.models.Recipe;

import java.util.List;

import androidx.annotation.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RecipeLibraryManager {

    private  RecipeLibraryService RecipeLibraryService;

    private String TAG = RecipeLibraryManager.class.getSimpleName();
    private static volatile RecipeLibraryManager sharedInstance = new RecipeLibraryManager();


    private RecipeLibraryManager() {
        if (sharedInstance != null) {
            throw new RuntimeException("You can only make one instance of this class.");
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.REQUEST_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RecipeLibraryService = retrofit.create(RecipeLibraryService.class);
    }
    public static RecipeLibraryManager getInstance() {
        if (sharedInstance == null) {
            synchronized (RecipeLibraryManager.class) {
                if (sharedInstance == null) sharedInstance = new RecipeLibraryManager();
            }
        }
        return sharedInstance;
    }


   public void getRecipes(final RecipeLibraryCallback<List<Recipe>> recipeLibraryCallback) {
        RecipeLibraryService.getRecipes().enqueue(new Callback<List<Recipe>>() {

            @Override
            public void onResponse(@NonNull Call<List<Recipe>> call, Response<List<Recipe>> response) {
                recipeLibraryCallback.onResponse(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<Recipe>> call, Throwable t) {
                if (call.isCanceled()) {
                    Log.e(TAG, "Request was cancelled");
                    recipeLibraryCallback.onCancel();
                } else {
                    Log.e(TAG, t.getMessage());
                    recipeLibraryCallback.onResponse(null);
                }
            }
        });
            }
    }


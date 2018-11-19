package com.udacity.heather.timetobake.utilities;

import android.support.annotation.NonNull;
import android.util.Log;

import com.udacity.heather.timetobake.Constants;
import com.udacity.heather.timetobake.models.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.support.constraint.Constraints.TAG;


public class RecipeLibraryManager {

    private final  String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";
    private final RecipeLibraryService RecipeLibraryService;

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
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                recipeLibraryCallback.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
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


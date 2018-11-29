package com.udacity.heather.timetobake.utilities;

import com.udacity.heather.timetobake.models.Recipe;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipeLibraryService {

    //Get list of recipes
    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> getRecipes();
}


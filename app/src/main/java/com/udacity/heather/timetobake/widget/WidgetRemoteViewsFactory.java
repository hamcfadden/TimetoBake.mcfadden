package com.udacity.heather.timetobake.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.udacity.heather.timetobake.R;
import com.udacity.heather.timetobake.models.Ingredient;
import com.udacity.heather.timetobake.models.Recipe;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    public static ArrayList<Recipe>  selectedRecipe = new ArrayList<>();
    private static final String TAG = "IngredientsListRemoteViewFactory";

    ArrayList<Ingredient> ingredientList = new ArrayList<>();
    Context mContext;

    public WidgetRemoteViewsFactory(Context applicationContext){
        mContext = applicationContext;
    }

    private void readIngredients(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());
        Gson gson = new Gson();

        String json = prefs.getString("ingredients", "");
        Type type = new TypeToken<ArrayList<Ingredient>>(){}.getType();
        ingredientList = gson.fromJson(json, type);
    }

    @Override
    public void onCreate() {
        readIngredients();
    }

    @Override
    public void onDataSetChanged() {
        readIngredients();

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return ingredientList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.ingredients_widget_list_item);

        views.setTextViewText(R.id.current_recipe_layout, "\u2022 " + ingredientList.get(position).getIngredient()
                + "\n" +  "Quantity: " + String.valueOf(ingredientList.get(position).getQuantity())
                + "\n" +  "Measure: " + ingredientList.get(position).getMeasure());


        Bundle selectedRecipeBundle = new Bundle();
        selectedRecipeBundle.putParcelableArrayList("recipe",selectedRecipe);

        Intent fillInIntent = new Intent();
        views.setOnClickFillInIntent(R.id.recipe_widget_listview, fillInIntent);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }



    }
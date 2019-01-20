package com.udacity.heather.timetobake.widget;


import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;

import com.udacity.heather.timetobake.models.Recipe;
import com.udacity.heather.timetobake.utilities.Constants;

import java.util.List;

import androidx.annotation.Nullable;

public class WidgetRecipeLoadService extends IntentService {
    public WidgetRecipeLoadService() {
        super("WidgetRecipeLoadService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingAppWidget.class));
            // If action = ACTION_PREVIOUS_RECIPE move to previous Recipe and update widget
        if (intent.getAction().equals(Constants.ACTION_PREVIOUS_RECIPE)) {
            int position = intent.getExtras().getInt(Constants.CURRENT_RECIPE_POSITION_KEY_WIDGET);
            List<Recipe> recipes = intent.getParcelableArrayListExtra(Constants.ALL_RECIPES_KEY_WIDGET);
            int newRecipePosition = position;
            if (position > 0) {
                newRecipePosition--;
            }

            BakingAppWidget.updateRecipeWidgets(this, appWidgetManager, appWidgetIds, recipes, newRecipePosition);
            // If action = ACTION_NEXT_RECIPE move to next Recipe and update widget
        } else if (intent.getAction().equals(Constants.ACTION_NEXT_RECIPE)) {
            int position = intent.getExtras().getInt(Constants.CURRENT_RECIPE_POSITION_KEY_WIDGET);
            List<Recipe> recipes = intent.getParcelableArrayListExtra(Constants.ALL_RECIPES_KEY_WIDGET);
            int newRecipePosition = position;
            if (position < recipes.size() - 1) {
                newRecipePosition++;
            }
            BakingAppWidget.updateRecipeWidgets(this, appWidgetManager, appWidgetIds, recipes, newRecipePosition);
        }
    }
}


package com.udacity.heather.timetobake.widget;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.google.gson.Gson;
import com.udacity.heather.timetobake.utilities.Constants;
import com.udacity.heather.timetobake.models.Ingredient;
import com.udacity.heather.timetobake.models.Recipe;

import java.util.List;

public class BakingAppWidgetService extends IntentService {

    public BakingAppWidgetService() {
        super("BakingAppWidgetService");

    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingAppWidgetProvider.class));
        // If action = ACTION_PREVIOUS_RECIPE move to previous Recipe and update widget
        if (intent.getAction().equals(Constants.ACTION_PREVIOUS_RECIPE)) {
            int position = intent.getExtras().getInt(Constants.CURRENT_RECIPE_ID_WIDGET);
            List<Recipe> recipes = intent.getParcelableArrayListExtra(Constants.ALL_RECIPES_KEY_WIDGET);
            int newRecipePosition = position;
            if (position > 0) {
                newRecipePosition--;
            }

            BakingAppWidgetProvider.updateWidgetRecipe(this, recipes, newRecipePosition, appWidgetManager, appWidgetIds);
            // If action = ACTION_NEXT_RECIPE move to next Recipe and update widget
        } else if (intent.getAction().equals(Constants.ACTION_NEXT_RECIPE)) {
            int position = intent.getExtras().getInt(Constants.CURRENT_RECIPE_ID_WIDGET);
            List<Recipe> recipes = intent.getParcelableArrayListExtra(Constants.ALL_RECIPES_KEY_WIDGET);
            int newRecipePosition = position;
            if (position < recipes.size() - 1) {
                newRecipePosition++;
            }
            BakingAppWidgetProvider.updateWidgetRecipe(this, recipes, newRecipePosition, appWidgetManager, appWidgetIds);
        }
    }
}
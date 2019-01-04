package com.udacity.heather.timetobake.widget;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.udacity.heather.timetobake.utilities.Constants;
import com.udacity.heather.timetobake.models.Recipe;

import java.util.List;

public class BakingAppWidgetService extends IntentService {

    public BakingAppWidgetService() {
        super("BakingAppWidgetService");

    }
@Override
public void onCreate() {
    super.onCreate();
    if (Build.VERSION.SDK_INT >= 26) {
        String CHANNEL_ID = "my_channel_01";
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                "TimeToBake service",
                NotificationManager.IMPORTANCE_DEFAULT);

        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).createNotificationChannel(channel);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("")
                .setContentText("").build();

        startForeground(1, notification);
    }
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
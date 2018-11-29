package com.udacity.heather.timetobake.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.gson.Gson;
import com.udacity.heather.timetobake.activities.RecipeStepActivity;
import com.udacity.heather.timetobake.models.Recipe;
import com.udacity.heather.timetobake.utilities.Constants;
import com.udacity.heather.timetobake.R;
import com.udacity.heather.timetobake.utilities.NetworkUtils;
import com.udacity.heather.timetobake.utilities.Preferences;
import com.udacity.heather.timetobake.utilities.RecipeLibraryCallback;
import com.udacity.heather.timetobake.utilities.RecipeLibraryManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidgetProvider extends AppWidgetProvider {

    static int currentRecipePosition = 0;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, List<Recipe> recipes, int newRecipePosition) {

        RemoteViews views;

        views = getSingleRecipeRemoteView(context, recipes,newRecipePosition,appWidgetId, appWidgetManager);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        loadRecipes(context, appWidgetManager, appWidgetIds);
    }

    private void loadRecipes(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
        if (NetworkUtils.isConnected(context)) {
            RecipeLibraryManager.getInstance().getRecipes(new RecipeLibraryCallback<List<Recipe>>() {
                @Override
                public void onResponse(final List<Recipe> recipes) {
                    if (recipes != null && recipes.size() > 0) {
                        updateWidgetRecipe(context, recipes,currentRecipePosition, appWidgetManager, appWidgetIds);
                    } else {
                        Toast.makeText(context, context.getResources().getString(R.string.no_data_to_display), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancel() {
                    Toast.makeText(context, context.getResources().getString(R.string.loading_canceled), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }

    public static void updateWidgetRecipe(Context context, List<Recipe> recipes , int newRecipePosition, AppWidgetManager appWidgetManager,
                                          int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, recipes, newRecipePosition);
        }
    }
        private static RemoteViews getSingleRecipeRemoteView (Context context, List < Recipe > recipes,int newRecipePosition,
        int appWidgetId, AppWidgetManager appWidgetManager){

            // newRecipePosition has value -1 when it has no changes after next or previous button is clicked
            if (newRecipePosition != -1) {
                currentRecipePosition = newRecipePosition;
                Preferences.saveCurrentRecipePosition(context, newRecipePosition);
            }
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget_list_item);
            Recipe currentRecipe = recipes.get(currentRecipePosition);

            if (currentRecipe != null) {
                // Intent to open current recipe details in RecipeActivity when clicked
                Intent appIntent = new Intent(context, RecipeStepActivity.class);
                appIntent.putExtra(Constants.CURRENT_RECIPE_ID_WIDGET, currentRecipe);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                // Construct the RemoteViews object

                views.setTextViewText(R.id.tv_recipe_name, currentRecipe.getName());
                // Widgets allow click handlers to only launch pending intents
                views.setOnClickPendingIntent(R.id.tv_recipe_name, pendingIntent);

                // Initialize the list view
                Intent intent = new Intent(context, BakingAppWidgetService.class);
                String extra = new Gson().toJson(recipes);
                intent.putExtra(Constants.ALL_RECIPES_KEY_WIDGET, extra);

                // Bind the remote adapter
                views.setRemoteAdapter(R.id.recipe_widget_listview, intent);
                // Instruct the widget manager to update the widget
                appWidgetManager.updateAppWidget(appWidgetId, views);
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.recipe_widget_listview);


                // Intent to handle previous recipe button function
                Intent previousRecipeIntent = new Intent(context, BakingAppWidgetService.class);
                previousRecipeIntent.setAction(Constants.ACTION_PREVIOUS_RECIPE);
                previousRecipeIntent.putExtra(Constants.CURRENT_RECIPE_ID_WIDGET, currentRecipePosition);
                previousRecipeIntent.putParcelableArrayListExtra(Constants.ALL_RECIPES_KEY_WIDGET, (ArrayList<? extends Parcelable>) recipes);
                PendingIntent previousRecipePendingIntent = PendingIntent.getService(context, 0, previousRecipeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                views.setOnClickPendingIntent(R.id.iv_previous_nav, previousRecipePendingIntent);

                // Intent to handle next recipe button function
                Intent nextRecipeIntent = new Intent(context, BakingAppWidgetService.class);
                nextRecipeIntent.setAction(Constants.ACTION_NEXT_RECIPE);
                nextRecipeIntent.putExtra(Constants.CURRENT_RECIPE_ID_WIDGET, currentRecipePosition);
                nextRecipeIntent.putParcelableArrayListExtra(Constants.ALL_RECIPES_KEY_WIDGET, (ArrayList<? extends Parcelable>) recipes);
                PendingIntent nextRecipePendingIntent = PendingIntent.getService(context, 0, nextRecipeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                views.setOnClickPendingIntent(R.id.iv_next_nav, nextRecipePendingIntent);
            }
            return views;
        }
    }
package com.udacity.heather.timetobake.utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

    public static void saveCurrentRecipePosition(Context context, int currentRecipePosition) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(Constants.PREFERENCE, Context.MODE_PRIVATE).edit();
        prefs.putInt(Constants.CURRENT_RECIPE_ID_WIDGET, currentRecipePosition);
        prefs.apply();
    }

    public static int getCurrentRecipePosition(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFERENCE, Context.MODE_PRIVATE);
        return prefs.getInt(Constants.CURRENT_RECIPE_ID_WIDGET, 0);
    }
}


package com.udacity.heather.timetobake.widget;

import android.content.Context;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.heather.timetobake.R;
import com.udacity.heather.timetobake.models.Ingredient;
import com.udacity.heather.timetobake.models.Recipe;
import com.udacity.heather.timetobake.utilities.Preferences;

import java.util.ArrayList;
import java.util.List;


public class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        private Context context;
        private static List<Ingredient> ingredients = new ArrayList<>();
        private int currentRecipePosition = 0;
        private List<Recipe> recipes;


        WidgetRemoteViewsFactory(Context applicationContext, String recipeString) {
            context = applicationContext;
            Recipe currentRecipe = recipes.get(currentRecipePosition);
            ingredients = currentRecipe.getIngredients();
        }

        @Override
        public void onCreate() {
        }

        @Override
        public void onDataSetChanged() {
            currentRecipePosition =Preferences.getCurrentRecipePosition(context);
            Recipe currentRecipe = recipes.get(currentRecipePosition);
            ingredients = currentRecipe.getIngredients();
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            if (ingredients == null) return 0;
            return ingredients.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            if (ingredients == null || ingredients.size() == 0) return null;
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget_list_item);
            views.setTextViewText(R.id.tv_ingredient_widget, ingredients.get(position).getIngredient());
            views.setTextViewText(R.id.tv_quantity_widget, String.valueOf(ingredients.get(position).getQuantity()));
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
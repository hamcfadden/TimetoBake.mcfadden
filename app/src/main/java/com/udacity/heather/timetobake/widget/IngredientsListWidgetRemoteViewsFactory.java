package com.udacity.heather.timetobake.widget;

import android.content.Context;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.heather.timetobake.R;
import com.udacity.heather.timetobake.models.Ingredient;
import com.udacity.heather.timetobake.models.Recipe;
import com.udacity.heather.timetobake.utilities.JsonUtils;
import com.udacity.heather.timetobake.utilities.Preferences;

import java.util.List;


public class IngredientsListWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
            Context mContext;
            List<Ingredient> ingredientList;
    private int mCurrentRecipePosition = 0;
    private List<Recipe> mRecipes;

    IngredientsListWidgetRemoteViewsFactory(Context applicationContext, String recipesStr) {
        mContext = applicationContext;
        mRecipes = JsonUtils.extractRecipesData(recipesStr);
        Recipe currentRecipe = mRecipes.get(mCurrentRecipePosition);
        ingredientList = currentRecipe.getIngredients();
    }


    @Override
    public void onCreate() {
    }


    @Override
    public void onDataSetChanged() {
        mCurrentRecipePosition = Preferences.getCurrentRecipePosition(mContext);
        Recipe currentRecipe = mRecipes.get(mCurrentRecipePosition);
        ingredientList = currentRecipe.getIngredients();




        }


        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            if (ingredientList == null) return 0;
            return ingredientList.size();


        }

        @Override
        public RemoteViews getViewAt(int position) {
            if (ingredientList == null || ingredientList.size() == 0) {
                return null;
        }

            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.ingredient_list_item);
            views.setTextViewText(R.id.tv_ingredient_name, ingredientList.get(position).getIngredient());
            views.setTextViewText(R.id.tv_ingredient_quantity, String.valueOf(ingredientList.get(position).getQuantity()));
            views.setTextViewText(R.id.tv_ingredient_measure, ingredientList.get(position).getMeasure());

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
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }

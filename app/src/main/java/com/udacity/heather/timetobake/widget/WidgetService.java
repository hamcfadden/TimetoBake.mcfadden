package com.udacity.heather.timetobake.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.udacity.heather.timetobake.utilities.Constants;

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        String recipesStr = intent.getStringExtra(Constants.ALL_RECIPES_KEY_WIDGET);
        return new IngredientsListWidgetRemoteViewsFactory(this.getApplicationContext(), recipesStr);
    }
}

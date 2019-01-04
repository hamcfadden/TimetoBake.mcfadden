package com.udacity.heather.timetobake.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class WidgetRemoteViewsFactoryService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetRemoteViewsFactory(this.getApplicationContext());
    }
}
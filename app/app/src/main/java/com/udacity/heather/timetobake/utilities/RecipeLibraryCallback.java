package com.udacity.heather.timetobake.utilities;

public interface RecipeLibraryCallback<T> {
    void onResponse(T result);

    void onCancel();
}

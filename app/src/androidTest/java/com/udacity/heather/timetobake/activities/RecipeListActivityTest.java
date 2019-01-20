package com.udacity.heather.timetobake.activities;


import com.udacity.heather.timetobake.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.action.ViewActions;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(androidx.test.ext.junit.runners.AndroidJUnit4.class)
@LargeTest
public class RecipeListActivityTest {

    @Rule
    public ActivityTestRule<RecipeListActivity> mActivityTestRule =
            new ActivityTestRule<>(RecipeListActivity.class);


    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {

        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }
    @Before
    public void launchActivity() {
        ActivityScenario.launch(RecipeListActivity.class);
    }

    @Test
    public void clickRecipeListItem() {

        onView(withId(R.id.rv_recipe_list))
                .perform(ViewActions.click())
                .check((view, noViewFoundException) -> isDisplayed());
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }
}


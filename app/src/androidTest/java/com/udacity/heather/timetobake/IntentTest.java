package com.udacity.heather.timetobake;

import com.udacity.heather.timetobake.activities.RecipeListActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.matcher.ViewMatchers.withId;



    @RunWith(AndroidJUnit4.class)
    public class IntentTest {

        private static final String EXTRA_RECIPE_ID = "id";
        private static final int DEFAULT_RECIPE_ID = 1;

        @Rule
        public IntentsTestRule<RecipeListActivity> intentsTestRule =
                new IntentsTestRule<>(RecipeListActivity.class);

        @Test
        public void clickOnRecyclerViewItem_runRecipeStepsIntent() {
            onView(withId(R.id.rv_recipe_list))
                    .perform(ViewActions.click());

            intended(
                    hasExtra(EXTRA_RECIPE_ID, DEFAULT_RECIPE_ID)
            );
        }
    }

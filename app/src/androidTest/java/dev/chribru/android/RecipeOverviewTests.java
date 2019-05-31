package dev.chribru.android;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import dev.chribru.android.activities.overview.RecipeOverviewActivity;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class RecipeOverviewTests {
    @Rule
    public ActivityScenarioRule<RecipeOverviewActivity> activityScenarioRule =
            new ActivityScenarioRule<>(RecipeOverviewActivity.class);

    @Test
    public void clickRecipe_RecipeDetailOpens() {
        String recipeName = "Brownies";

        // click on Brownies recipe
        Espresso.onView(withText(recipeName)).perform(click());

        // check Recipe detail screen is displayed
        Espresso.onView(withId(R.id.recipe_activity_layout)).check(ViewAssertions.matches(isDisplayed()));
    }
}

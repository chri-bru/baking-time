package dev.chribru.android;

import org.junit.Rule;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import dev.chribru.android.activities.recipe.RecipeActivity;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class RecipeDetailTests {
    @Rule
    public ActivityScenarioRule<RecipeActivity> activityScenarioRule =
            new ActivityScenarioRule<>(RecipeActivity.class);

}

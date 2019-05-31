package dev.chribru.android;

import android.content.Context;
import android.content.Intent;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import dev.chribru.android.activities.recipe.RecipeActivity;
import dev.chribru.android.activities.recipe.RecipeDetailFragment;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeRight;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class RecipeDetailTests {

    private static final int RECIPE_ID = 1;

    @Rule
    public ActivityTestRule<RecipeActivity> activityTestRule =
            new ActivityTestRule<RecipeActivity>(RecipeActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation()
                            .getTargetContext();
                    Intent result = new Intent(targetContext, RecipeActivity.class);
                    result.putExtra(RecipeDetailFragment.ARG_ITEM_ID, String.valueOf(RECIPE_ID));
                    return result;
                }
            };

    @Test
    public void clickOnStepList_StepDetailsOpen_checkViewPager() {
        Espresso.onView(withId(R.id.step_list)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        // check initial item on step detail page
        Espresso.onView(allOf(withId(R.id.step_detail_number), isDisplayed())).check(ViewAssertions.matches(isDisplayed()));
        Espresso.onView(allOf(withId(R.id.step_detail_title), isDisplayed())).check(ViewAssertions.matches(withText("Starting prep")));
        Espresso.onView(allOf(withId(R.id.step_detail_number), isDisplayed())).check(ViewAssertions.matches(withText("1")));
        Espresso.onView(withId(R.id.left_arrow)).check(ViewAssertions.matches(isDisplayed()));
        Espresso.onView(withId(R.id.right_arrow)).check(ViewAssertions.matches(isDisplayed()));

        // check view pager behaviour: swipe right: go to step 0
        Espresso.onView(withId(R.id.step_pager)).perform(swipeRight());
        Espresso.onView(allOf(withId(R.id.step_detail_title), isDisplayed())).check(ViewAssertions.matches(withText("Recipe Introduction")));
        Espresso.onView(allOf(withId(R.id.step_detail_number), isDisplayed())).check(ViewAssertions.matches(withText("0")));
        Espresso.onView(withId(R.id.left_arrow)).check(ViewAssertions.matches(not(isDisplayed())));
        Espresso.onView(withId(R.id.right_arrow)).check(ViewAssertions.matches(isDisplayed()));

        // check arrow right click: navigate back to step 1
        Espresso.onView(withId(R.id.right_arrow)).perform(click());
        Espresso.onView(allOf(withId(R.id.step_detail_title), isDisplayed())).check(ViewAssertions.matches(withText("Starting prep")));
        Espresso.onView(allOf(withId(R.id.step_detail_number), isDisplayed())).check(ViewAssertions.matches(withText("1")));
        Espresso.onView(withId(R.id.left_arrow)).check(ViewAssertions.matches(isDisplayed()));
        Espresso.onView(withId(R.id.right_arrow)).check(ViewAssertions.matches(isDisplayed()));

        // check arrow left click: navigate back to step 0
        Espresso.onView(withId(R.id.left_arrow)).perform(click());
        Espresso.onView(allOf(withId(R.id.step_detail_title), isDisplayed())).check(ViewAssertions.matches(withText("Recipe Introduction")));
        Espresso.onView(allOf(withId(R.id.step_detail_number), isDisplayed())).check(ViewAssertions.matches(withText("0")));
        Espresso.onView(withId(R.id.left_arrow)).check(ViewAssertions.matches(not(isDisplayed())));
        Espresso.onView(withId(R.id.right_arrow)).check(ViewAssertions.matches(isDisplayed()));

    }
}

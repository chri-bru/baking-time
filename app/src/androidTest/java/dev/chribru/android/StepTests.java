package dev.chribru.android;

import org.junit.Rule;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import dev.chribru.android.activities.step.StepActivity;

@RunWith(AndroidJUnit4.class)
public class StepTests {

    @Rule
    public ActivityScenarioRule<StepActivity> activityActivityScenarioRule =
            new ActivityScenarioRule<StepActivity>(StepActivity.class);
}

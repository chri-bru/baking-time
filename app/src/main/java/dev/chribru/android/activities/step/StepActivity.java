package dev.chribru.android.activities.step;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import dev.chribru.android.R;
import dev.chribru.android.data.models.Step;

public class StepActivity extends AppCompatActivity {

    public static String ARG_STEP_ID = "step_id";
    public static String ARG_RECIPE_ID = "recipe_id";

    private StepsViewModel viewModel;

    private ViewPager pager;
    private PagerAdapter adapter;
    private int recipeId;
    private int stepId;
    private ImageView arrowForward;
    private ImageView arrowBack;
    private int numberOfSteps;
    private TextView stepCounterTv;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        viewModel = ViewModelProviders.of(this).get(StepsViewModel.class);
        pager = findViewById(R.id.step_pager);

        if (getIntent().hasExtra(ARG_RECIPE_ID) && getIntent().hasExtra(ARG_STEP_ID)) {
            recipeId = getIntent().getIntExtra(ARG_RECIPE_ID, 0);
            stepId = getIntent().getIntExtra(ARG_STEP_ID, 0);
            viewModel.setCurrentStepId(stepId);
        } else {
            // e.g. device rotation happened
            recipeId = viewModel.getRecipeId();
            stepId = viewModel.getCurrentStepId();
        }

        viewModel.getSteps(recipeId).observe(this, this::updateUi);
    }

    private void updateUi(List<Step> steps) {
        if (steps == null) {
            return;
        }

        viewModel.setSteps(steps);
        adapter = new StepPagerAdapter(getSupportFragmentManager(), steps);
        pager.setAdapter(adapter);
        pager.setCurrentItem(stepId, true);

        // update bottom bar
        stepCounterTv = findViewById(R.id.current_step_indicator);
        arrowForward = findViewById(R.id.right_arrow);
        arrowBack = findViewById(R.id.left_arrow);
        numberOfSteps = steps.size() -1;

        stepCounterTv.setText(String.valueOf(stepId) + "/" + String.valueOf(numberOfSteps));
        toggleArrowVisibility();

        arrowForward.setOnClickListener(v -> {
            setStepId(++stepId);
            updateStepCounter();
        });
        arrowBack.setOnClickListener(v -> {
            setStepId(--stepId);
            updateStepCounter();
        });

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setStepId(position);
                updateStepCounter();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void updateStepCounter() {
        pager.setCurrentItem(stepId, true);
        viewModel.setCurrentStepId(stepId);
        stepCounterTv.setText(String.valueOf(stepId) + "/" + String.valueOf(numberOfSteps));
        toggleArrowVisibility();
    }

    private void toggleArrowVisibility() {
        if (stepId == 0) {
            arrowBack.setVisibility(View.INVISIBLE);
        } else {
            arrowBack.setVisibility(View.VISIBLE);
        }
        if (stepId == numberOfSteps) {
            arrowForward.setVisibility(View.INVISIBLE);
        }
    }

    private void setStepId(int newStepId) {
        this.stepId = newStepId;
        viewModel.setCurrentStepId(stepId);
    }

    @Override
    public void onBackPressed() {
        if (pager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            pager.setCurrentItem(pager.getCurrentItem() - 1);
        }
    }

    private class StepPagerAdapter extends FragmentStatePagerAdapter {
        private List<Step> steps;

        public StepPagerAdapter(@NonNull FragmentManager fm, List<Step> steps) {
            super(fm, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            this.steps = steps;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            StepFragment fragment = new StepFragment();

            Bundle args = new Bundle();
            args.putInt(StepFragment.ARG_STEP_NUMBER, position);
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public int getCount() {
            return steps == null ? 0 : steps.size();
        }
    }

}

package dev.chribru.android.activities.recipe;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import dev.chribru.android.R;
import dev.chribru.android.activities.overview.RecipeOverviewActivity;
import dev.chribru.android.activities.step.StepFragment;
import dev.chribru.android.data.models.Recipe;
import dev.chribru.android.data.models.Step;

import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RecipeOverviewActivity}.
 */
public class RecipeActivity extends AppCompatActivity implements OnStepClickListener, StepFragment.IStepProvider {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    // in memory vars for book keeping
    private RecipeViewModel viewModel;
    private int recipeId;
    private Recipe currentRecipe;
    private Step selectedStep;

    private StepRecyclerViewAdapter adapter;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        viewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        RecipeViewModel viewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);

        if (getIntent().hasExtra(RecipeDetailFragment.ARG_ITEM_ID)) {
            recipeId = getIntent().getIntExtra(RecipeDetailFragment.ARG_ITEM_ID, 1);
            viewModel.select(recipeId);
        } else {
            recipeId = viewModel.getSelectedId();
        }

        if (mTwoPane) {
            // do nothing, handled on click
            RecyclerView stepView = findViewById(R.id.step_list);
            adapter = new StepRecyclerViewAdapter(new ArrayList<>(), this);
            stepView.setAdapter(adapter);

            viewModel.getSelected().observe(this, this::populateUiForTablet);

        } else {
            // savedInstanceState is non-null when there is fragment state
            // saved from previous configurations of this activity
            // (e.g. when rotating the screen from portrait to landscape).
            // In this case, the fragment will automatically be re-added
            // to its container so we don't need to manually add it.
            // For more information, see the Fragments API guide at:
            //
            // http://developer.android.com/guide/components/fragments.html
            //
            if (savedInstanceState == null) {
                viewModel.getSelected().observe(this, this::populateUi);
                // Create the detail fragment and add it to the activity
                // using a fragment transaction.
                RecipeDetailFragment fragment = new RecipeDetailFragment();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.recipe_container, fragment)
                        .commit();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recipe_detail_menu, menu);
        this.menu = menu;
        updateMenuIcon();
        return true;
    }

    private void populateUiForTablet(Recipe recipe) {
        if (recipe == null) {
            return;
        }

        currentRecipe = recipe;
        updateMenuIcon();
        adapter.setSteps(recipe.getSteps());
    }

    private void populateUi(Recipe recipe) {
        if (recipe == null) {
            return;
        }

        currentRecipe = recipe;
        updateMenuIcon();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case android.R.id.home:
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back
                //
                navigateUpTo(new Intent(this, RecipeOverviewActivity.class));
                return true;
            case R.id.add_to_widget:
                if (currentRecipe.isShowInAppWidget()) {
                    currentRecipe.setShowInAppWidget(false);
                } else {
                    currentRecipe.setShowInAppWidget(true);
                }
                viewModel.updateRecipe(currentRecipe);
                updateMenuIcon();
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateMenuIcon() {
        if (menu == null || currentRecipe == null) {
            return;
        }

        int iconId = currentRecipe.isShowInAppWidget() ?
                R.drawable.ic_star_black_24dp :
                R.drawable.ic_star_border_black_24dp;

        menu.getItem(0).setIcon(ContextCompat.getDrawable(this, iconId));
    }

    @Override
    public void OnClick(Step step) {
        // Create the detail fragment and add it to the activity
        // using a fragment transaction.
        StepFragment fragment = new StepFragment();
        selectedStep = step;
        fragment.setStepProvider(this);
        Bundle args = new Bundle();
        args.putInt(StepFragment.ARG_STEP_NUMBER, step.getId());
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.item_detail_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public Step getStep() {
        return selectedStep;
    }

    @Override
    public Step get(int id) {
        return selectedStep;
    }
}

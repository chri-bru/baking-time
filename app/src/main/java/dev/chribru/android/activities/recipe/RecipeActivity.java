package dev.chribru.android.activities.recipe;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;
import androidx.lifecycle.ViewModelProviders;
import dev.chribru.android.R;
import dev.chribru.android.activities.overview.RecipeOverviewActivity;

import android.view.MenuItem;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RecipeOverviewActivity}.
 */
public class RecipeActivity extends AppCompatActivity {

    private int recipeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

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
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            RecipeDetailFragment fragment = new RecipeDetailFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.item_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, RecipeOverviewActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

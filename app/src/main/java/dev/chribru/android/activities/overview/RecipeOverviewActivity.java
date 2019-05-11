package dev.chribru.android.activities.overview;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import dev.chribru.android.activities.recipe.RecipeActivity;
import dev.chribru.android.R;
import dev.chribru.android.activities.recipe.RecipeDetailFragment;
import dev.chribru.android.data.models.Recipe;

import java.util.List;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeOverviewActivity extends AppCompatActivity implements OnRecipeClickHandler {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private SimpleItemRecyclerViewAdapter adapter;
    private List<Recipe> recipes ;
    private OverviewViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_overview);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        viewModel = ViewModelProviders.of(this).get(OverviewViewModel.class);
        viewModel.getAll().observe(this, this::setRecipes);

        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        adapter = new SimpleItemRecyclerViewAdapter(recipes, mTwoPane, this);
        recyclerView.setAdapter(adapter);
    }

    private void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
        this.adapter.setRecipes(recipes);
    }

    @Override
    public void onClick(Recipe recipe) {
        // TODO add content provider

        // TODO: tablet layout
        if (mTwoPane) {
            // TODO add bundle with recipe id
            RecipeDetailFragment fragment = new RecipeDetailFragment();
            this.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit();
        } else {
            // start recipe activity passing in the ID of the recipe
            Intent intent = new Intent(this, RecipeActivity.class);
            intent.putExtra(RecipeDetailFragment.ARG_ITEM_ID, recipe.getId());
            this.startActivity(intent);
        }
    }
}

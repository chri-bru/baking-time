package dev.chribru.android.activities.overview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dev.chribru.android.R;
import dev.chribru.android.activities.recipe.RecipeActivity;
import dev.chribru.android.activities.recipe.RecipeDetailFragment;
import dev.chribru.android.data.models.Recipe;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeOverviewActivity extends AppCompatActivity implements OnRecipeClickHandler {

    private SimpleItemRecyclerViewAdapter adapter;
    private List<Recipe> recipes ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_overview);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        OverviewViewModel viewModel = ViewModelProviders.of(this).get(OverviewViewModel.class);
        viewModel.getAll().observe(this, this::setRecipes);

        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        adapter = new SimpleItemRecyclerViewAdapter(recipes, this);
        // tablet layout
        if (recyclerView.getLayoutManager() == null) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 4, RecyclerView.VERTICAL, false));
        }
        recyclerView.setAdapter(adapter);
    }

    private void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
        this.adapter.setRecipes(recipes);
    }

    @Override
    public void onClick(Recipe recipe) {
        // start recipe activity passing in the ID of the recipe
        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra(RecipeDetailFragment.ARG_ITEM_ID, recipe.getId());
        this.startActivity(intent);
    }
}

package dev.chribru.android.activities.recipe;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProviders;
import dev.chribru.android.R;
import dev.chribru.android.activities.overview.RecipeOverviewActivity;
import dev.chribru.android.data.models.Recipe;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link RecipeOverviewActivity}
 * in two-pane mode (on tablets) or a {@link RecipeActivity}
 * on handsets.
 */
public class RecipeDetailFragment extends Fragment {
    /**
     * The item id for the recipe to be used for extras
     */
    public static String ARG_ITEM_ID = "item_id";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RecipeViewModel viewModel = ViewModelProviders.of(getActivity()).get(RecipeViewModel.class);
        viewModel.getSelected().observe(this, this::updateUi);
    }

    private void updateUi(Recipe recipe) {
        if (recipe == null) {
            return;
        }

        CollapsingToolbarLayout appBarLayout = this.getActivity().findViewById(R.id.toolbar_layout);

        if (appBarLayout != null) {
            appBarLayout.setTitle(recipe.getName());
        }

        if (recipe != null) {
            if (!TextUtils.isEmpty(recipe.getImage())) {
                // set image to recipe image (no recipe has an image, so disregard for now)
            }

            // TODO add adapter
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_detail, container, false);
        return rootView;
    }
}

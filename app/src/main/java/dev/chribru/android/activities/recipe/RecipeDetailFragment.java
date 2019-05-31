package dev.chribru.android.activities.recipe;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import dev.chribru.android.R;
import dev.chribru.android.activities.overview.RecipeOverviewActivity;
import dev.chribru.android.activities.step.StepActivity;
import dev.chribru.android.data.models.Recipe;
import dev.chribru.android.data.models.Step;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link RecipeOverviewActivity}
 * in two-pane mode (on tablets) or a {@link RecipeActivity}
 * on handsets.
 */
public class RecipeDetailFragment extends Fragment implements OnStepClickListener{
    /**
     * The item id for the recipe to be used for extras
     */
    public static final String ARG_ITEM_ID = "item_id";

    private int recipeId;
    private RecyclerView stepsView;
    private RecyclerView ingredientView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeDetailFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = this.getArguments();

        RecipeViewModel viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()))
                .get(RecipeViewModel.class);

        if (bundle != null) {
            recipeId = bundle.getInt(ARG_ITEM_ID);
            viewModel.select(recipeId);
        }

        viewModel.getSelected().observe(this, this::updateUi);
    }

    private void updateUi(Recipe recipe) {
        if (recipe == null) {
            return;
        }

        recipeId = recipe.getId();

        CollapsingToolbarLayout appBarLayout = this.getActivity().findViewById(R.id.toolbar_layout);

        if (appBarLayout != null) {
            appBarLayout.setTitle(recipe.getName());
        }

        if (!TextUtils.isEmpty(recipe.getImage())) {
            // set image to recipe image (no recipe has an image, so disregard for now)
        }

        assert stepsView != null;
        StepRecyclerViewAdapter stepAdapter = new StepRecyclerViewAdapter(recipe.getSteps(), this);
        stepsView.setAdapter(stepAdapter);

        assert ingredientView != null;
        IngredientRecyclerViewAdapter ingredientAdapter = new IngredientRecyclerViewAdapter(recipe.getIngredients());
        ingredientView.setAdapter(ingredientAdapter);

    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_detail, container, false);
        stepsView = rootView.findViewById(R.id.step_list);
        ingredientView= rootView.findViewById(R.id.ingredient_list);
        return rootView;
    }

    @Override
    public void OnClick(Step step) {
        Intent intent = new Intent(getActivity(), StepActivity.class);
        intent.putExtra(StepActivity.ARG_RECIPE_ID, recipeId);
        intent.putExtra(StepActivity.ARG_STEP_ID, step.getId());
        this.startActivity(intent);
    }
}

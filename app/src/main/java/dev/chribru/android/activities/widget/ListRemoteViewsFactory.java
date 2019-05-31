package dev.chribru.android.activities.widget;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.List;

import dev.chribru.android.R;
import dev.chribru.android.activities.recipe.RecipeActivity;
import dev.chribru.android.activities.recipe.RecipeDetailFragment;
import dev.chribru.android.data.RecipeRepository;
import dev.chribru.android.data.models.Ingredient;
import dev.chribru.android.data.models.Recipe;

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private final Context context;
    private final RecipeRepository repository;
    private List<Recipe> recipes;

    public ListRemoteViewsFactory(Context appContext) {
        context = appContext;
        repository = RecipeRepository.getInstance(context);
    }

    @Override
    public void onCreate() {
    }

    //called on start and when notifyAppWidgetViewDataChanged is called
    @Override
    public void onDataSetChanged() {
        recipes = repository.loadRecipes();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        // no recipe selected
        if (recipes == null) {
            return 0;
        }

        // only 1 recipe selected, show ingredients
        if (recipes.size() == 1) {
            return recipes.get(0).getIngredients() != null ? recipes.get(0).getIngredients().size() : 0;
        }

        // multiple recipes selected: show thumbnails
        return recipes.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (recipes == null) {
            return null;
        }

        if (recipes.size() == 1) {
            return displayIngredients(position);
        }

        return displayThumbnails(position);
    }

    private RemoteViews displayIngredients(int position) {
        Recipe selectedRecipe = recipes.get(0);

        if (selectedRecipe.getIngredients() == null || selectedRecipe.getIngredients().size() == 0) {
            return null;
        }

        Ingredient ingredient = selectedRecipe.getIngredients().get(position);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.home_widget_item);
        views.setTextViewText(R.id.ingredient_quantity, ingredient.getQuantityAsString());
        views.setTextViewText(R.id.ingredient_measure, ingredient.getMeasure());
        views.setTextViewText(R.id.ingredient_name, ingredient.getName());
        views.setViewVisibility(R.id.ingredient_quantity, View.VISIBLE);
        views.setViewVisibility(R.id.ingredient_measure, View.VISIBLE);
        views.setViewVisibility(R.id.ingredient_name, View.VISIBLE);

        // set click listener to container to launch the detail activity
        PendingIntent pendingIntent = getPendingIntentForRecipe(selectedRecipe.getId());
        views.setOnClickPendingIntent(R.id.ingredient_name, pendingIntent);
        views.setOnClickPendingIntent(R.id.ingredient_measure, pendingIntent);
        views.setOnClickPendingIntent(R.id.ingredient_quantity, pendingIntent);

        // disable the recipe view
        views.setViewVisibility(R.id.recipe_name, View.GONE);

        return views;
    }

    private RemoteViews displayThumbnails(int position) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.home_widget_item);

        views.setViewVisibility(R.id.recipe_name, View.VISIBLE);
        views.setTextViewText(R.id.recipe_name, recipes.get(position).getName());

        // disable all ingredient related views
        views.setViewVisibility(R.id.ingredient_quantity, View.GONE);
        views.setViewVisibility(R.id.ingredient_measure, View.GONE);
        views.setViewVisibility(R.id.ingredient_name, View.GONE);

        // set click listener to container to launch the detail activity
        PendingIntent pendingIntent = getPendingIntentForRecipe(recipes.get(position).getId());
        views.setOnClickPendingIntent(R.id.recipe_name, pendingIntent);


        return views;
    }

    private PendingIntent getPendingIntentForRecipe(int recipeId) {
        Intent intent = new Intent(context, RecipeActivity.class);
        intent.putExtra(RecipeDetailFragment.ARG_ITEM_ID, recipeId);
        return PendingIntent.getActivity(context, 0, intent, 0);
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}

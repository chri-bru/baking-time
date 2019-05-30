package dev.chribru.android.activities.widget;

import android.content.Context;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import dev.chribru.android.R;
import dev.chribru.android.data.RecipeRepository;
import dev.chribru.android.data.models.Ingredient;
import dev.chribru.android.data.models.Recipe;

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private final Context context;
    private final RecipeRepository repository;
    private Recipe recipe;

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
        recipe = repository.loadRecipe(1);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return recipe != null && recipe.getIngredients() != null ? recipe.getIngredients().size() : 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (recipe == null || recipe.getIngredients() == null) {
            return null;
        }

        Ingredient ingredient = recipe.getIngredients().get(position);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.home_widget_item);
        views.setTextViewText(R.id.ingredient_quantity, ingredient.getQuantityAsString());
        views.setTextViewText(R.id.ingredient_measure, ingredient.getMeasure());
        views.setTextViewText(R.id.ingredient_name, ingredient.getName());

        return views;
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

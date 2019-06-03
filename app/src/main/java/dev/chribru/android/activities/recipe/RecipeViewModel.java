package dev.chribru.android.activities.recipe;

import android.app.Application;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import dev.chribru.android.activities.widget.BakingTimeWidgetProvider;
import dev.chribru.android.data.RecipeRepository;
import dev.chribru.android.data.models.Recipe;

public class RecipeViewModel extends AndroidViewModel {
    private final RecipeRepository repository;

    private int selectedId;
    private final Application app;

    public RecipeViewModel(@NonNull Application application) {
        super(application);
        app = application;
        repository = RecipeRepository.getInstance(application.getApplicationContext());
    }

    /***
     * Marks the given recipe ID as selected (in the view model only)
     * @param id    the recipe ID to mark as selected
     */
    public void select(int id) {
        selectedId = id;
    }

    /***
     * Returns the currently selected recipe ID
     * @return  the currently selected recipe ID
     */
    public int getSelectedId() {
        return selectedId;
    }

    /***
     * Retrieves the currently selected recipe (based on its id)
     * from the repository
     * @return  the currently selected recipe
     */
    public LiveData<Recipe> getSelected() {
        return repository.get(selectedId);
    }

    /***
     * Updates the given recipe in the database
     * @param recipe    the recipe to update
     */
    public void updateRecipe(Recipe recipe) {
        repository.insertOrUpdateRecipe(recipe);

        // update app widget
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.setComponent(new ComponentName(app.getApplicationContext(), BakingTimeWidgetProvider.class));
        app.getApplicationContext().sendBroadcast(intent);
    }
}

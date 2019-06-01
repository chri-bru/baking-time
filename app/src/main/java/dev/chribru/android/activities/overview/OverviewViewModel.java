package dev.chribru.android.activities.overview;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import dev.chribru.android.data.RecipeRepository;
import dev.chribru.android.data.models.Recipe;

public class OverviewViewModel extends AndroidViewModel {
    private final RecipeRepository repository;

    public OverviewViewModel(Application application) {
        super(application);
        repository = RecipeRepository.getInstance(application.getApplicationContext());
    }

    /**
     * Returns all recipes from the repository
     * @return  all recipes in the repository
     */
    public LiveData<List<Recipe>> getAll() {
        return repository.getAll();
    }

    /**
     * Returns a specific recipe from the repository
     * @param id    the id of the recipe to return
     * @return      the recipe associated with the id
     */
    public LiveData<Recipe> get(int id) {
        return repository.get(id);
    }
}

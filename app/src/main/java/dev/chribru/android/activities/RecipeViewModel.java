package dev.chribru.android.activities;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import dev.chribru.android.data.RecipeRepository;
import dev.chribru.android.data.models.Recipe;

public class RecipeViewModel extends AndroidViewModel {
    private final RecipeRepository repository;

    // no recipe selected: id == -1
    private int selectedRecipe = -1;

    public RecipeViewModel(Application application) {
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

    /**
     * Temporarily saves a recipe selection
     * @param id    the id of the recipe to save
     */
    public void setSelectedRecipe(int id) {
        selectedRecipe = id;
    }

    /**
     * Returns the previously selected recipe.
     * @return      the selected recipe or null if none was selected
     */
    public LiveData<Recipe> getSelectedRecipe() {
        if (selectedRecipe < 0) {
            return null;
        }
        return get(selectedRecipe);
    }
}

package dev.chribru.android.activities.recipe;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
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

    public void select(int id) {
        selectedId = id;
    }

    public int getSelectedId() {
        return selectedId;
    }

    public LiveData<Recipe> getSelected() {
        return repository.get(selectedId);
    }
}

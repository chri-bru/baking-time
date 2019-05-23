package dev.chribru.android.activities.step;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import dev.chribru.android.data.RecipeRepository;
import dev.chribru.android.data.models.Step;

public class StepsViewModel extends AndroidViewModel {

    private final RecipeRepository repository;

    private List<Step> steps;
    private int numberOfSteps;

    public StepsViewModel(@NonNull Application application) {
        super(application);
        repository = RecipeRepository.getInstance(application.getApplicationContext());
    }

    public LiveData<List<Step>> getSteps(int recipeId) {
        return Transformations.map(repository.get(recipeId), recipe -> recipe.getSteps());
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
        this.numberOfSteps = steps.size();
    }

    public Step getStepInRecipe(int step) {
        return steps != null ? steps.get(step) : null;
    }

    public int getNumberOfSteps() {
        return numberOfSteps;
    }
}

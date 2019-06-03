package dev.chribru.android.activities.step;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import dev.chribru.android.data.RecipeRepository;
import dev.chribru.android.data.models.Recipe;
import dev.chribru.android.data.models.Step;

public class StepsViewModel extends AndroidViewModel {

    private final RecipeRepository repository;

    private List<Step> steps;
    private int recipeId;

    private int currentStepId;

    public StepsViewModel(@NonNull Application application) {
        super(application);
        repository = RecipeRepository.getInstance(application.getApplicationContext());
    }

    public LiveData<List<Step>> getSteps(int recipeId) {
        this.recipeId = recipeId;
        return Transformations.map(repository.get(recipeId), Recipe::getSteps);
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public Step getStepInRecipe(int step) {
        return steps != null ? steps.get(step) : null;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public int getCurrentStepId() {
        return currentStepId;
    }

    public void setCurrentStepId(int currentStepId) {
        this.currentStepId = currentStepId;
    }
}

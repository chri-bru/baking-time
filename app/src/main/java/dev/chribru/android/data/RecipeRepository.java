package dev.chribru.android.data;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.room.Room;
import dev.chribru.android.data.models.Recipe;
import dev.chribru.android.data.persistance.RecipeDao;
import dev.chribru.android.data.persistance.RecipeDatabase;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeRepository {
    private static volatile RecipeRepository instance;
    private final Executor executor;

    private final RecipeClient client;
    private final RecipeDao dao;

    private MutableLiveData<List<Recipe>> recipes = new MutableLiveData<>();

    private RecipeRepository(Context context) {
        client = new RecipeClient(context);
        executor = Executors.newSingleThreadExecutor();

        RecipeDatabase db = Room.databaseBuilder(context.getApplicationContext(),
                RecipeDatabase.class, "recipes").build();

        dao = db.dao();
    }

    public static RecipeRepository getInstance(Context context) {
        if (instance == null) {
            synchronized (RecipeRepository.class) {
                instance = new RecipeRepository(context);
            }
        }

        return instance;
    }

    /***
     * Loads all recipes for the widget.
     * @return      a list of recipes marked to be shown on the app widget
     */
    public List<Recipe> loadRecipes() {
        return dao.loadRecipesForWidget();
    }

    /***
     * Inserts or updates the given recipe in the database.
     * @param recipe    the recipe to insert/update
     */
    public void insertOrUpdateRecipe(Recipe recipe) {
        executor.execute(() -> dao.update(recipe));
    }

    /**
     * Returns all recipes found in Room.
     * @return  the list of recipes
     */
    public LiveData<List<Recipe>> getAll() {
        // data isn't in memory yet, retrieve it from the network
        if (recipes.getValue() == null) {
            refreshData();
            return dao.getAll();
        }
        return recipes;
    }

    /**
     * Returns a specific recipe based on id.
     * @param id    the unique ID of the recipe
     * @return      the recipe associated with the id
     */
    public LiveData<Recipe> get(int id) {
        // recipes aren't in memory yet, retrieve it from the DB
        if (recipes.getValue() == null) {
            return dao.get(id);
        }

        // mapping required as adapter position is 0 based, recipe IDs start at 1
        return Transformations.map(recipes, input -> {
            for (Recipe recipe : input) {
                if (recipe.getId() == id) {
                    return recipe;
                }
            }
            return null;
        });
    }

    private void refreshData() {
        new FetchRecipesTask().execute();
    }

    private class FetchRecipesTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            client.fetchRecipes().enqueue(new Callback<List<Recipe>>() {
                @Override
                public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                    if (response.isSuccessful()) {
                        recipes.setValue(response.body());
                        executor.execute(() -> dao.insert(response.body()));
                    }
                }

                @Override
                public void onFailure(Call<List<Recipe>> call, Throwable t) {
                    Log.w(this.getClass().getName(), "Recipe fetching failed!");
                }
            });
            return null;
        }
    }

}

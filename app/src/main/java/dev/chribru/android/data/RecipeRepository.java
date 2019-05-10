package dev.chribru.android.data;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.lifecycle.LiveData;
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

    private RecipeRepository(Context context) {
        client = new RecipeClient();
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

    /**
     * Returns all recipes found in Room.
     * @return  the list of recipes
     */
    public LiveData<List<Recipe>> getAll() {
        refreshData();
        return dao.getAll();
    }

    /**
     * Returns a specific recipe based on id.
     * @param id    the unique ID of the recipe
     * @return      the recipe associated with the id
     */
    public LiveData<Recipe> get(int id) {
        return dao.get(id);
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
                        executor.execute(() -> {
                            dao.insert(response.body());
                        });
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

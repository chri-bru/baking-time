package dev.chribru.android.data;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

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

    private final RecipeClient client;
    private final RecipeDao dao;

    private RecipeRepository(Context context) {
        client = new RecipeClient();

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
    public LiveData<List<Recipe>> getRecipes() {
        refreshData();
        return dao.getAll();
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
                        dao.insert(response.body());
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

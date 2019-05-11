package dev.chribru.android.data.persistance;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import dev.chribru.android.data.models.Recipe;

@Dao
public interface RecipeDao {

    @Query("SELECT * FROM recipe")
    LiveData<List<Recipe>> getAll();

    @Query("SELECT * FROM recipe WHERE id == :id")
    LiveData<Recipe> get(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE) // there might be an update, so replace it
    void insert(List<Recipe> recipes);

    @Delete
    void delete(Recipe recipe);
}

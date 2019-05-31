package dev.chribru.android.data.persistance;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import dev.chribru.android.data.models.Recipe;

@Dao
public interface RecipeDao {

    @Query("SELECT * FROM recipe")
    LiveData<List<Recipe>> getAll();

    @Query("SELECT * FROM recipe WHERE id == :id")
    LiveData<Recipe> get(int id);

    @Query("SELECT * FROM recipe WHERE showInAppWidget == 1")
    List<Recipe> loadRecipesForWidget();

    @Insert(onConflict = OnConflictStrategy.IGNORE) // there might be an update, so replace it
    void insert(List<Recipe> recipes);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Recipe recipe);

    @Delete
    void delete(Recipe recipe);
}

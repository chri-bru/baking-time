package dev.chribru.android.data.persistance;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import dev.chribru.android.data.models.Recipe;

@Dao
public interface RecipeDao {

    @Query("SELECT * FROM recipe")
    List<Recipe> getAll();

    @Insert
    void insert(Recipe... recipes);

    @Delete
    void delete(Recipe recipe);
}

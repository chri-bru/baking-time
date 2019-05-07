package dev.chribru.android.data.persistance;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import dev.chribru.android.data.models.Recipe;

@Database(entities = {Recipe.class}, version = 1)
public abstract class RecipeDatabase extends RoomDatabase {
    public abstract RecipeDao dao();
}

package dev.chribru.android.data.persistance;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import androidx.room.TypeConverter;
import dev.chribru.android.data.models.Ingredient;
import dev.chribru.android.data.models.Step;

public class ModelConverter {

    // Converters for Ingredients
    @TypeConverter
    public static List<Ingredient> fromStringToIngredients (String value) {
        Type type = new TypeToken<List<Ingredient>>() {}.getType();
        return new Gson().fromJson(value, type);
    }

    @TypeConverter
    public static String fromIngredientsToString (List<Ingredient> ingredients) {
        return new Gson().toJson(ingredients);
    }

    // Converters for Steps
    @TypeConverter
    public static List<Step> fromStringToSteps (String value) {
        Type type = new TypeToken<List<Step>>() {}.getType();
        return new Gson().fromJson(value, type);
    }

    @TypeConverter
    public static String fromStepsToString (List<Step> steps) {
        return new Gson().toJson(steps);
    }
}

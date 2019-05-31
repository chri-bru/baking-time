package dev.chribru.android.data;

import java.util.List;

import dev.chribru.android.data.models.Recipe;
import retrofit2.Call;
import retrofit2.http.GET;

interface IRecipeRetrievalService {

    @GET("baking.json")
    Call<List<Recipe>> getRecipes();
}

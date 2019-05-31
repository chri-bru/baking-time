package dev.chribru.android.data;

import android.content.Context;

import java.util.List;

import dev.chribru.android.data.models.Recipe;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class RecipeClient {
    private final IRecipeRetrievalService api;
    private final String baseUrl = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";

    public RecipeClient(Context context) {
        long cacheSize = (5 * 1024 * 1024); // 5mb
        Cache cache = new Cache(context.getCacheDir(), cacheSize);

        OkHttpClient client = new OkHttpClient.Builder()
                .cache(cache)
                .build();

        this.api = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(IRecipeRetrievalService.class);
    }

    /**
     * Gets all available recipes from the JSON hosted on Cloudfront
     * @return  the list of available recipes
     */
    public Call<List<Recipe>> fetchRecipes() {
        return api.getRecipes();
    }
}

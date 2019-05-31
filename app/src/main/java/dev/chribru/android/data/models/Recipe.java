package dev.chribru.android.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import dev.chribru.android.data.persistance.ModelConverter;

@Entity
@TypeConverters({ModelConverter.class})
public class Recipe {

    @SerializedName("id")
    @Expose
    @PrimaryKey
    private Integer id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("ingredients")
    @Expose
    private List<Ingredient> ingredients = null;

    @SerializedName("steps")
    @Expose
    private List<Step> steps = null;

    @SerializedName("servings")
    @Expose
    private Integer servings;

    @SerializedName("image")
    @Expose
    private String image;

    @Expose
    private boolean showInAppWidget;

    /**
     * No args constructor for use in serialization
     *
     */
    public Recipe() {
    }

    /**
     * Initializes a new recipe with the given arguments
     * @param ingredients   list of ingredients
     * @param id            id for the recipe (unique)
     * @param servings      number of servings
     * @param name          name of the recipe
     * @param image         image associated with the recipe (URL)
     * @param steps         steps required to make the recipe
     */
    public Recipe(Integer id, String name, List<Ingredient> ingredients, List<Step> steps, Integer servings, String image, boolean showInAppWidget) {
        super();
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
        this.image = image;
        this.showInAppWidget = showInAppWidget;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isShowInAppWidget() {
        return showInAppWidget;
    }

    public void setShowInAppWidget(boolean showInAppWidget) {
        this.showInAppWidget = showInAppWidget;
    }
}

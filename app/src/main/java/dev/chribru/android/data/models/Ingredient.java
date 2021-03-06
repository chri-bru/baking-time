package dev.chribru.android.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ingredient {

    @SerializedName("quantity")
    @Expose
    private float quantity;

    @SerializedName("measure")
    @Expose
    private String measure;

    @SerializedName("ingredient")
    @Expose
    private String name;

    /**
     * No args constructor for use in serialization
     *
     */
    public Ingredient() {
    }

    /**
     * Initializes a new ingredients with the given parameters
     * @param measure   measurement type (e.g. g, cup, ...)
     * @param name      name of the ingredient
     * @param quantity  quantity of the ingredient used for a recipe
     */
    public Ingredient(float quantity, String measure, String name) {
        super();
        this.quantity = quantity;
        this.measure = measure;
        this.name = name;
    }


    public float getQuantity() {
        return quantity;
    }

    public String getQuantityAsString() {
        return Float.toString(quantity);
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

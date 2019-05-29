package dev.chribru.android.activities.recipe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import dev.chribru.android.R;
import dev.chribru.android.data.models.Ingredient;

public class IngredientRecyclerViewAdapter extends RecyclerView.Adapter<IngredientRecyclerViewAdapter.IngredientViewHolder> {
    private List<Ingredient> ingredients;

    public IngredientRecyclerViewAdapter(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_recipe_ingredients_content, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        if (getItemCount() == 0) {
            return;
        }

        Ingredient ingredient = ingredients.get(position);
        holder.quantity.setText(ingredient.getQuantityAsString());

        String unit = ingredient.getMeasure().toLowerCase();
        if (unit.equals("unit")) {
            unit = "";
        }
        holder.unit.setText(unit);
        holder.name.setText(ingredient.getName());
    }

    @Override
    public int getItemCount() {
        return ingredients == null ? 0 : ingredients.size();
    }

    class IngredientViewHolder extends RecyclerView.ViewHolder {
        final TextView quantity;
        final TextView unit;
        final TextView name;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            quantity = itemView.findViewById(R.id.ingredient_quantity);
            unit = itemView.findViewById(R.id.ingredient_measure);
            name = itemView.findViewById(R.id.ingredient_name);
        }
    }
}

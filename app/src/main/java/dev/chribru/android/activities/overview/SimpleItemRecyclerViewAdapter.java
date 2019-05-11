package dev.chribru.android.activities.overview;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import dev.chribru.android.R;
import dev.chribru.android.data.models.Recipe;

public class SimpleItemRecyclerViewAdapter
        extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

    private final boolean mTwoPane;
    private List<Recipe> recipes;

    private final OnRecipeClickHandler mOnClickListener;

    SimpleItemRecyclerViewAdapter(List<Recipe> recipes,
                                  boolean twoPane,
                                  OnRecipeClickHandler onClickListener) {
        this.recipes = recipes;
        mTwoPane = twoPane;
        mOnClickListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.overview_item_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (recipes == null || recipes.size() == 0) {
            return;
        }
        Recipe recipe = recipes.get(position);

        if (TextUtils.isEmpty(recipe.getImage())) {
            // no image available; use generic display
            holder.image.setVisibility(View.GONE);
            holder.altDisplayContainer.setVisibility(View.VISIBLE);
            holder.recipeName.setText(recipe.getName());
        } else {
            // as the entire data set doesn't contain any image, disregard this case for now
        }

        holder.itemView.setOnClickListener(holder);
    }

    @Override
    public int getItemCount() {
        return recipes == null ? 0 : recipes.size();
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView image;
        final TextView recipeName;
        final LinearLayout altDisplayContainer;

        ViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.overview_recipe_img);
            altDisplayContainer = view.findViewById(R.id.id_container_alt_display);
            recipeName = view.findViewById(R.id.recipe_name);
        }

        @Override
        public void onClick(View v) {
            Recipe recipe = recipes.get(getAdapterPosition());
            mOnClickListener.onClick(recipe);
        }
    }
}

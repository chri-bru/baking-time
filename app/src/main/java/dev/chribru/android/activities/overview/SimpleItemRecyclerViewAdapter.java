package dev.chribru.android.activities.overview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import dev.chribru.android.R;
import dev.chribru.android.activities.RecipeActivity;
import dev.chribru.android.activities.fragments.RecipeDetailFragment;
import dev.chribru.android.data.models.Recipe;
import dev.chribru.android.dummy.DummyContent;

public class SimpleItemRecyclerViewAdapter
        extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

    private final RecipeOverviewActivity mParentActivity;
    private final boolean mTwoPane;
    private List<Recipe> recipes;

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DummyContent.DummyItem item = (DummyContent.DummyItem) view.getTag();
            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putString(RecipeDetailFragment.ARG_ITEM_ID, item.id);
                RecipeDetailFragment fragment = new RecipeDetailFragment();
                fragment.setArguments(arguments);
                mParentActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.item_detail_container, fragment)
                        .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, RecipeActivity.class);
                intent.putExtra(RecipeDetailFragment.ARG_ITEM_ID, item.id);

                context.startActivity(intent);
            }
        }
    };

    SimpleItemRecyclerViewAdapter(RecipeOverviewActivity parent,
                                  List<Recipe> recipes,
                                  boolean twoPane) {
        this.recipes = recipes;
        mParentActivity = parent;
        mTwoPane = twoPane;
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

        holder.itemView.setTag(recipe);
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return recipes == null ? 0 : recipes.size();
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView image;
        final TextView recipeName;
        final LinearLayout altDisplayContainer;

        ViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.overview_recipe_img);
            altDisplayContainer = view.findViewById(R.id.id_container_alt_display);
            recipeName = view.findViewById(R.id.recipe_name);
        }
    }
}

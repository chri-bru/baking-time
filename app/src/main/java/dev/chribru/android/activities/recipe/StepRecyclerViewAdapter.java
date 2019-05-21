package dev.chribru.android.activities.recipe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import dev.chribru.android.R;
import dev.chribru.android.data.models.Step;

public class StepRecyclerViewAdapter extends RecyclerView.Adapter<StepRecyclerViewAdapter.StepViewHolder> {

    private List<Step> steps;
    private final OnStepClickListener listener;

    StepRecyclerViewAdapter(List<Step> steps, OnStepClickListener listener) {
        this.steps = steps;
        this.listener = listener;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_steps_content, parent, false);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
        if (getItemCount() == 0) {
            return;
        }

        Step step = steps.get(position);
        holder.stepNumber.setText(step.getIdAsString());
        holder.stepDescription.setText(step.getShortDescription());
        holder.itemView.setOnClickListener(holder);
    }

    @Override
    public int getItemCount() {
        return steps == null ? 0 : steps.size();
    }

    class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView stepNumber;
        final TextView stepDescription;

        StepViewHolder(@NonNull View itemView) {
            super(itemView);
            stepNumber = itemView.findViewById(R.id.step_number);
            stepDescription = itemView.findViewById(R.id.step_title);
        }

        @Override
        public void onClick(View v) {
            listener.OnClick(steps.get(getAdapterPosition()));
        }
    }
}

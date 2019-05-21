package dev.chribru.android.activities.step;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import dev.chribru.android.R;
import dev.chribru.android.data.models.Step;

public class StepFragment extends Fragment {
    public static String ARG_STEP_NUMBER = "step_number";
    private int stepNo;
    private StepsViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = ViewModelProviders.of(getActivity()).get(StepsViewModel.class);

        if (getArguments() != null) {
            stepNo = getArguments().getInt(ARG_STEP_NUMBER);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.step_detail, container, false);

        TextView title = view.findViewById(R.id.step_detail_title);
        TextView description = view.findViewById(R.id.step_detail_description);

        Step step = viewModel.getStepInRecipe(stepNo);
        title.setText(step.getShortDescription());
        description.setText(step.getDescription());

        return view;
    }
}

package dev.chribru.android.activities.step;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import dev.chribru.android.R;
import dev.chribru.android.data.models.Step;

public class StepFragment extends Fragment {
    public static String ARG_STEP_NUMBER = "step_number";
    private int stepNo;
    private SimpleExoPlayer exoPlayer;

    private IStepProvider stepProvider;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        TextView stepNumber = view.findViewById(R.id.step_detail_number);
        PlayerView playerView = view.findViewById(R.id.exo_player);

        Step step = stepProvider.getStep();

        if (!TextUtils.isEmpty(step.getVideoURL())) {
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext());
            playerView.setPlayer(exoPlayer);
            prepareDataSource(step);
        } else {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                playerView.setVisibility(View.INVISIBLE);
            } else {
                playerView.setVisibility(View.GONE);
            }
        }

        title.setText(step.getShortDescription());

        if (!step.getShortDescription().equals(step.getDescription())) {
            description.setText(step.getDescription());
        }

        stepNumber.setText(String.valueOf(stepNo));
        return view;
    }

    private void prepareDataSource(Step step) {
        Uri sourceUri = Uri.parse(step.getVideoURL());

        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                Util.getUserAgent(getContext(), "yourApplicationName"));
        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(sourceUri);
        // Prepare the player with the source.
        exoPlayer.prepare(videoSource);
    }

    public void setStepProvider(IStepProvider stepProvider) {
        this.stepProvider = stepProvider;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (exoPlayer != null) {
            exoPlayer.release();
        }
    }

    public interface IStepProvider {
        Step getStep();
    }
}

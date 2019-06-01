package dev.chribru.android.activities.step;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import dev.chribru.android.R;
import dev.chribru.android.data.models.Step;

public class StepFragment extends Fragment {

    // keys for the saved instance state
    public static final String ARG_STEP_NUMBER = "step_number";
    public static final String PLAYBACK_POSITION = "playback_position";
    public static final String PLAY_WHEN_READY = "play_when_ready";
    public static final String CURRENT_WINDOW = "current_window";

    private int stepNo;
    private long playbackPosition;
    private boolean playWhenReady;
    private int currentWindow;

    private SimpleExoPlayer exoPlayer;
    private IStepProvider stepProvider;
    private PlayerView playerView;
    private Step step;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            stepNo = getArguments().getInt(ARG_STEP_NUMBER);
        }

        if (savedInstanceState != null) {
            playbackPosition = savedInstanceState.getLong(PLAYBACK_POSITION, 0);
            playWhenReady = savedInstanceState.getBoolean(PLAY_WHEN_READY, false);
            currentWindow = savedInstanceState.getInt(CURRENT_WINDOW, 0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.step_detail, container, false);

        TextView title = view.findViewById(R.id.step_detail_title);
        TextView description = view.findViewById(R.id.step_detail_description);
        TextView stepNumber = view.findViewById(R.id.step_detail_number);
        playerView = view.findViewById(R.id.exo_player);

        if (stepProvider != null) {
            step = stepProvider.get(stepNo);
        } else {
            StepsViewModel viewModel = ViewModelProviders.of(getActivity()).get(StepsViewModel.class);
            step = viewModel.getSteps().get(stepNo);
        }

        if (TextUtils.isEmpty(step.getVideoURL())) {
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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ARG_STEP_NUMBER, stepNo);
        outState.putLong(PLAYBACK_POSITION, playbackPosition);
        outState.putBoolean(PLAY_WHEN_READY, playWhenReady);
        outState.putInt(CURRENT_WINDOW, currentWindow);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!TextUtils.isEmpty(step.getVideoURL()) && Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(step.getVideoURL()) && (Util.SDK_INT <= 23 || exoPlayer == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop(); if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void releasePlayer() {
        if (exoPlayer != null) {
            playbackPosition = exoPlayer.getCurrentPosition();
            playWhenReady = exoPlayer.getPlayWhenReady();
            currentWindow = exoPlayer.getCurrentWindowIndex();
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    private void initializePlayer() {
        exoPlayer = ExoPlayerFactory.newSimpleInstance(Objects.requireNonNull(getContext()));
        playerView.setPlayer(exoPlayer);
        exoPlayer.setPlayWhenReady(playWhenReady);
        prepareDataSource(step);
    }

    private void prepareDataSource(Step step) {
        Uri sourceUri = Uri.parse(step.getVideoURL());

        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(Objects.requireNonNull(getContext()),
                Util.getUserAgent(getContext(), "yourApplicationName"));
        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(sourceUri);
        // Prepare the player with the source.
        exoPlayer.prepare(videoSource);

        // video was played before change
        exoPlayer.seekTo(currentWindow, playbackPosition);
    }

    public void setStepProvider(IStepProvider stepProvider) {
        this.stepProvider = stepProvider;
    }

    public interface IStepProvider {
        Step get(int id);
    }
}

package com.udacity.heather.timetobake.fragments;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.udacity.heather.timetobake.R;
import com.udacity.heather.timetobake.activities.RecipeDetailActivity;
import com.udacity.heather.timetobake.databinding.FragmentStepBinding;
import com.udacity.heather.timetobake.models.Recipe;
import com.udacity.heather.timetobake.models.Step;
import com.udacity.heather.timetobake.utilities.Constants;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.media.session.MediaButtonReceiver;

import static android.content.Context.NOTIFICATION_SERVICE;

public  class StepFragment extends Fragment implements Player.EventListener {

    private static final String TAG = StepFragment.class.getSimpleName();
    private FragmentStepBinding stepBinding;

    private boolean mTwoPane;
    private Recipe currentRecipe = new Recipe();
    private int stepPosition;
    private Step currentStep = new Step();
    private ExoPlayer exoPlayer;

    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private NotificationManager mNotificationManager;
    // An interface to implement onNextClicked and onPreviousClicked functions
    private NextPreviousClickListener nextPreviousClickListener;

    private boolean mPlayWhenReady = true;
    private long mCurrentVideoPosition = -1;

    String mVideoThumbnail;
    Bitmap mVideoThumbnailImage;
    Drawable film;

    public static final String STEP_URI = "step_uri";
    public static final String VIDEO = "video";

    public StepFragment() {

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.w(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>onCreate");
        mTwoPane = getArguments().getBoolean(Constants.TWO_PANE_KEY);
        if (!mTwoPane) {
            int orientation = getResources().getConfiguration().orientation;
            if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    // In landscape
                    ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
                } else {
                    // In portrait
                    ((AppCompatActivity) getActivity()).getSupportActionBar().show();
                }
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        stepBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_step, container, false);
        View view = stepBinding.getRoot();

        if (savedInstanceState != null && savedInstanceState.containsKey(Constants.CURRENT_VIDEO_POSITION)) {
            Log.w(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>onCreateView savedInstanceState.containsKey CURRENT_VIDEO_POSITION");
            mCurrentVideoPosition = savedInstanceState.getLong(Constants.CURRENT_VIDEO_POSITION);
            mPlayWhenReady = savedInstanceState.getBoolean(Constants.PLAY_WHEN_READY);
        }

        currentRecipe = getArguments().getParcelable(Constants.CURRENT_RECIPE);
        stepPosition = getArguments().getInt(Constants.CURRENT_STEP_POSITION_KEY);
        currentStep = currentRecipe.getSteps().get(stepPosition);


        initializeMediaSession();

        stepBinding.tvStepDescription.setText(currentStep.getDescription());
        if (!currentStep.getVideoURL().isEmpty()) {
            stepBinding.imageView.setVisibility(View.GONE);
            stepBinding.simpleExoPlayerView.setVisibility(View.VISIBLE);
            Uri videoUri = Uri.parse(currentStep.getVideoURL());
            initializePlayer(videoUri);
        }else{
            stepBinding.simpleExoPlayerView.setVisibility(View.GONE);
            stepBinding.imageView.setImageResource(R.drawable.film);
            stepBinding.imageView.setVisibility(View.VISIBLE);
        }
            stepBinding.btnNextStep.setOnClickListener(v -> nextPreviousClickListener.onNextClicked());

            stepBinding.btnPreviousStep.setOnClickListener(v -> nextPreviousClickListener.onPreviousClicked());

      //  if (!currentStep.getThumbnailURL().equals("") && !TextUtils.isEmpty(currentStep.getThumbnailURL())) {

          //  stepBinding.simpleExoPlayerView.setVisibility(View.GONE);
            //stepBinding.imageView.setVisibility(View.VISIBLE);

          //  Log.w(TAG, ">>>>>>>>>>>>>>Thumbnail URL is not empty");

            // stepBinding.imageView.setVisibility(View.VISIBLE);
            //   stepBinding.simpleExoPlayerView.setVisibility(View.GONE);
           // mVideoThumbnail = currentStep.getThumbnailURL();
            //mVideoThumbnailImage = ThumbnailUtils.createVideoThumbnail(mVideoThumbnail, MediaStore.Video.Thumbnails.MICRO_KIND);

            // }else{
            //stepBinding.simpleExoPlayerView.setDefaultArtwork(mVideoThumbnailImage);
            //stepBinding.simpleExoPlayerView.setVisibility(View.VISIBLE);
            //stepBinding.imageView.setVisibility(View.GONE);

       // }
        return view;
                    }

                    // An interface to implement onNextClicked and onPreviousClicked functions
                    public interface NextPreviousClickListener {
                        void onNextClicked();

                        void onPreviousClicked();
                    }


                    // Helper method to initialize ExoPlayer

                    @Override
                    public void onAttach (@NonNull Context context){
                        super.onAttach(context);

                        if (mTwoPane = true) {
                            try {
                                nextPreviousClickListener = (NextPreviousClickListener) context;
                            } catch (ClassCastException e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    private void initializeMediaSession () {

                        mMediaSession = new MediaSessionCompat(getActivity(), TAG);
                        // Enable callbacks from MediaButtons and TransportControls.
                        mMediaSession.setFlags(
                                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
                        // Do not let MediaButtons restart the player when the app is not visible.
                        mMediaSession.setMediaButtonReceiver(null);
                        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
                        mStateBuilder = new PlaybackStateCompat.Builder()
                                .setActions(
                                        PlaybackStateCompat.ACTION_PLAY |
                                                PlaybackStateCompat.ACTION_PAUSE |
                                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                                PlaybackStateCompat.ACTION_PLAY_PAUSE);
                        mMediaSession.setPlaybackState(mStateBuilder.build());
                        // MySessionCallback has methods that handle callbacks from a media controller.
                        mMediaSession.setCallback(new MySessionCallback());
                        // Start the Media Session since the activity is active.
                        mMediaSession.setActive(true);
                    }


                    // Helper method to show exoPlayer notification to control and show current step video info
                    private void showNotification (PlaybackStateCompat state){
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(Objects.requireNonNull(getActivity()), Constants.NOTIFICATION_CHANNEL_ID);
                        int icon;
                        String play_pause;
                        if (state.getState() == PlaybackStateCompat.STATE_PLAYING) {
                            icon = R.drawable.exo_controls_pause;
                            play_pause = getString(R.string.pause_btn_notification);
                        } else {
                            icon = R.drawable.exo_controls_play;
                            play_pause = getString(R.string.play_btn_notification);
                        }
                        NotificationCompat.Action playPauseAction = new NotificationCompat.Action(
                                icon, play_pause,
                                MediaButtonReceiver.buildMediaButtonPendingIntent(getActivity(),
                                        PlaybackStateCompat.ACTION_PLAY_PAUSE));
                        Intent intent = new Intent(getActivity(), RecipeDetailActivity.class);
                        intent.putExtra(Constants.CURRENT_RECIPE, currentRecipe);
                        intent.putExtra(Constants.CURRENT_STEP_POSITION_KEY, stepPosition);
                        intent.setAction(Constants.ACTION_RECIPE_STEP);
                        NotificationCompat.Action restartAction = new NotificationCompat
                                .Action(R.drawable.exo_controls_previous, getString(R.string.restart_btn_notification),
                                MediaButtonReceiver.buildMediaButtonPendingIntent
                                        (getActivity(), PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS));
                        PendingIntent contentPendingIntent = PendingIntent.getActivity
                                (getActivity(), 0, intent, 0);
                        builder.setContentTitle(currentRecipe.getName())
                                .setContentText(currentStep.getShortDescription())
                                .setContentIntent(contentPendingIntent)
                                .setSmallIcon(R.drawable.baseline_ondemand_video_black_48)
                                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                                .addAction(restartAction)
                                .addAction(playPauseAction);
                        mNotificationManager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
                        mNotificationManager.notify(0, builder.build());
                    }
                    public void initializePlayer (Uri mediaUri){

                     //   if (currentStep.getVideoURL() .equals("")) {
                       //     stepBinding.simpleExoPlayerView.setVisibility(View.GONE);
                         //   stepBinding.imageView.setVisibility(View.VISIBLE);
                        //}else{
                          //  stepBinding.simpleExoPlayerView.setVisibility(View.VISIBLE);
                            //stepBinding.imageView.setVisibility(View.GONE);
                       // }
                        if (exoPlayer == null) {
                            Log.w(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>initializePlayer mExoPlayer not null");
                            // Create an instance of the ExoPlayer.

                            //exoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), (RenderersFactory) new DefaultLoadControl(), new DefaultTrackSelector());
                            TrackSelector trackSelector = new DefaultTrackSelector();
                            LoadControl loadControl = new DefaultLoadControl();

                            exoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);

                            stepBinding.simpleExoPlayerView.setPlayer(exoPlayer);

                            exoPlayer.addListener(this);
                            // Prepare the MediaSource.
                            String userAgent = Util.getUserAgent(getActivity(), getString(R.string.app_name));
                            MediaSource mediaSource = new ExtractorMediaSource.Factory(new DefaultDataSourceFactory(getContext(), userAgent)).createMediaSource(mediaUri);
                            exoPlayer.prepare(mediaSource);
                            if (mCurrentVideoPosition != -1) {
                                Log.w(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>initializePlayer mCurrentVideoPosition not -1");
                                exoPlayer.seekTo(mCurrentVideoPosition);
                                exoPlayer.setPlayWhenReady(mPlayWhenReady);
                            } else {
                                exoPlayer.setPlayWhenReady(true);
                            }
                        }
                    }

                    // Helper method to release ExoPlayer and notification
                    private void releasePlayer () {
                        Log.w(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>releasePlayer");
                        if (mNotificationManager != null) {
                            mNotificationManager.cancelAll();
                        }
                        if (exoPlayer != null) {
                            Log.w(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>releasePlayer mExoPlayer not null");
                            try {
                                mPlayWhenReady = exoPlayer.getPlayWhenReady();
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }
                            mCurrentVideoPosition = exoPlayer.getCurrentPosition();
                            Log.w(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + mCurrentVideoPosition);
                            exoPlayer.stop();
                            exoPlayer.release();
                            exoPlayer = null;
                        }
                    }
                   // @Override
                    //public void onTimelineChanged(Timeline timeline, Object manifest) {
                    //}

                    @Override
                    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                    }

                    @Override
                    public void onLoadingChanged(boolean isLoading) {
                    }

                    @Override
                    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                        if ((playbackState == Player.STATE_READY) && playWhenReady) {
                            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                                    exoPlayer.getCurrentPosition(), 1f);
                        } else if ((playbackState == Player.STATE_READY)) {
                            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                                    exoPlayer.getCurrentPosition(), 1f);
                        }
                        mMediaSession.setPlaybackState(mStateBuilder.build());
                        showNotification(mStateBuilder.build());
                    }

                    @Override
                    public void onRepeatModeChanged(int repeatMode) {
                    }

                    @Override
                    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
                    }

                    @Override
                    public void onPlayerError(ExoPlaybackException error) {
                    }

                    @Override
                    public void onPositionDiscontinuity(int reason) {
                    }

                    @Override
                    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
                    }

                    @Override
                    public void onSeekProcessed() {
                    }

                    private class MySessionCallback extends MediaSessionCompat.Callback {
                        @Override
                        public void onPlay() {
                            exoPlayer.setPlayWhenReady(true);
                        }

                        @Override
                        public void onPause() { exoPlayer.setPlayWhenReady(false);
                        }

                        @Override
                        public void onSkipToPrevious() {
                            exoPlayer.seekTo(0);
                        }
                    }

                    public static class MediaReceiver extends BroadcastReceiver {
                        public MediaReceiver() {
                        }

                        @Override
                        public void onReceive(Context context, Intent intent) {
                            MediaButtonReceiver.handleIntent(mMediaSession, intent);
                        }
                    }

                    @Override
                    public void onPause() {
                        super.onPause();
                        Log.w(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>onPause");
                        // Release ExoPlayer and disable mediaSession when activity is destroyed
                        releasePlayer();
                        mMediaSession.setActive(false);
                    }

                    @Override
                    public void onResume() {
                        super.onResume();
                        Log.w(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>onResume");
                        // Set new title in actionBar
                        getActivity().setTitle(currentStep.getShortDescription());
                        if (exoPlayer == null){
                            if (currentStep.getVideoURL() != null && !TextUtils.isEmpty(currentStep.getVideoURL())) {
                                Uri videoUri = Uri.parse(currentStep.getVideoURL());
                                initializePlayer(videoUri);
                            }

                        }
                    }

                    @Override
                    public void onSaveInstanceState(@NonNull Bundle outState) {
                        Log.w(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>onSaveInstanceState");
                        outState.putBoolean(Constants.PLAY_WHEN_READY, mPlayWhenReady);
                        outState.putLong(Constants.CURRENT_VIDEO_POSITION, mCurrentVideoPosition);
                        super.onSaveInstanceState(outState);
                    }
                }
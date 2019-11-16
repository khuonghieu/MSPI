package edu.temple.spiapp;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

public class VideoAdapterCard extends RecyclerView.Adapter<VideoAdapterCard.VideoViewHolder> {

    public Context context;
    public ArrayList<Uri> videoLinkArray;
    public ArrayList<String> videoNameArray;
    public PlayerView playerView;
    public SimpleExoPlayer simpleExoPlayer;

    public VideoAdapterCard(Context context, ArrayList<Uri> videoLinkArray,
                            ArrayList<String> videoNameArray, PlayerView playerView,
                            SimpleExoPlayer simpleExoPlayer) {
        this.context = context;
        this.videoLinkArray = videoLinkArray;
        this.videoNameArray = videoNameArray;
        this.playerView = playerView;
        this.simpleExoPlayer = simpleExoPlayer;
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        TextView videoName;
        ImageButton playVideo;

        public VideoViewHolder(View itemView) {
            super(itemView);
            this.videoName = (TextView) itemView.findViewById(R.id.videoName);
            this.playVideo = (ImageButton) itemView.findViewById(R.id.playVideo);
        }
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_card_noti, parent, false);
        VideoViewHolder myViewHolder = new VideoViewHolder(view);
        return myViewHolder;
    }

    public void onBindViewHolder(final VideoViewHolder holder, int position) {

        final Uri videoUri = videoLinkArray.get(videoLinkArray.size() - position - 1);
        TextView videoName = holder.videoName;
        videoName.setText(videoNameArray.get(videoNameArray.size() - position - 1));
        ImageButton playVideo = holder.playVideo;
        playVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Bind player to the playerView
                playerView.setPlayer(simpleExoPlayer);
                // Produces DataSource instances through which media data is loaded.
                DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                        Util.getUserAgent(context, "MSPi"));
                // This is the MediaSource representing the media to be played.
                MediaSource videoSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(videoUri);
                // Prepare the player with the source.
                simpleExoPlayer.prepare(videoSource);
                simpleExoPlayer.setPlayWhenReady(false);
            }
        });


    }

    @Override
    public int getItemCount() {
        return Math.min(10, videoLinkArray.size());
    }
}

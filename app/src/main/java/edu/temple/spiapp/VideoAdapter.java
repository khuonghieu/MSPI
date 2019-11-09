package edu.temple.spiapp;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

public class VideoAdapter extends BaseAdapter {

    public Context context;
    public ArrayList<Uri> videoLinkArray;
    public ArrayList<String> videoNameArray;
    public PlayerView playerView;
    public SimpleExoPlayer simpleExoPlayer;

    public VideoAdapter(Context context, ArrayList<Uri> videoLinkArray,
                        ArrayList<String> videoNameArray, PlayerView playerView,
                        SimpleExoPlayer simpleExoPlayer) {
        this.context = context;
        this.videoLinkArray = videoLinkArray;
        this.videoNameArray = videoNameArray;
        this.playerView = playerView;
        this.simpleExoPlayer = simpleExoPlayer;
    }

    @Override
    public int getCount() {
        return Math.min(videoLinkArray.size(),5);
    }

    @Override
    public Object getItem(int position) {
        return videoLinkArray.get(videoLinkArray.size()-position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Uri videoUri = videoLinkArray.get(videoLinkArray.size()-position-1);

        LinearLayout linearLayout = new LinearLayout(this.context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TextView textView = new TextView(this.context);
        textView.setText(videoNameArray.get(videoNameArray.size() - position -1));
        textView.setTextSize(20);
        textView.setOnClickListener(new View.OnClickListener() {
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

        linearLayout.addView(textView);
        return linearLayout;
    }

}

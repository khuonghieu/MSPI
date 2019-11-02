package edu.temple.spiapp;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class VideoAdapter extends BaseAdapter {

    public Context context;
    public ArrayList<Uri> videoLinkArray;
    public ArrayList<String> videoNameArray;

    public VideoAdapter(Context context, ArrayList<Uri> videoLinkArray, ArrayList<String> videoNameArray) {
        this.context = context;
        this.videoLinkArray = videoLinkArray;
        this.videoNameArray = videoNameArray;
    }

    @Override
    public int getCount() {
        return Math.min(videoLinkArray.size(),2);
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

        SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(this.context);

        LinearLayout linearLayout = new LinearLayout(this.context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        PlayerView playerView = new PlayerView(this.context);
        playerView.setPlayer(player);
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                Util.getUserAgent(context, "yourApplicationName"));
        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(videoLinkArray.get(videoLinkArray.size()-position-1));
        // Prepare the player with the source.
        player.prepare(videoSource);
        player.pla

        TextView textView = new TextView(this.context);
        textView.setText(videoNameArray.get(videoNameArray.size() - position -1));

        linearLayout.addView(playerView);
        linearLayout.addView(textView);

        return linearLayout;
    }
}

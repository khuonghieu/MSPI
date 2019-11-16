package edu.temple.spiapp;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class VideoFragment extends Fragment {

    private final static String TAG = "VidFrag";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);

        final RecyclerView videoList = view.findViewById(R.id.videoList);
        videoList.setHasFixedSize(true);
        videoList.setLayoutManager(new LinearLayoutManager(getContext()));
        videoList.setItemAnimator(new DefaultItemAnimator());

        final ArrayList<Uri> videoUriList = new ArrayList<>();
        final PlayerView playerView = view.findViewById(R.id.playerView);
        final ArrayList<String> videoNameList = new ArrayList<>();
        final SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(getContext());
        final TextView setSpeed = view.findViewById(R.id.setSpeed);

        SeekBar speedSeekBar = view.findViewById(R.id.speedSeekBar);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference listRef = storage.getReferenceFromUrl("gs://mspi-a4b75.appspot.com/videos");
        listRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference item : listResult.getItems()) {
                    // All the items under listRef.
                    videoNameList.add(item.getName());
                    item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            videoUriList.add(uri);
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            VideoAdapterCard videoAdapter = new VideoAdapterCard(getContext(), videoUriList, videoNameList, playerView, player);
                            videoList.setAdapter(videoAdapter);
                        }
                    });
                }
            }
        });

        //Seekbar to control video speed
        final double[] speed = {0.25, 0.5, 0.75, 1.0, 1.25, 1.5, 1.75};
        speedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    player.setPlaybackParameters(new PlaybackParameters((float) speed[progress]));
                    setSpeed.setText(String.valueOf(speed[progress]) + "x");
                }
            }

            //Pause when choosing speed
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                player.setPlayWhenReady(false);
                player.getPlaybackState();
            }

            //Resume when done choosing speed
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                player.setPlayWhenReady(true);
                player.getPlaybackState();
            }
        });
        return view;
    }
}
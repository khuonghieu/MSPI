package edu.temple.spiapp;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class VideoFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);

        final ListView videoList = view.findViewById(R.id.videoList);
        final ArrayList<Uri> videoUriList = new ArrayList<>();
        final PlayerView playerView = view.findViewById(R.id.playerView);
        final ArrayList<String> videoNameList = new ArrayList<>();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference listRef = storage.getReferenceFromUrl("gs://mspi-a4b75.appspot.com/videos");
        listRef.list(5).addOnSuccessListener(new OnSuccessListener<ListResult>() {
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
                            VideoAdapter videoAdapter = new VideoAdapter(getContext(),videoUriList,videoNameList, playerView);
                            videoList.setAdapter(videoAdapter);
                        }
                    });
                }
            }
        });

        return view;
    }
}
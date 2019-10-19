package edu.temple.spiapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class NotifFragment extends Fragment {
    private final static String TAG = "AccountFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notif_fragment, container, false);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference listRef = storage.getReferenceFromUrl("gs://mspi-a4b75.appspot.com/images");


        return view;
    }
}

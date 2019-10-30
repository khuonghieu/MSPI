package edu.temple.spiapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CameraFragment extends Fragment {
    private final static String TAG = "CameraFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.camera_fragment, container, false);

        Button chooseCam = view.findViewById(R.id.chooseCam);


        chooseCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://192.168.1.217:5000";
                WebView viewCam = (WebView)getView().findViewById(R.id.webView);
                viewCam.getSettings().setLoadsImagesAutomatically(true);
                viewCam.getSettings().setJavaScriptEnabled(true);
                viewCam.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                viewCam.loadUrl(url);
            }
        });


        return view;
    }
}

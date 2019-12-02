package edu.temple.spiapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CameraFragment extends Fragment {
    private final static String TAG = "CamFrag";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.camera_fragment, container, false);

        Button showStream = view.findViewById(R.id.showStream);
        Button stopStream = view.findViewById(R.id.stopStream);
        final WebView viewCam = view.findViewById(R.id.webView);

        showStream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://mspi-a4b75.firebaseapp.com/login";
                viewCam.getSettings().setLoadsImagesAutomatically(true);
                viewCam.getSettings().setAllowContentAccess(true);
                viewCam.getSettings().setJavaScriptEnabled(true);
                viewCam.getSettings().setAllowFileAccess(true);
                viewCam.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                viewCam.getSettings().setDomStorageEnabled(true);
                viewCam.getSettings().setMediaPlaybackRequiresUserGesture(false);
                viewCam.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                viewCam.setWebChromeClient(new WebChromeClient());
                viewCam.loadUrl(url);
            }
        });
        stopStream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //viewCam.clearCache(true);
                viewCam.stopLoading();
                viewCam.destroy();
            }
        });


        return view;
    }
}

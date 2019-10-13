package edu.temple.spiapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button chooseCam = findViewById(R.id.chooseCam);
        TextView userName = findViewById(R.id.userText);
        userName.setText("Logged in as:" + getIntent().getStringExtra("accountName"));
        chooseCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://192.168.1.217:5000";
                WebView viewCam = (WebView)findViewById(R.id.webView);
                viewCam.getSettings().setLoadsImagesAutomatically(true);
                viewCam.getSettings().setJavaScriptEnabled(true);
                viewCam.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                viewCam.loadUrl(url);

            }
        });
    }
}

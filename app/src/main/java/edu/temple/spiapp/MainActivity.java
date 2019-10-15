package edu.temple.spiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Button signoutButton;
    //test commit, making sure I have version control setup properly.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button chooseCam = findViewById(R.id.chooseCam);
        TextView userName = findViewById(R.id.userText);
        userName.setText(getIntent().getStringExtra("Service name")+":"+ getIntent().getStringExtra("accountName"));
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
        signoutButton = findViewById(R.id.signoutButton);
        signoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();
                GoogleSignInAccount googleCurrentAcc = GoogleSignIn.getLastSignedInAccount(MainActivity.this);
                FirebaseUser githubCurrentAcc = FirebaseAuth.getInstance().getCurrentUser();
                if(googleCurrentAcc!=null){
                    GoogleSignIn.getClient(MainActivity.this,gso).signOut();
                    startActivity(new Intent(MainActivity.this,LogIn.class));
                } else if(githubCurrentAcc!=null){
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(MainActivity.this,LogIn.class));
                }
                finish();
            }
        });
    }
}

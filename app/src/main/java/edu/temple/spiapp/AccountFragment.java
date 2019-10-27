package edu.temple.spiapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.OAuthCredential;
import com.google.firebase.auth.OAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;

public class AccountFragment extends Fragment {
    private final static String TAG = "AccountFragment";
    Button signoutButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.account_fragment, container, false);
        TextView userName = view.findViewById(R.id.userText);
        userName.setText("Logged In by: " +
                getActivity().getIntent().getStringExtra("Service name") + ":" + "\n"+
                getActivity().getIntent().getStringExtra("accountName"));

        signoutButton = view.findViewById(R.id.signoutButton);
        signoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();
                GoogleSignInAccount googleCurrentAcc = GoogleSignIn.getLastSignedInAccount(getContext());
                FirebaseUser githubCurrentAcc = FirebaseAuth.getInstance().getCurrentUser();
                if (googleCurrentAcc != null) {
                    GoogleSignIn.getClient(getContext(), gso).signOut();
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getActivity(), LogIn.class));
                } else if (githubCurrentAcc != null) {
                    FirebaseAuth.getInstance(FirebaseAuth.getInstance().getApp()).signOut();
                    GoogleSignIn.getClient(getContext(), gso).signOut();
                    startActivity(new Intent(getActivity(), LogIn.class));
                }
                getActivity().finish();
            }
        });
        return view;
    }
}

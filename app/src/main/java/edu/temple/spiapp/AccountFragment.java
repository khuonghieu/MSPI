package edu.temple.spiapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AccountFragment extends Fragment {
    private final static String TAG = "AccFrag";
    Button signoutButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.account_fragment, container, false);

        // Access a Cloud Firestore instance from your Activity
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        //Look for existing user
        final GoogleSignInAccount googleCurrentAcc = GoogleSignIn.getLastSignedInAccount(getContext());
        FirebaseUser githubCurrentAcc = FirebaseAuth.getInstance().getCurrentUser();

        ImageView userAva = view.findViewById(R.id.userAva);
        ImageView serviceIcon = view.findViewById(R.id.serviceIcon);
        TextView userName = view.findViewById(R.id.userName);
        TextView userEmail = view.findViewById(R.id.userEmail);

        if (googleCurrentAcc != null) {
            Picasso.get().load(googleCurrentAcc.getPhotoUrl()).into(userAva);
            serviceIcon.setBackground(getResources().getDrawable(R.drawable.fui_ic_googleg_color_24dp, null));
            userName.setText("Name: " + googleCurrentAcc.getDisplayName());
            userEmail.setText("Email: " + googleCurrentAcc.getEmail());

            DocumentReference docIdRef = db.collection("users").document(googleCurrentAcc.getId());
            docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("Document", "Document exists!");
                        } else {
                            Log.d("Document", "Document does not exist!");
                            //Create data
                            Map<String, Object> docData = new HashMap<>();
                            ArrayList<String> cameraList = new ArrayList<>();
                            docData.put("cameraIds",cameraList);
                            docData.put("email",googleCurrentAcc.getEmail());

                            //Add document to firestore
                            db.collection("users").document(googleCurrentAcc.getId())
                                    .set(docData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("AddDoc", "DocumentSnapshot successfully written!");
                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("AddDoc", "Error writing document", e);
                                        }
                                    });
                        }
                    } else {
                        Log.d("Document", "Failed with: ", task.getException());
                    }
                }
            });


        } else if (githubCurrentAcc != null) {
            Picasso.get().load(githubCurrentAcc.getPhotoUrl()).into(userAva);

            if (githubCurrentAcc.getProviderId() == "firebase") {
                serviceIcon.setBackground(getResources().getDrawable(R.drawable.fui_ic_mail_white_24dp, null));
                serviceIcon.setBackgroundTintList(getResources().getColorStateList(R.color.fui_bgPhone, null));
                userName.setText("Name: " + githubCurrentAcc.getEmail()
                        .substring(0, githubCurrentAcc.getEmail().indexOf('@')));
            } else {
                serviceIcon.setBackground(getResources().getDrawable(R.drawable.fui_ic_github_white_24dp, null));
                serviceIcon.setBackgroundTintList(getResources().getColorStateList(R.color.fui_bgGitHub, null));
                userName.setText("Name: " + githubCurrentAcc.getDisplayName());
            }
            userEmail.setText("Email: " + githubCurrentAcc.getEmail());
        }
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

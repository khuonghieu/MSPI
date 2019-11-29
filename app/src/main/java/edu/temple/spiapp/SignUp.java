package edu.temple.spiapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    public EditText emailID, pswd, firstName, lastName;
    Button btnSignUp;
    FirebaseAuth firebaseAuth;
    final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        firebaseAuth = FirebaseAuth.getInstance();
        emailID = findViewById(R.id.signUpEmail);
        pswd = findViewById(R.id.signUpPassword);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        btnSignUp = findViewById(R.id.signUpButton);


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String emailId = emailID.getText().toString();
                String paswd = pswd.getText().toString();
                if (emailId.isEmpty()) {
                    emailID.setError("Provide your Email first!");
                    emailID.requestFocus();
                } else if (paswd.isEmpty()) {
                    pswd.setError("Set your password");
                    pswd.requestFocus();
                } else if (emailId.isEmpty() && paswd.isEmpty()) {
                    Toast.makeText(SignUp.this, "Fields Empty!", Toast.LENGTH_SHORT).show();
                } else if (!(emailId.isEmpty() && paswd.isEmpty())) {
                    firebaseAuth.createUserWithEmailAndPassword(emailId, paswd)
                            .addOnCompleteListener(SignUp.this, new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {

                            if (!task.isSuccessful()) {
                                Toast.makeText(SignUp.this.getApplicationContext(),
                                        "SignUp unsuccessful: " + task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                Map<String, Object> newUser = new HashMap<>();
                                newUser.put("cameraIds",new ArrayList<String>());
                                newUser.put("email", emailId);
                                newUser.put("familiarFaces",new ArrayList<String>());
                                newUser.put("firstName", firstName.getText().toString());
                                newUser.put("lastName", lastName.getText().toString());

                                db.collection("users").document(currentUser.getUid())
                                        .set(newUser)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("AddDocTest", "DocumentSnapshot successfully written!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("AddDocTest", "Error writing document", e);
                                            }
                                        });
                                startActivity(new Intent(SignUp.this, MainActivity.class));
                            }
                        }
                    });
                } else {
                    Toast.makeText(SignUp.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}

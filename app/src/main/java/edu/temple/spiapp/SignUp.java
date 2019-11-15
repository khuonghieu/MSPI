package edu.temple.spiapp;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {

    public EditText emailID, pswd, pswd2;
    Button btnSignUp;
    TextView signIn;
    FirebaseAuth firebaseAuth;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_sign_up);
        firebaseAuth = FirebaseAuth.getInstance();
        emailID = findViewById(R.id.signUpEmail);
        pswd = findViewById(R.id.signUpPassword);
        pswd2 = findViewById(R.id.signUpPassword2);
        btnSignUp = findViewById(R.id.signUpButton);


    }
}

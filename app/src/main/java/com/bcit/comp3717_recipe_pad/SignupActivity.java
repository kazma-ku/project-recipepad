package com.bcit.comp3717_recipe_pad;

import static com.bcit.comp3717_recipe_pad.DataHandler.getUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();
    }

    public void swapToLogin(View v) {
        EditText username = findViewById(R.id.editText_signup_username);
        Button in = findViewById(R.id.button_main_signup);

        username.setVisibility(View.GONE);
        in.setText(R.string.login);


    }

    public void createUser(View v) {
        EditText username = findViewById(R.id.editText_signup_username);
        EditText email = findViewById(R.id.editText_signup_email);
        EditText password = findViewById(R.id.editText_signup_password);

        String usernameText = username.getText().toString().trim();
        String emailText = email.getText().toString().trim();
        String passwordText = password.getText().toString().trim();

        if (usernameText.isEmpty() || emailText.isEmpty() || passwordText.isEmpty()) {
            Toast.makeText(SignupActivity.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("debug", "createUserWithEmail:success");
                            mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
                                @Override
                                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                                    if (firebaseAuth.getCurrentUser() == null) {
                                        Log.d("debug", "null user");
                                    }
                                    else {
                                        Log.d("debug", firebaseAuth.getCurrentUser().getUid());
                                        DataHandler.addUser(new User(usernameText, emailText),
                                                firebaseAuth.getCurrentUser().getUid());
                                    }
                                }
                            });

                            //
                            Intent myIntent = new Intent(SignupActivity.this, TrendingActivity.class);
                            SignupActivity.this.startActivity(myIntent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("debug", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Sign up failed. Please try again.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
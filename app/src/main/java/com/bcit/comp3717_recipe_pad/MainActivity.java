package com.bcit.comp3717_recipe_pad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText email;
    private EditText password;
    private Button login;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if (mAuth == null) {
            mAuth = FirebaseAuth.getInstance();
        }
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUser.reload();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.editText_main_username);
        password = findViewById(R.id.editText_main_password);
        login = findViewById(R.id.button_main_login);

        System.out.println(email.getText().toString());
        System.out.println(password.getText().toString());


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_mail = email.getText().toString();
                String user_password = password.getText().toString();
                signIn(user_mail, user_password);
            }
        });
    }

    public void signUp(View v) {

        Intent myIntent = new Intent(MainActivity.this, SignupActivity.class);
        MainActivity.this.startActivity(myIntent);

    }

    public void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Log.d("debug", "success");
                    Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, TrendingActivity.class));
                    finish();
                }
                else
                {

                    TextView textView = findViewById(R.id.textView_main_incorrectlogin);
                    textView.setText("The email or password you entered is incorrect.");
                    task.getException().printStackTrace();
                    Toast.makeText(MainActivity.this,"Login Unsuccessful",Toast.LENGTH_SHORT).show();
                }
            }

//            @Override
//            public void onSuccess(AuthResult authResult) {
//                Log.d("debug", "success");
//                Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(MainActivity.this, TrendingActivity.class));
//                finish();
//            }

        });
    }
}

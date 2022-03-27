package com.bcit.comp3717_recipe_pad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void signUp(View v) {

        Intent myIntent = new Intent(MainActivity.this, SignupActivity.class);
        MainActivity.this.startActivity(myIntent);

    }
}
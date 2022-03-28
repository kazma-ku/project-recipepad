package com.bcit.comp3717_recipe_pad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class TrendingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trending);
        setupRecyclerView(DataHandler.getTrending());
    }


    void setupRecyclerView(Recipe[] recipes) {

        RecyclerView rv = findViewById(R.id.reyclerView_trending);
        TrendingRecyclerActivity adapter = new TrendingRecyclerActivity(recipes);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
    }
}



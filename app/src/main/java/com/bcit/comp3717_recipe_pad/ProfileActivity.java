package com.bcit.comp3717_recipe_pad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
//        setupRecyclerView(DataHandler.getTrending());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView_profile);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.item_bottomnav_home)
                {
                    item.setChecked(true);
                    Intent intent = new Intent(ProfileActivity.this, MainFeedActivity.class);
                    startActivity(intent);
                }
                else if(item.getItemId() == R.id.item_bottomnav_profile)
                {
                    item.setChecked(true);
                    Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
                    startActivity(intent);
                }
                else if(item.getItemId() == R.id.item_bottomnav_trending)
                {
                    item.setChecked(true);
                    Intent intent = new Intent(ProfileActivity.this, TrendingActivity.class);
                    startActivity(intent);
                }
                else if (item.getItemId() == R.id.item_bottomnav_upload)
                {
                    item.setChecked(true);
                    Intent intent = new Intent(ProfileActivity.this, AddRecipeActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });
    }

    void setupRecyclerView(Recipe[] recipes) {

        RecyclerView rv = findViewById(R.id.recyclerView_profile);
        ProfileRecyclerActivity adapter = new ProfileRecyclerActivity(recipes);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
    }
}
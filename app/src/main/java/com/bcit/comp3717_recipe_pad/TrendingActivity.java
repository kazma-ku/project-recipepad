package com.bcit.comp3717_recipe_pad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class TrendingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trending);
        DataHandler.getData(new OnSuccess() {
            @Override
            public void onData(Recipe[] recipes) {
                setupRecyclerView(recipes);
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView_trending);
        bottomNavigationView.setSelectedItemId(R.id.item_bottomnav_trending);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.item_bottomnav_home)
                {
                    item.setChecked(true);
                    Intent intent = new Intent(TrendingActivity.this, MainFeedActivity.class);
                    startActivity(intent);
                }
                else if(item.getItemId() == R.id.item_bottomnav_profile)
                {
                    item.setChecked(true);
                    Intent intent = new Intent(TrendingActivity.this, ProfileActivity.class);
                    startActivity(intent);
                }
                else if(item.getItemId() == R.id.item_bottomnav_trending)
                {
                    item.setChecked(true);
                    Intent intent = new Intent(TrendingActivity.this, TrendingActivity.class);
                    startActivity(intent);
                }
                else if (item.getItemId() == R.id.item_bottomnav_upload)
                {
                    item.setChecked(true);
                    Intent intent = new Intent(TrendingActivity.this, AddRecipeActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });
    }

    void setupRecyclerView(Recipe[] recipes) {

        RecyclerView rv = findViewById(R.id.reyclerView_trending);
        TrendingRecyclerActivity adapter = new TrendingRecyclerActivity(recipes);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_action_bar, menu);

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if (item.getItemId() == R.id.item_actionbar_logout)
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}



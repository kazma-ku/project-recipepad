package com.bcit.comp3717_recipe_pad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class TrendingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trending);

        DataHandler.getTrending(new OnSuccess() {
            @Override
            public void onData(Recipe[] recipes) {
                if (recipes.length == 0) {
                    TextView header = findViewById(R.id.textView_trending_trendingrecipes);
                    header.setText(R.string.noTrending);
                }
                setupRecyclerView(recipes);
            }
        }, this);


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
        else if (item.getItemId() == R.id.item_actionbar_loadmockdata)
        {
            DataHandler.addMockData(this);
            Toast.makeText(this, R.string.mock_data_loaded, Toast.LENGTH_SHORT).show();
            recreate();
        }

        return super.onOptionsItemSelected(item);
    }

}



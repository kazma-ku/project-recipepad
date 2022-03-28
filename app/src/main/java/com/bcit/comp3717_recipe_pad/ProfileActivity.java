package com.bcit.comp3717_recipe_pad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DocumentReference docRef = db.collection("users").document(user.getUid());

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    User user = document.toObject(User.class);
                    if (user != null)
                    passUserData(user);
                } else {
                    Log.d("debug", "No doc");
                }
            }
        });


        setupRecyclerData();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView_profile);
        bottomNavigationView.setSelectedItemId(R.id.item_bottomnav_profile);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.item_bottomnav_home) {
                    item.setChecked(true);
                    Intent intent = new Intent(ProfileActivity.this, MainFeedActivity.class);
                    startActivity(intent);
                } else if (item.getItemId() == R.id.item_bottomnav_profile) {
                    item.setChecked(true);
                    Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
                    startActivity(intent);
                } else if (item.getItemId() == R.id.item_bottomnav_trending) {
                    item.setChecked(true);
                    Intent intent = new Intent(ProfileActivity.this, TrendingActivity.class);
                    startActivity(intent);
                } else if (item.getItemId() == R.id.item_bottomnav_upload) {
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

    void setupRecyclerData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

//        Date threeDaysAgo = new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(3));
//
//        Timestamp cutOff = new Timestamp(threeDaysAgo);

        List<Recipe> recipes = new ArrayList<>();

        Log.d("before", user.getUid());


        db.collection("recipes")
                .whereEqualTo("userID", user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                Log.d("RecipeGet", documentSnapshot.get("title").toString());
                                recipes.add(documentSnapshot.toObject(Recipe.class));

                            }
                        } else {
                            Log.d("else", "not success");
                        }
                        Recipe[] sorted = recipes.toArray(new Recipe[recipes.size()]);
                        setupRecyclerView(sorted);

                    }
                });
    }

    void passUserData(User u) {

        TextView username = findViewById(R.id.textView_profile_username);
        TextView followers = findViewById(R.id.textView_profile_followers);
        TextView following = findViewById(R.id.textView_profile_following);

        username.setText(u.getUsername());
        followers.setText(u.getFollowerCount() + " Followers");
        following.setText(u.getFollowingCount() + " Following");
    }
}
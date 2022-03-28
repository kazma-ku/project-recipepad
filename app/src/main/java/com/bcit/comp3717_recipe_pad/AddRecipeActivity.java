package com.bcit.comp3717_recipe_pad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddRecipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView_addrecipe);
        bottomNavigationView.setSelectedItemId(R.id.item_bottomnav_upload);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.item_bottomnav_home)
                {
                    item.setChecked(true);
                    Intent intent = new Intent(AddRecipeActivity.this, MainFeedActivity.class);
                    startActivity(intent);
                }
                else if(item.getItemId() == R.id.item_bottomnav_profile)
                {
                    item.setChecked(true);
                    Intent intent = new Intent(AddRecipeActivity.this, ProfileActivity.class);
                    startActivity(intent);
                }
                else if(item.getItemId() == R.id.item_bottomnav_trending)
                {
                    item.setChecked(true);
                    Intent intent = new Intent(AddRecipeActivity.this, TrendingActivity.class);
                    startActivity(intent);
                }
                else if (item.getItemId() == R.id.item_bottomnav_upload)
                {
                    item.setChecked(true);
                    Intent intent = new Intent(AddRecipeActivity.this, AddRecipeActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });
    }

    public void addRecipe(View view)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();

        EditText editTextTitle = findViewById(R.id.editText_addrecipe_title);
        EditText editTextDescription = findViewById(R.id.editText_addrecipe_description);
        EditText editTextIngredients = findViewById(R.id.editText_addrecipe_ingredients);
        EditText editTextSteps = findViewById(R.id.editText_addrecipe_steps);
        EditText editTextNutritionalFacts = findViewById(R.id.editText_addrecipe_nutritionalfacts);

        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        String ingredients = editTextIngredients.getText().toString();
        String steps = editTextSteps.getText().toString();
        String nutritionalFacts = editTextNutritionalFacts.getText().toString();

        if(!title.isEmpty() && !description.isEmpty() && !ingredients.isEmpty() && !steps.isEmpty()
                && !nutritionalFacts.isEmpty())
        {
            Recipe recipe = new Recipe("", title, description, ingredients, steps, nutritionalFacts, userID);

            db.collection("recipes")
                    .add(recipe)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("Debug", "DocumentSnapshot added with ID: " + documentReference.getId());
                            Intent intent = new Intent(getBaseContext(), AddRecipeActivity.class);
                            startActivity(intent);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("Debug", "Error adding document", e);
                        }
                    });
        }
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
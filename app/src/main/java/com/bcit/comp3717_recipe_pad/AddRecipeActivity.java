package com.bcit.comp3717_recipe_pad;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class AddRecipeActivity<storage> extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        ImageView foodPic = findViewById(R.id.imageView_addrecipe_uploadimage);

        foodPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 3);
            }
        });


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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       super.onActivityResult(requestCode, resultCode, data);
       if (resultCode == RESULT_OK && data != null) {
           Uri selectedImage = data.getData();
           ImageView imageView = findViewById(R.id.imageView_addrecipe_uploadimage);
           imageView.setImageURI(selectedImage);
           }
       }


    public void addRecipe(View view)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();
        ImageView foodPic = findViewById(R.id.imageView_addrecipe_uploadimage);

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

        if (foodPic != null){

        }

        if(foodPic != null && !title.isEmpty() && !description.isEmpty() && !ingredients.isEmpty() && !steps.isEmpty()
                && !nutritionalFacts.isEmpty())
        {
            addImage();
            String path = addImage();
            Recipe recipe = new Recipe(path, title, description, ingredients, steps, nutritionalFacts, userID);

            db.collection("recipes")
                    .add(recipe)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("Debug", "DocumentSnapshot added with ID: " + documentReference.getId());
                            documentReference.update("recipeID", documentReference.getId());

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

    private String addImage() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        ImageView foodPic = findViewById(R.id.imageView_addrecipe_uploadimage);

        String path = "foods/" + UUID.randomUUID() + ".jpeg";
        StorageReference imagesRef = storageRef.child(path);
        
        foodPic.setDrawingCacheEnabled(true);
        foodPic.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) foodPic.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
               public void onFailure(@NonNull Exception exception) {
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
         @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
              }
            });
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("images").document().set("url" + path);
        return path;
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
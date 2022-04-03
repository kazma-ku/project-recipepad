package com.bcit.comp3717_recipe_pad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class RecipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//
//        DocumentReference docRef = db.collection("recipes").document(user.getUid());
//
//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    Recipe recipe = document.toObject(Recipe.class);
//                    if (user != null)
//                        passRecipeData(recipeInfo);
//                } else {
//                    Log.d("debug", "No doc");
//                }
//            }
//        });

        Intent intent = getIntent();

        ArrayList<String> recipeInfo = intent.getStringArrayListExtra("recipe");

//        System.out.println(recipe.getIngredients());
        passRecipeData(recipeInfo);
    }


    void passRecipeData(ArrayList<String> recipeInfo)
    {
        TextView recipeName = findViewById(R.id.textView_recipe_recipename);
        TextView likeCount = findViewById(R.id.textView_recipe_likecount);
        TextView dislikeCount = findViewById(R.id.textView_recipe_dislikecount);
        TextView commentCount = findViewById(R.id.textView_recipe_commentscount);
        TextView description = findViewById(R.id.textView_recipe_description);
        TextView ingredients = findViewById(R.id.textView_recipe_ingredients);
        TextView steps = findViewById(R.id.textView_recipe_steps);
        TextView uploadDate = findViewById(R.id.textView_recipe_uploaddate);

        recipeName.setText(recipeInfo.get(0));
        likeCount.setText(recipeInfo.get(1));
        dislikeCount.setText(recipeInfo.get(2));
        commentCount.setText(recipeInfo.get(3));
        description.setText(recipeInfo.get(4));
        ingredients.setText(recipeInfo.get(5));
        steps.setText(recipeInfo.get(6));
        uploadDate.setText(recipeInfo.get(7));
    }
}
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

public class RecipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DocumentReference docRef = db.collection("recipes").document(user.getUid());

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Recipe recipe = document.toObject(Recipe.class);
                    if (user != null)
                        passRecipeData(recipe);
                } else {
                    Log.d("debug", "No doc");
                }
            }
        });

        Intent intent = getIntent();
        Recipe recipe = (Recipe) intent.getSerializableExtra("recipe");
//        System.out.println(recipe.getIngredients());
        passRecipeData(recipe);
    }


    void passRecipeData(Recipe recipe)
    {
        TextView recipeName = findViewById(R.id.textView_recipe_recipename);
        TextView likeCount = findViewById(R.id.textView_recipe_likecount);
        TextView dislikeCount = findViewById(R.id.textView_recipe_dislikecount);
        TextView commentCount = findViewById(R.id.textView_recipe_commentscount);
        TextView description = findViewById(R.id.textView_recipe_description);
        TextView ingredients = findViewById(R.id.textView_recipe_ingredients);
        TextView steps = findViewById(R.id.textView_recipe_steps);
        TextView uploadDate = findViewById(R.id.textView_recipe_uploaddate);

        recipeName.setText(recipe.getTitle());
        likeCount.setText(Integer.toString(recipe.getLikesNum()));
        dislikeCount.setText(Integer.toString(recipe.getDislikesNum()));
        commentCount.setText(Integer.toString(recipe.getCommentsNum()));
        description.setText(recipe.getDesc());
        ingredients.setText(recipe.getIngredients());
        steps.setText(recipe.getSteps());
        uploadDate.setText(recipe.getSteps());

    }
}
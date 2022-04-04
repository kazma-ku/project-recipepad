package com.bcit.comp3717_recipe_pad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Intent intent = getIntent();

        ArrayList<String> recipeInfo = intent.getStringArrayListExtra("recipe");

//        System.out.println(recipe.getIngredients());
        passRecipeData(recipeInfo);

        ArrayList<String> comments = intent.getStringArrayListExtra("comments");

        String[] arrayComments = new String[comments.size()];
                 arrayComments = comments.toArray(arrayComments);
        setupRecyclerView(arrayComments);

        Button submitButton =  findViewById(R.id.button_recipe_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = findViewById(R.id.editText_recipe_addcomment);
                String comment = editText.getText().toString();
                if(comment != null && !comment.isEmpty())
                {
                    String recipeID = intent.getStringExtra("recipeID");

                    comments.add(comment);

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("recipes").document(recipeID)
                            .update("comments", comments).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                String[] arrayCommentsNew = new String[comments.size()];
                                arrayCommentsNew = comments.toArray(arrayCommentsNew);
                                setupRecyclerView(arrayCommentsNew);
                            }
                            else
                            {
                                Log.d("add comment", "failed");
                            }
                        }
                    });

                }
            }
        });

        Button followUser = findViewById(R.id.button_recipe_followuser);

        String userID = intent.getStringExtra("userID");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DocumentReference docRef = db.collection("users").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    User document = task.getResult().toObject(User.class);
                    ArrayList<String> followingList = (ArrayList<String>) document.getFollowingList();

                    if(followingList.contains(userID))
                    {
                        followUser.setText("Unfollow This User");
                    }
                }
                else
                {
                    Log.d("get following list", "failed");
                }
            }
        });

        followUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful())
                        {
                            User document = task.getResult().toObject(User.class);
                            ArrayList<String> followingList = (ArrayList<String>) document.getFollowingList();

                            if(!followingList.contains(userID))
                            {
                                followingList.add(userID);
                                db.collection("users").document(user.getUid())
                                        .update("followingList", followingList);
                                followUser.setText("Unfollow This User");
                            }
                            else
                            {
                                followingList.remove(userID);
                                db.collection("users").document(user.getUid())
                                        .update("followingList", followingList);
                                followUser.setText("Follow This User");
                            }
                        }
                        else
                        {
                            Log.d("get following list", "failed");
                        }
                    }
                });
            }
        });
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
//        TextView uploadDate = findViewById(R.id.textView_recipe_uploaddate);

        recipeName.setText(recipeInfo.get(0));
        likeCount.setText(recipeInfo.get(1));
        dislikeCount.setText(recipeInfo.get(2));
        commentCount.setText(recipeInfo.get(3));
        description.setText(recipeInfo.get(4));
        ingredients.setText(recipeInfo.get(5));
        steps.setText(recipeInfo.get(6));
//        uploadDate.setText(recipeInfo.get(7));
    }

    void setupRecyclerView(String[] comments)
    {
        RecyclerView rv = findViewById(R.id.recyclerView_recipe);

        CommentAdapter adapter = new CommentAdapter(comments);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));


        TextView numComments = findViewById(R.id.textView_recipe_totalcomments);
        numComments.setText(comments.length + " comments");
    }
}
package com.bcit.comp3717_recipe_pad;

import android.nfc.Tag;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class DataHandler {


    public static void addUser(User newUser, String userId) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").document(userId).set(newUser).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("debug", "user added!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("AddUser method", "user failed to add.");
            }
        });
    }

    public static void addRecipe(Recipe newRecipe) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //Will work once Firebase authentication is set up
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String userID = user.getUid();
            newRecipe.setUserID(userID);
        }

        db.collection("recipes")
                .add(newRecipe).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d("debug", "DocumentSnapshot added with ID: " + documentReference.getId());
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("debug", "Error adding document", e);
                    }
                });
    }

    public static void getUser(String userID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("users").document(userID);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    User user = document.toObject(User.class);
                    if (user != null)
                    Log.d("debug", user.getUsername().toString());
                } else {
                    Log.d("debug", "No doc");
                }
            }
        });

    }



}

package com.bcit.comp3717_recipe_pad;

import android.nfc.Tag;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DataHandler {

    public static void addMockData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            Log.w("debug", "No user logged in, cannot add mock data");
            return;
        }

        String userID = user.getUid();

        Recipe[] mockRecipes = {
            new Recipe(
                "images/placeholder.jpg",
                "Classic Spaghetti Carbonara",
                "A creamy Italian pasta dish with eggs, cheese, and pancetta",
                "400g spaghetti\n200g pancetta\n4 eggs\n100g parmesan\n2 cloves garlic\nSalt and pepper",
                "1. Cook spaghetti according to package\n2. Fry pancetta until crispy\n3. Beat eggs with parmesan\n4. Toss hot pasta with pancetta\n5. Remove from heat and mix in egg mixture\n6. Season and serve",
                "Calories: 650\nProtein: 25g\nCarbs: 75g\nFat: 28g",
                userID
            ),
            new Recipe(
                "images/placeholder.jpg",
                "Chicken Teriyaki Bowl",
                "Japanese-style glazed chicken over rice with vegetables",
                "500g chicken thigh\n2 cups rice\n1/2 cup soy sauce\n1/4 cup mirin\n2 tbsp sugar\nBroccoli\nCarrots",
                "1. Cook rice\n2. Mix soy sauce, mirin, and sugar for sauce\n3. Grill chicken and glaze with sauce\n4. Steam vegetables\n5. Assemble bowl with rice, chicken, and veggies",
                "Calories: 580\nProtein: 35g\nCarbs: 65g\nFat: 18g",
                userID
            ),
            new Recipe(
                "images/placeholder.jpg",
                "Avocado Toast with Poached Eggs",
                "Trendy breakfast with creamy avocado and perfectly poached eggs",
                "2 slices sourdough bread\n1 ripe avocado\n2 eggs\nChili flakes\nLemon juice\nSalt",
                "1. Toast bread until golden\n2. Mash avocado with lemon and salt\n3. Poach eggs in simmering water\n4. Spread avocado on toast\n5. Top with poached eggs and chili flakes",
                "Calories: 420\nProtein: 16g\nCarbs: 32g\nFat: 26g",
                userID
            ),
            new Recipe(
                "images/placeholder.jpg",
                "Beef Tacos",
                "Authentic Mexican street tacos with seasoned ground beef",
                "500g ground beef\n8 corn tortillas\n1 onion\n2 tomatoes\nCilantro\nLime\nTaco seasoning",
                "1. Brown ground beef with seasoning\n2. Warm tortillas\n3. Dice onion, tomatoes, and cilantro\n4. Assemble tacos with beef and toppings\n5. Squeeze lime on top",
                "Calories: 380\nProtein: 22g\nCarbs: 28g\nFat: 20g",
                userID
            ),
            new Recipe(
                "images/placeholder.jpg",
                "Vegetable Stir Fry",
                "Quick and healthy Asian vegetable stir fry with tofu",
                "400g firm tofu\n2 cups mixed vegetables\n3 tbsp soy sauce\n1 tbsp sesame oil\nGinger\nGarlic",
                "1. Press and cube tofu\n2. Stir fry tofu until golden\n3. Add vegetables and cook until tender-crisp\n4. Add sauce and toss\n5. Serve over rice or noodles",
                "Calories: 320\nProtein: 18g\nCarbs: 25g\nFat: 16g",
                userID
            ),
            new Recipe(
                "images/placeholder.jpg",
                "Chocolate Chip Cookies",
                "Classic homemade cookies that are crispy outside and chewy inside",
                "2 1/4 cups flour\n1 cup butter\n3/4 cup sugar\n3/4 cup brown sugar\n2 eggs\n1 tsp vanilla\n2 cups chocolate chips",
                "1. Cream butter and sugars\n2. Add eggs and vanilla\n3. Mix in flour gradually\n4. Fold in chocolate chips\n5. Bake at 375F for 10-12 minutes",
                "Calories: 180 per cookie\nProtein: 2g\nCarbs: 24g\nFat: 9g",
                userID
            )
        };

        // Set some likes/dislikes to make it interesting
        mockRecipes[0].setLikesNum(42);
        mockRecipes[0].setDislikesNum(3);
        mockRecipes[1].setLikesNum(38);
        mockRecipes[1].setDislikesNum(5);
        mockRecipes[2].setLikesNum(56);
        mockRecipes[2].setDislikesNum(8);
        mockRecipes[3].setLikesNum(29);
        mockRecipes[3].setDislikesNum(2);
        mockRecipes[4].setLikesNum(21);
        mockRecipes[4].setDislikesNum(4);
        mockRecipes[5].setLikesNum(67);
        mockRecipes[5].setDislikesNum(1);

        for (Recipe recipe : mockRecipes) {
            db.collection("recipes")
                .add(recipe)
                .addOnSuccessListener(documentReference -> {
                    Log.d("debug", "Mock recipe added: " + recipe.getTitle());
                })
                .addOnFailureListener(e -> {
                    Log.w("debug", "Error adding mock recipe", e);
                });
        }
    }

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

//    public static Recipe[] getTrending() {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//        Date threeDaysAgo = new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(3));
//
//        Timestamp cutOff = new Timestamp(threeDaysAgo);
//
//        List<Recipe> recipes = new ArrayList<>();
//
//        Log.d("before", "made it");
//
//        db.collection("recipes")
////                .whereGreaterThan("uploadDate", cutOff)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
//                                Log.d("RecipeGet", documentSnapshot.getId().toString());
//                                recipes.add(documentSnapshot.toObject(Recipe.class));
//                                recipes.sort(Comparator.comparingInt(Recipe::getLikesNum));
//                                Log.d("sortPart", recipes.toString());
//
//                            }
//                        } else {
//                            Log.d("else", "not success");
//                        }
//
////
////                        Recipe[] sorted = recipes.toArray(new Recipe[6]);
//
//                    }
//                });
//
//        return recipes.toArray(new Recipe[6]);
//
//    }

    public static void getTrending(OnSuccess o) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Date threeDaysAgo = new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(3));

        Timestamp cutOff = new Timestamp(threeDaysAgo);

        List<Recipe> recipes = new ArrayList<>();

        Log.d("before", "made it");

        db.collection("recipes")
                .whereGreaterThan("uploadDate", cutOff)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                Log.d("RecipeGet", documentSnapshot.getId().toString());
                                recipes.add(documentSnapshot.toObject(Recipe.class));

                            }
                        } else {
                            Log.d("else", "not success");
                        }
                        recipes.sort(Comparator.comparingInt(Recipe::getLikesNum));
                        Recipe[] sorted = recipes.toArray(new Recipe[recipes.size()]);
//                        setupRecyclerView(sorted);
                        o.onData(sorted);

                    }
                });


    }

    public static void getFeed(OnSuccess o) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<Recipe> recipes = new ArrayList<>();

        Log.d("before", "made it");

        DocumentReference docRef = db.collection("users").document(userID);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    User document = task.getResult().toObject(User.class);
                    List<String> followingList = document.getFollowingList();

                    if (followingList.isEmpty()) {
                        o.onData(recipes.toArray(new Recipe[0]));
                    } else {

                        db.collection("recipes")
                                .whereIn("userID", followingList).get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                                Log.d("RecipeGetFeed", documentSnapshot.getId().toString());
                                                recipes.add(documentSnapshot.toObject(Recipe.class));

                                            }
                                        } else {
                                            Log.d("else", "not success");
                                        }

                                        Comparator<Recipe> uploadsOrder =
                                                (r1, r2) -> {
                                                    return r2.getUploadDate().toDate().compareTo(r1.getUploadDate().toDate());
                                                };
                                        recipes.sort(uploadsOrder);
                                        Recipe[] sorted = recipes.toArray(new Recipe[recipes.size()]);
//                        setupRecyclerView(sorted); now is done through passing to onData below
                                        o.onData(sorted);

                                    }
                                });
                    }
                }
            }
        });
    }
}


package com.bcit.comp3717_recipe_pad;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class DataHandler {

    public static void addMockData(Context context) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            Log.w("debug", "No user logged in, cannot add mock data");
            return;
        }

        String userID = user.getUid();

        // Define mock recipe data with drawable resource IDs
        int[] imageResIds = {
            R.drawable.food_pasta,
            R.drawable.food_chicken,
            R.drawable.food_avocado_toast,
            R.drawable.food_tacos,
            R.drawable.food_stirfry,
            R.drawable.food_cookies
        };

        String[] titles = {
            "Classic Spaghetti Carbonara",
            "Chicken Teriyaki Bowl",
            "Avocado Toast with Poached Eggs",
            "Beef Tacos",
            "Vegetable Stir Fry",
            "Chocolate Chip Cookies"
        };

        String[] descriptions = {
            "A creamy Italian pasta dish with eggs, cheese, and pancetta",
            "Japanese-style glazed chicken over rice with vegetables",
            "Trendy breakfast with creamy avocado and perfectly poached eggs",
            "Authentic Mexican street tacos with seasoned ground beef",
            "Quick and healthy Asian vegetable stir fry with tofu",
            "Classic homemade cookies that are crispy outside and chewy inside"
        };

        String[] ingredients = {
            "400g spaghetti\n200g pancetta\n4 eggs\n100g parmesan\n2 cloves garlic\nSalt and pepper",
            "500g chicken thigh\n2 cups rice\n1/2 cup soy sauce\n1/4 cup mirin\n2 tbsp sugar\nBroccoli\nCarrots",
            "2 slices sourdough bread\n1 ripe avocado\n2 eggs\nChili flakes\nLemon juice\nSalt",
            "500g ground beef\n8 corn tortillas\n1 onion\n2 tomatoes\nCilantro\nLime\nTaco seasoning",
            "400g firm tofu\n2 cups mixed vegetables\n3 tbsp soy sauce\n1 tbsp sesame oil\nGinger\nGarlic",
            "2 1/4 cups flour\n1 cup butter\n3/4 cup sugar\n3/4 cup brown sugar\n2 eggs\n1 tsp vanilla\n2 cups chocolate chips"
        };

        String[] steps = {
            "1. Cook spaghetti according to package\n2. Fry pancetta until crispy\n3. Beat eggs with parmesan\n4. Toss hot pasta with pancetta\n5. Remove from heat and mix in egg mixture\n6. Season and serve",
            "1. Cook rice\n2. Mix soy sauce, mirin, and sugar for sauce\n3. Grill chicken and glaze with sauce\n4. Steam vegetables\n5. Assemble bowl with rice, chicken, and veggies",
            "1. Toast bread until golden\n2. Mash avocado with lemon and salt\n3. Poach eggs in simmering water\n4. Spread avocado on toast\n5. Top with poached eggs and chili flakes",
            "1. Brown ground beef with seasoning\n2. Warm tortillas\n3. Dice onion, tomatoes, and cilantro\n4. Assemble tacos with beef and toppings\n5. Squeeze lime on top",
            "1. Press and cube tofu\n2. Stir fry tofu until golden\n3. Add vegetables and cook until tender-crisp\n4. Add sauce and toss\n5. Serve over rice or noodles",
            "1. Cream butter and sugars\n2. Add eggs and vanilla\n3. Mix in flour gradually\n4. Fold in chocolate chips\n5. Bake at 375F for 10-12 minutes"
        };

        String[] nutrFacts = {
            "Calories: 650\nProtein: 25g\nCarbs: 75g\nFat: 28g",
            "Calories: 580\nProtein: 35g\nCarbs: 65g\nFat: 18g",
            "Calories: 420\nProtein: 16g\nCarbs: 32g\nFat: 26g",
            "Calories: 380\nProtein: 22g\nCarbs: 28g\nFat: 20g",
            "Calories: 320\nProtein: 18g\nCarbs: 25g\nFat: 16g",
            "Calories: 180 per cookie\nProtein: 2g\nCarbs: 24g\nFat: 9g"
        };

        int[] likes = {42, 38, 56, 29, 21, 67};
        int[] dislikes = {3, 5, 8, 2, 4, 1};

        // Check for existing mock recipes and only add ones that don't exist
        List<String> titleList = java.util.Arrays.asList(titles);
        db.collection("recipes")
            .whereIn("title", titleList)
            .get()
            .addOnSuccessListener(querySnapshot -> {
                // Collect existing titles
                List<String> existingTitles = new ArrayList<>();
                for (QueryDocumentSnapshot doc : querySnapshot) {
                    String title = doc.getString("title");
                    if (title != null) {
                        existingTitles.add(title);
                    }
                }

                // Upload each image and create recipe only if it doesn't exist
                for (int i = 0; i < imageResIds.length; i++) {
                    if (existingTitles.contains(titles[i])) {
                        Log.d("debug", "Mock recipe already exists, skipping: " + titles[i]);
                        continue;
                    }

                    final int index = i;
                    String imagePath = "foods/" + UUID.randomUUID() + ".jpeg";
                    StorageReference imageRef = storageRef.child(imagePath);

                    // Load bitmap from drawable
                    Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), imageResIds[i]);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
                    byte[] data = baos.toByteArray();

                    // Upload image then create recipe
                    imageRef.putBytes(data).addOnSuccessListener(taskSnapshot -> {
                        Log.d("debug", "Image uploaded: " + imagePath);

                        // Create recipe with uploaded image path
                        Recipe recipe = new Recipe(
                            imagePath,
                            titles[index],
                            descriptions[index],
                            ingredients[index],
                            steps[index],
                            nutrFacts[index],
                            userID
                        );
                        recipe.setLikesNum(likes[index]);
                        recipe.setDislikesNum(dislikes[index]);

                        db.collection("recipes")
                            .add(recipe)
                            .addOnSuccessListener(documentReference -> {
                                Log.d("debug", "Mock recipe added: " + titles[index]);
                            })
                            .addOnFailureListener(e -> {
                                Log.w("debug", "Error adding mock recipe", e);
                            });
                    }).addOnFailureListener(e -> {
                        Log.w("debug", "Error uploading image", e);
                    });
                }
            })
            .addOnFailureListener(e -> {
                Log.w("debug", "Error checking existing recipes", e);
            });
    }

    public static void deleteDuplicateRecipes() {
        Log.d("debug", "Starting duplicate recipe cleanup...");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();

        db.collection("recipes")
            .get()
            .addOnSuccessListener(querySnapshot -> {
                Log.d("debug", "Fetched " + querySnapshot.size() + " total recipes");
                Map<String, String> seenTitles = new HashMap<>();
                List<QueryDocumentSnapshot> duplicates = new ArrayList<>();

                for (QueryDocumentSnapshot doc : querySnapshot) {
                    String title = doc.getString("title");
                    if (title != null) {
                        if (seenTitles.containsKey(title)) {
                            duplicates.add(doc);
                            Log.d("debug", "Found duplicate: " + title);
                        } else {
                            seenTitles.put(title, doc.getId());
                        }
                    }
                }

                Log.d("debug", "Found " + duplicates.size() + " duplicate recipes to delete");

                for (QueryDocumentSnapshot duplicate : duplicates) {
                    String imagePath = duplicate.getString("img");
                    String docId = duplicate.getId();

                    // Delete the document
                    db.collection("recipes").document(docId).delete()
                        .addOnSuccessListener(aVoid -> {
                            Log.d("debug", "Deleted duplicate recipe: " + duplicate.getString("title"));

                            // Also delete the image from storage if it exists
                            if (imagePath != null && !imagePath.isEmpty()) {
                                storage.getReference().child(imagePath).delete()
                                    .addOnSuccessListener(aVoid2 -> {
                                        Log.d("debug", "Deleted image: " + imagePath);
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.w("debug", "Failed to delete image: " + imagePath, e);
                                    });
                            }
                        })
                        .addOnFailureListener(e -> {
                            Log.w("debug", "Failed to delete duplicate recipe", e);
                        });
                }
            })
            .addOnFailureListener(e -> {
                Log.w("debug", "Error fetching recipes for duplicate check", e);
            });
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


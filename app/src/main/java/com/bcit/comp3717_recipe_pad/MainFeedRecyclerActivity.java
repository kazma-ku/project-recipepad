package com.bcit.comp3717_recipe_pad;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class MainFeedRecyclerActivity extends RecyclerView.Adapter<com.bcit.comp3717_recipe_pad.MainFeedRecyclerActivity.ViewHolder> {

    private Recipe[] recipes;

    /**
     * Provide a reference to the type of views that you are using
     * This template comes with a TextView
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView img;
        private final TextView username;
        private final TextView desc;
        private final TextView likes;
        private final TextView dislkes;
        private final TextView comments;
        private final ImageView likeButton;
        private final ImageView dislikeButton;
        private final Button getRecipeButton;

        public ViewHolder(View view) {
            super(view);

            img = view.findViewById(R.id.imageView_feedItem_thumbnail);
            username = view.findViewById(R.id.textView_feedItem_username);
            desc = view.findViewById(R.id.textView_feeditem_description);
            likes = view.findViewById(R.id.textView_feedItem_likeCount);
            dislkes = view.findViewById(R.id.textView_feeditem_dislikesCount);
            comments = view.findViewById(R.id.textView_feeditem_commentCount);
            likeButton = view.findViewById(R.id.imageView_feeditem_likebutton);
            dislikeButton = view.findViewById(R.id.imageView_feeditem_dislikebutton);
            getRecipeButton = view.findViewById(R.id.button_feeditem_getRecipe);
        }

        public TextView getUsername() {
            return username;
        }

        public ImageView getImg() {
            return img;
        }

        public TextView getDesc() {
            return desc;
        }

        public TextView getLikes() {
            return likes;
        }

        public TextView getDislkes() {
            return dislkes;
        }

        public TextView getComments() {
            return comments;
        }

        public ImageView getLikeButton() {
            return likeButton;
        }

        public ImageView getDislikeButton() {
            return dislikeButton;
        }

        public Button getGetRecipeButton() {
            return getRecipeButton;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     *                by RecyclerView.
     */
    public MainFeedRecyclerActivity(Recipe[] dataSet) {
        recipes = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_main_feed, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {


        String foodImg = recipes[position].getImg();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imagesRef = storageRef.child(foodImg);

        final long ONE_MEGABYTE = 1024 * 1024;
        imagesRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                viewHolder.getImg().setImageBitmap(bmp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("debug", "unsuccess");
            }
        });

        //displays title instead of username for now
        viewHolder.getUsername().setText(recipes[position].getTitle());
        viewHolder.getDesc().setText(recipes[position].getDesc());
        viewHolder.getLikes().setText(String.valueOf(recipes[position].getLikesNum()));
        viewHolder.getDislkes().setText(String.valueOf(recipes[position].getDislikesNum()));
        viewHolder.getComments().setText(String.valueOf(recipes[position].getCommentsNum()));

        viewHolder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                if (!recipes[viewHolder.getAdapterPosition()].isLiked()) {

                    int oldCount = Integer.valueOf(viewHolder.getLikes().getText().toString());
                    oldCount++;
                    int finalCount = oldCount;

                    db.collection("recipes").document(recipes[viewHolder.getAdapterPosition()].getRecipeID())
                            .update("likesNum", oldCount).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                viewHolder.getLikes().setText(String.valueOf(finalCount));
                            } else {
                                Log.d("increment", "failed");
                            }
                        }
                    });

                    recipes[viewHolder.getAdapterPosition()].setLiked(true);
                    viewHolder.likeButton.setImageResource(R.drawable.ic_baseline_thumb_up_green24);

                    if (recipes[viewHolder.getAdapterPosition()].isDisliked())
                    {
                        int oldCountDislike = Integer.valueOf(viewHolder.getDislkes().getText().toString());
                        oldCountDislike--;
                        int finalCountDislike = oldCountDislike;

                        db.collection("recipes").document(recipes[viewHolder.getAdapterPosition()].getRecipeID())
                                .update("dislikesNum", oldCountDislike).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    viewHolder.getDislkes().setText(String.valueOf(finalCountDislike));
                                } else {
                                    Log.d("decrement", "failed");
                                }
                            }
                        });

                        recipes[viewHolder.getAdapterPosition()].setDisliked(false);
                        viewHolder.dislikeButton.setImageResource(R.drawable.ic_baseline_thumb_down_24);
                    }
                }
                else
                {
                    int oldCount = Integer.valueOf(viewHolder.getLikes().getText().toString());
                    oldCount--;
                    int finalCount = oldCount;

                    db.collection("recipes").document(recipes[viewHolder.getAdapterPosition()].getRecipeID())
                            .update("likesNum", oldCount).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                viewHolder.getLikes().setText(String.valueOf(finalCount));
                            } else {
                                Log.d("decrement", "failed");
                            }
                        }
                    });

                    recipes[viewHolder.getAdapterPosition()].setLiked(false);
                    viewHolder.likeButton.setImageResource(R.drawable.ic_baseline_thumb_up_24);
                }
            }
        });

        viewHolder.dislikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                if (!recipes[viewHolder.getAdapterPosition()].isDisliked()) {

                    int oldcount = Integer.valueOf(viewHolder.getDislkes().getText().toString());
                    oldcount++;
                    int finalCount = oldcount;
                    db.collection("recipes").document(recipes[viewHolder.getAdapterPosition()].getRecipeID())
                            .update("dislikesNum", oldcount).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                viewHolder.getDislkes().setText(String.valueOf(finalCount));
                            } else {
                                Log.d("increment", "failed");
                            }
                        }
                    });

                    recipes[viewHolder.getAdapterPosition()].setDisliked(true);
                    viewHolder.dislikeButton.setImageResource(R.drawable.ic_baseline_thumb_down_red24);

                    if (recipes[viewHolder.getAdapterPosition()].isLiked())
                    {
                        int oldCountLike = Integer.valueOf(viewHolder.getLikes().getText().toString());
                        oldCountLike--;
                        int finalCountLike = oldCountLike;

                        db.collection("recipes").document(recipes[viewHolder.getAdapterPosition()].getRecipeID())
                                .update("likesNum", oldCountLike).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    viewHolder.getLikes().setText(String.valueOf(finalCountLike));
                                } else {
                                    Log.d("decrement", "failed");
                                }
                            }
                        });

                        recipes[viewHolder.getAdapterPosition()].setLiked(false);
                        viewHolder.likeButton.setImageResource(R.drawable.ic_baseline_thumb_up_24);
                    }

                }
                else
                {
                    int oldCount = Integer.valueOf(viewHolder.getDislkes().getText().toString());
                    oldCount--;
                    int finalCount = oldCount;

                    db.collection("recipes").document(recipes[viewHolder.getAdapterPosition()].getRecipeID())
                            .update("dislikesNum", oldCount).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                viewHolder.getDislkes().setText(String.valueOf(finalCount));
                            } else {
                                Log.d("decrement", "failed");
                            }
                        }
                    });

                    recipes[viewHolder.getAdapterPosition()].setDisliked(false);
                    viewHolder.dislikeButton.setImageResource(R.drawable.ic_baseline_thumb_down_24);
                }

            }
        });

        viewHolder.getRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Recipe recipe = recipes[viewHolder.getAdapterPosition()];

                ArrayList<String> recipeInfo = new ArrayList<>();
                recipeInfo.add(recipe.getTitle());
                recipeInfo.add(String.valueOf(recipe.getLikesNum()));
                recipeInfo.add(String.valueOf(recipe.getDislikesNum()));
                recipeInfo.add(String.valueOf(recipe.getCommentsNum()));
                recipeInfo.add(recipe.getDesc());
                recipeInfo.add(recipe.getIngredients());
                recipeInfo.add(recipe.getSteps());

                Intent intent = new Intent(view.getContext(), RecipeActivity.class);

                intent.putStringArrayListExtra("recipe", recipeInfo);
                intent.putStringArrayListExtra("comments", recipe.getComments());
                intent.putExtra("recipeID", recipe.getRecipeID());
                intent.putExtra("userID", recipe.getUserID());

                view.getContext().startActivity(intent);
            }
        });


    }

    //
    @Override
    public int getItemCount() {
        return recipes.length;
    }

}
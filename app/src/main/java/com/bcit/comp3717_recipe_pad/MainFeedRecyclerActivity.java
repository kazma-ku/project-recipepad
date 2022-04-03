package com.bcit.comp3717_recipe_pad;

import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;


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

        //needs to be updates when we figure out images
        viewHolder.getImg().setImageResource(R.drawable._35_8354885_macaroni_and_cheese_clipart_transparent_frozen_mac_and);

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

                int count = Integer.valueOf(viewHolder.getLikes().getText().toString());
                count ++;
                db.collection("recipes").document(recipes[position].getRecipeID())
                        .update("likesNum", count);

//                viewHolder.getLikes().setText(String.valueOf(recipes[position].getLikesNum()));
                System.out.println("this should increment");
            }
        });

        viewHolder.dislikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                int count = Integer.valueOf(viewHolder.getDislkes().getText().toString());
                count ++;
                db.collection("recipes").document(recipes[position].getRecipeID())
                        .update("dislikesNum", count);

//                viewHolder.getLikes().setText(String.valueOf(recipes[position].getLikesNum()));
                System.out.println("this should increment");
            }
        });

        viewHolder.getRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Recipe recipe = recipes[position];
                Intent intent = new Intent(view.getContext(), RecipeActivity.class);

                intent.putExtra("recipe", (Parcelable) recipe);
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
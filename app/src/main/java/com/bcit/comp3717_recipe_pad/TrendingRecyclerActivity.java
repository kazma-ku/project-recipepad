package com.bcit.comp3717_recipe_pad;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class TrendingRecyclerActivity extends RecyclerView.Adapter<TrendingRecyclerActivity.ViewHolder> {

    private Recipe[] recipes;

    /**
     * Provide a reference to the type of views that you are using
     * This template comes with a TextView
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView img;
        private final TextView title;
        private final TextView desc;
        private final TextView likes;
        private final TextView dislkes;
        private final TextView comments;
        private final LinearLayout trendingFeed;

        public ViewHolder(View view) {
            super(view);

            img = view.findViewById(R.id.imageView_item_thumbnail);
            title = view.findViewById(R.id.textView_item_title);
            desc = view.findViewById(R.id.textView_item_desc);
            likes = view.findViewById(R.id.textView_item_likeCount);
            dislkes = view.findViewById(R.id.textView_item_dislikeCount);
            comments = view.findViewById(R.id.textView_item_commentCount);
            trendingFeed = view.findViewById(R.id.linearlayout_trending_feed);
        }

        public TextView getTitle() {
            return title;
        }
        public ImageView getImg() { return img;}
        public TextView getDesc() { return desc;}
        public TextView getLikes() { return likes;}
        public TextView getDislkes() { return dislkes;}
        public TextView getComments() { return comments;}
        public LinearLayout getTrendingFeed() { return trendingFeed; }

    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     *                by RecyclerView.
     */
    public TrendingRecyclerActivity(Recipe[] dataSet) {
        recipes = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_trending_feed, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        viewHolder.getImg().setImageResource(R.drawable._35_8354885_macaroni_and_cheese_clipart_transparent_frozen_mac_and);
        viewHolder.getTitle().setText(recipes[position].getTitle());
        viewHolder.getDesc().setText(recipes[position].getDesc());
        viewHolder.getLikes().setText(String.valueOf(recipes[position].getLikesNum()));
        viewHolder.getDislkes().setText(String.valueOf(recipes[position].getDislikesNum()));
        viewHolder.getComments().setText(String.valueOf(recipes[position].getCommentsNum()));

        viewHolder.trendingFeed.setOnClickListener(new View.OnClickListener() {
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
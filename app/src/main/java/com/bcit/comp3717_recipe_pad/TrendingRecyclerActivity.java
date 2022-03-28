package com.bcit.comp3717_recipe_pad;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


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

        public ViewHolder(View view) {
            super(view);

            img = view.findViewById(R.id.imageView_item_thumbnail);
            title = view.findViewById(R.id.textView_item_title);
            desc = view.findViewById(R.id.textView_item_desc);
            likes = view.findViewById(R.id.textView_item_likeCount);
            dislkes = view.findViewById(R.id.textView_item_dislikeCount);
            comments = view.findViewById(R.id.textView_item_commentCount);
        }

        public TextView getTitle() {
            return title;
        }
        public ImageView getImg() { return img;}
        public TextView getDesc() { return desc;}
        public TextView getLikes() { return likes;}
        public TextView getDislkes() { return dislkes;}
        public TextView getComments() { return comments;}

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


//        viewHolder.getImg().setImageBitmap();
        viewHolder.getTitle().setText(recipes[position].getTitle());
        viewHolder.getDesc().setText(recipes[position].getDesc());
        viewHolder.getLikes().setText(String.valueOf(recipes[position].getLikesNum()));
        viewHolder.getDislkes().setText(String.valueOf(recipes[position].getDislikesNum()));
        viewHolder.getComments().setText(String.valueOf(recipes[position].getCommentsNum()));
    }

    //
    @Override
    public int getItemCount() {
        return recipes.length;
    }
}
package com.bcit.comp3717_recipe_pad;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class ProfileRecyclerActivity extends RecyclerView.Adapter<ProfileRecyclerActivity.ViewHolder> {

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
        private final TextView dislikes;
        private final TextView comments;


        public ViewHolder(View view) {
            super(view);

            img = view.findViewById(R.id.imageView_item_thumbnail);
            title = view.findViewById(R.id.textView_item_title);
            desc = view.findViewById(R.id.textView_item_desc);
            likes = view.findViewById(R.id.textView_item_likeCount);
            dislikes = view.findViewById(R.id.textView_item_dislikeCount);
            comments = view.findViewById(R.id.textView_item_commentCount);
        }

        public TextView getTitle() {
            return title;
        }
        public ImageView getImg() { return img;}
        public TextView getDesc() { return desc;}
        public TextView getLikes() { return likes;}
        public TextView getDislkes() { return dislikes;}
        public TextView getComments() { return comments;}
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     *                by RecyclerView.
     */
    public ProfileRecyclerActivity(Recipe[] dataSet) {
        recipes = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_trending_feed, viewGroup, false); //error here should be expected, this is a template

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        String foodImg = recipes[position].getImg();

        // Check if foodImg is a drawable resource ID (numeric string)
        try {
            int resId = Integer.parseInt(foodImg);
            viewHolder.getImg().setImageResource(resId);
        } catch (NumberFormatException e) {
            // Fall back to Firebase Storage for non-mock data
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
        }

        viewHolder.getTitle().setText(recipes[position].getTitle());
        viewHolder.getDesc().setText(recipes[position].getDesc());
        viewHolder.getLikes().setText(String.valueOf(recipes[position].getLikesNum()));
        viewHolder.getDislkes().setText(String.valueOf(recipes[position].getDislikesNum()));
        viewHolder.getComments().setText(String.valueOf(recipes[position].getCommentsNum()));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return recipes.length;
    }
}
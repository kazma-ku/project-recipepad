package com.bcit.comp3717_recipe_pad;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;
import com.google.type.Date;

import java.io.Serializable;
import java.sql.Time;

public class Recipe implements Serializable, Parcelable {

    public Recipe(){};

    // tbd depending on how we want to store images in db
    private String img;

    private String title;
    private String desc;
    private int likesNum;
    private int dislikesNum;
    private int commentsNum;
    private String ingredients;
    private String steps;
    private String nutrFacts;
    private Timestamp uploadDate;
    private String userID;
    private String recipeID;
    private boolean liked;

    public Recipe(String img, String title, String desc, String ingredients, String steps, String nutrFacts, String userID) {
        this.img = img;
        this.title = title;
        this.desc = desc;
        this.ingredients = ingredients;
        this.steps = steps;
        this.likesNum = 0;
        this.dislikesNum = 0;
        this.commentsNum = 0;
        this.nutrFacts = nutrFacts;
        this.userID = userID;
        this.uploadDate = Timestamp.now();
        this.recipeID = "";
        this.liked = false;
    }

    public Recipe(String img, String title, String desc, int likesNum, int dislikesNum,
                  int commentsNum, String ingredients, String steps, String nutrFacts, String userID) {
        this.img = img;
        this.title = title;
        this.desc = desc;
        this.likesNum = likesNum;
        this.dislikesNum = dislikesNum;
        this.commentsNum = commentsNum;
        this.ingredients = ingredients;
        this.steps = steps;
        this.nutrFacts = nutrFacts;
        this.userID = userID;
        this.recipeID = "";
        this.liked = false;
    }

    protected Recipe(Parcel in) {
        img = in.readString();
        title = in.readString();
        desc = in.readString();
        likesNum = in.readInt();
        dislikesNum = in.readInt();
        commentsNum = in.readInt();
        ingredients = in.readString();
        steps = in.readString();
        nutrFacts = in.readString();
        uploadDate = in.readParcelable(Timestamp.class.getClassLoader());
        userID = in.readString();
        recipeID = in.readString();
        liked = in.readByte() != 0;
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getLikesNum() {
        return likesNum;
    }

    public void setLikesNum(int likesNum) {
        this.likesNum = likesNum;
    }

    public int getDislikesNum() {
        return dislikesNum;
    }

    public void setDislikesNum(int dislikesNum) {
        this.dislikesNum = dislikesNum;
    }

    public int getCommentsNum() {
        return commentsNum;
    }

    public void setCommentsNum(int commentsNum) {
        this.commentsNum = commentsNum;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getNutrFacts() {
        return nutrFacts;
    }

    public void setNutrFacts(String nutrFacts) {
        this.nutrFacts = nutrFacts;
    }

    public Timestamp getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Timestamp uploadDate) {
        this.uploadDate = uploadDate;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public String getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(String recipeID) {
        this.recipeID = recipeID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(img);
        parcel.writeString(title);
        parcel.writeString(desc);
        parcel.writeInt(likesNum);
        parcel.writeInt(dislikesNum);
        parcel.writeInt(commentsNum);
        parcel.writeString(ingredients);
        parcel.writeString(steps);
        parcel.writeString(nutrFacts);
        parcel.writeParcelable(uploadDate, i);
        parcel.writeString(userID);
        parcel.writeString(recipeID);
        parcel.writeByte((byte) (liked ? 1 : 0));
    }
}

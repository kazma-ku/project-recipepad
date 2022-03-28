package com.bcit.comp3717_recipe_pad;

public class Recipe {

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
    private String uploadDate;
    private String userID;

    public Recipe(String img, String title, String desc, String ingredients, String steps, String nutrFacts) {
        this.img = img;
        this.title = title;
        this.desc = desc;
        this.ingredients = ingredients;
        this.steps = steps;
        this.likesNum = 0;
        this.dislikesNum = 0;
        this.commentsNum = 0;
        this.nutrFacts = nutrFacts;
    }

    public Recipe(String img, String title, String desc, int likesNum, int dislikesNum, int commentsNum, String ingredients, String steps) {
        this.img = img;
        this.title = title;
        this.desc = desc;
        this.likesNum = likesNum;
        this.dislikesNum = dislikesNum;
        this.commentsNum = commentsNum;
        this.ingredients = ingredients;
        this.steps = steps;
    }

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

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }
}

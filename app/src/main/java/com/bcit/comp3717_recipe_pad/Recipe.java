package com.bcit.comp3717_recipe_pad;

public class Recipe {

    // tbd depending on how we want to store images in db
    private String img;

    private String title;
    private String desc;
    private int likesNum;
    private int dislikesNum;
    private int commentsNum;

    public Recipe(String img, String title, String desc) {
        this.img = img;
        this.title = title;
        this.desc = desc;
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
}

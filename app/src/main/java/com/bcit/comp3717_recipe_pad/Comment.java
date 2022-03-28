package com.bcit.comp3717_recipe_pad;

public class Comment {

    private String id;
    private int likesCount;
    private int dislikesCount;

    public Comment(String id, int likesCount, int dislikesCount)
    {
        this.id = id;
        this.likesCount = likesCount;
        this.dislikesCount = dislikesCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public int getDislikesCount() {
        return dislikesCount;
    }

    public void setDislikesCount(int dislikesCount) {
        this.dislikesCount = dislikesCount;
    }
}

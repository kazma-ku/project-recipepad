package com.bcit.comp3717_recipe_pad;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String username;
    public String email;

    private int followerCount;
    private int followingCount;

    private List<String> ownRecipes;
    private List<String> savedRecipes;

    public User() {}


    public User(String username, String email) {
        this.username = username;
        this.email = email;
        this.followerCount = 0;
        this.followingCount = 0;
        this.ownRecipes = new ArrayList<String>();
        this.savedRecipes = new ArrayList<String>();
    }

    public User(String username, String email, int followerCount, int followingCount) {
        this.username = username;
        this.email = email;
        this.followerCount = followerCount;
        this.followingCount = followingCount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }

    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    public List<String> getOwnRecipes() {
        return ownRecipes;
    }

    public void setOwnRecipes(List<String> ownRecipes) {
        this.ownRecipes = ownRecipes;
    }

    public List<String> getSavedRecipes() {
        return savedRecipes;
    }

    public void setSavedRecipes(List<String> savedRecipes) {
        this.savedRecipes = savedRecipes;
    }
}

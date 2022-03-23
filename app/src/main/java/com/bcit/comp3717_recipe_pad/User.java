package com.bcit.comp3717_recipe_pad;

public class User {

    private String username;
    public String email;

    //firebase can handle unique id + password authentication, don't need to store password here but likely id.
    private String id;

    private String followerCount;
    private String followingCount;


    public User(String username, String email) {
        this.username = username;
        this.email = email;
        //other data is set when retrieving from firestore
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(String followerCount) {
        this.followerCount = followerCount;
    }

    public String getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(String followingCount) {
        this.followingCount = followingCount;
    }
}

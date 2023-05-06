package com.example.project2_gameshop_v2;

public class userItem {
    private int mImageResource;
    private String mUserName;

    public userItem(int imageResource, String userName) {
        mImageResource = imageResource;
        mUserName = userName;
    }

    public int getImageResource() {
        return mImageResource;
    }

    public String getUserName() {
        return mUserName;
    }

}

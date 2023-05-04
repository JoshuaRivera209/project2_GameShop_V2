package com.example.project2_gameshop_v2;

public class gameItem {
    private int mImageResource;
    private String mGameTitle;
    private String mGameDescription;

    public gameItem(int imageResource, String gameTitle, String gameDescription) {
        mImageResource = imageResource;
        mGameTitle = gameTitle;
        mGameDescription = gameDescription;
    }

    public int getImageResource() {
        return mImageResource;
    }

    public String getGameTitle() {
        return mGameTitle;
    }

    public String getGameDescription() {
        return mGameDescription;
    }
}

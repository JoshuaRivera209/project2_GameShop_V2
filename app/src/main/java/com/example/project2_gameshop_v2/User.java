package com.example.project2_gameshop_v2;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.project2_gameshop_v2.db.AppDataBase;

import java.util.ArrayList;

@Entity(tableName = AppDataBase.USER_TABLE)
public class User {

    @PrimaryKey(autoGenerate = true)
    private int mUserId;

    private String mUserName;
    private String mPassword;
    private boolean mIsAdmin;
    private ArrayList<Game> mGameList;

    public User(String userName, String password, boolean isAdmin) {
        mUserName = userName;
        mPassword = password;
        mIsAdmin = isAdmin;
        mGameList = new ArrayList<>();
    }

    public ArrayList<Game> getGameList() {
        return mGameList;
    }

    public void setGameList(ArrayList<Game> gameList) {
        mGameList = gameList;
    }

    public boolean isAdmin() {
        return mIsAdmin;
    }

    public void setAdmin(boolean admin) {
        mIsAdmin = admin;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }
}

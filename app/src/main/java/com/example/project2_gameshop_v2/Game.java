package com.example.project2_gameshop_v2;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.project2_gameshop_v2.db.AppDataBase;

@Entity(tableName = AppDataBase.GAME_TABLE)
public class Game {

    @PrimaryKey(autoGenerate = true)
    private int mGameId;

    private String mGameName;
    private double mPrice;
    private String mDescription;
    private int mCopies;

    public Game(String gameName, double price, String description, int copies) {
        mGameName = gameName;
        mPrice = price;
        mDescription = description;
        mCopies = copies;
    }

    public int getGameId() {
        return mGameId;
    }

    public void setGameId(int gameId) {
        mGameId = gameId;
    }

    public String getGameName() {
        return mGameName;
    }

    public void setGameName(String gameName) {
        mGameName = gameName;
    }

    public double getPrice() {
        return mPrice;
    }

    public void setPrice(double price) {
        mPrice = price;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public int getCopies() {
        return mCopies;
    }

    public void setCopies(int copies) {
        mCopies = copies;
    }
}

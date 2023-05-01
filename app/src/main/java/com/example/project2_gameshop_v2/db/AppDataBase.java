package com.example.project2_gameshop_v2.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.project2_gameshop_v2.Game;
import com.example.project2_gameshop_v2.ListTypeConverter;
import com.example.project2_gameshop_v2.User;

@Database(entities = {Game.class, User.class}, version = 2)
@TypeConverters(ListTypeConverter.class)
public abstract class AppDataBase extends RoomDatabase {
    public static final String DATABASE_NAME = "Game.db";
    public static final String GAME_TABLE = "game_table";
    public static final String USER_TABLE = "USER_TABLE";

    public abstract GameShopDAO getGameShopDAO();


    private static volatile AppDataBase instance;
    private static final Object LOCK = new Object();

    public static AppDataBase getInstance(Context context) {
        if(instance == null) {
            synchronized (LOCK){
                if(instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDataBase.class,
                            DATABASE_NAME).build();
                }
            }
        }
        return instance;
    }

}

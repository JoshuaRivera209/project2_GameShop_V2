package com.example.project2_gameshop_v2.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.project2_gameshop_v2.Game;
import com.example.project2_gameshop_v2.User;

import java.util.List;

@Dao
public interface GameShopDAO {

    @Insert
    void insert(User...users);

    @Update
    void update(User...users);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM " + AppDataBase.USER_TABLE)
    List<User> getAllUsers();

    @Query("SELECT * FROM " + AppDataBase.USER_TABLE + " WHERE mUserName = :username ")
    User getUserByUsername(String username);

    @Query("SELECT * FROM " + AppDataBase.USER_TABLE + " WHERE mUserId = :userId ")
    User getUserById(int userId);

    @Insert
    void insert(Game...games);

    @Update
    void update(Game...games);

    @Delete
    void delete(Game games);

    @Query("SELECT * FROM " + AppDataBase.GAME_TABLE)
    List<Game> getAllGames();
}

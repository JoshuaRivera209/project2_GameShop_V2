package com.example.project2_gameshop_v2;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListTypeConverter {

//    Gson gson = new Gson();

    @TypeConverter
    public static ArrayList<Game> stringToGameList(String data){
//        if(data == null){
//            return Collections.emptyList();
//        }
        Type arrayListType = new TypeToken<ArrayList<Game>>(){}.getType();
        return new Gson().fromJson(data, arrayListType);

    }

    @TypeConverter
    public static String GameListToString(ArrayList<Game> Games){
        Gson gson = new Gson();
        return gson.toJson(Games);
    }

}
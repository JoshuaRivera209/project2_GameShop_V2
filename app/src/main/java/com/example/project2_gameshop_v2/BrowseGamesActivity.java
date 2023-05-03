package com.example.project2_gameshop_v2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.project2_gameshop_v2.db.AppDataBase;
import com.example.project2_gameshop_v2.db.GameShopDAO;

import java.util.List;

public class BrowseGamesActivity extends AppCompatActivity {

    private GameShopDAO mGameShopDAO;
    private List<Game> allGames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_games);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        getDatabase();
        displayGames();
    }

    private void displayGames() {
        for (Game g : allGames) {
            g.toString();
        }
    }

    public static Intent intentFactory(Context context) {
        Intent intent = new Intent(context, BrowseGamesActivity.class);
        return intent;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id==android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getDatabase() {
        mGameShopDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
                .getGameShopDAO();

        allGames = mGameShopDAO.getAllGames();
    }
}
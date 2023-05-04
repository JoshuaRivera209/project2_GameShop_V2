package com.example.project2_gameshop_v2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.project2_gameshop_v2.db.AppDataBase;
import com.example.project2_gameshop_v2.db.GameShopDAO;

import java.util.List;

public class ViewOwnedActivity extends AppCompatActivity {

    private static final String USER_ID_KEY = "com.example.project2_gameshop_v2.userIdKey";
    private static final String PREFERENCES_KEY = "com.example.project2_gameshop_v2.PREFERENCES_KEY";

    private SharedPreferences mPreferences;
    private int mUserId;
//    private List<Game> ownedGames;
    private User mUser;
    private GameShopDAO mGameShopDAO;
    TextView ownedGamesTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_owned);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getDatabase();
        getUserPrefs();
        displayOwnedGames();
    }

    private void displayOwnedGames() {
        ownedGamesTextView = findViewById(R.id.ownedGamesTextView);
        ownedGamesTextView.setMovementMethod(new ScrollingMovementMethod());
        if (mUser.getGameList().size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (Game g : mUser.getGameList()) {
                sb.append(g.toString());
            }
            ownedGamesTextView.setText(sb.toString());
        } else if (mUser.getGameList() == null || mUser.getGameList().isEmpty()) {
            ownedGamesTextView.setText("You don't own any games! Go spend some money!");
        }
    }

    private void getPrefs() {
        mPreferences = this.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
    }

    private void getUserPrefs() {
        if(mPreferences == null){
            getPrefs();
        }
        mUserId = mPreferences.getInt(USER_ID_KEY, -1);
        mUser = mGameShopDAO.getUserById(mUserId);
    }

    private void getDatabase() {
        mGameShopDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
                .getGameShopDAO();
    }

    public static Intent intentFactory(Context context) {
        Intent intent = new Intent(context, ViewOwnedActivity.class);
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
}
package com.example.project2_gameshop_v2.userActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.project2_gameshop_v2.Game;
import com.example.project2_gameshop_v2.GameAdapter;
import com.example.project2_gameshop_v2.R;
import com.example.project2_gameshop_v2.User;
import com.example.project2_gameshop_v2.db.AppDataBase;
import com.example.project2_gameshop_v2.db.GameShopDAO;
import com.example.project2_gameshop_v2.gameItem;

import java.util.ArrayList;

public class ReturnGamesActivity extends AppCompatActivity {

    private static final String USER_ID_KEY = "com.example.project2_gameshop_v2.userIdKey";
    private static final String PREFERENCES_KEY = "com.example.project2_gameshop_v2.PREFERENCES_KEY";

    private RecyclerView mRecyclerView;
    private GameAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private SharedPreferences mPreferences;
    private int mUserId;
    private User mUser;
    private GameShopDAO mGameShopDAO;
    private ArrayList<gameItem> gameItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_games);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getDatabase();
        getUserPrefs();
        getReturnedGames();


    }

    private void getReturnedGames() {
        gameItemList = new ArrayList<>();
        for (Game g : mUser.getGameList()) {
            gameItemList.add(new gameItem(R.drawable.ic_controller, g.getGameName(), g.getDescription()));
        }
        mRecyclerView = findViewById(R.id.returnGamesRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new GameAdapter(gameItemList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new GameAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                returnItem(position);
            }
        });

    }

    private void returnItem(int position) {
        String gameReturn = gameItemList.get(position).getGameTitle();
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setMessage("Confirm Return: Are you sure you would like to return this game?");

        alertBuilder.setPositiveButton(getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO: implement return logic
                        // logic goes before remove item is called
                        Game currentGameReturn = mGameShopDAO.getGameByName(gameReturn);
                        int currentCopies = currentGameReturn.getCopies();
                        currentGameReturn.setCopies(currentCopies + 1);
                        mGameShopDAO.update(currentGameReturn);
                        mUser.getGameList().remove(position);
                        mGameShopDAO.update(mUser);
                        removeItem(position);
                        Toast.makeText(ReturnGamesActivity.this, "Return Successful!", Toast.LENGTH_LONG).show();
                    }
                });
        alertBuilder.setNegativeButton(getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // we dont really need to do anything here.
                    }
                });
        alertBuilder.create().show();
    }

    private void removeItem(int position) {
        gameItemList.remove(position);
        mAdapter.notifyItemRemoved(position);
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
        Intent intent = new Intent(context, ReturnGamesActivity.class);
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
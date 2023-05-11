package com.example.project2_gameshop_v2.adminActivities;

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
import com.example.project2_gameshop_v2.userActivities.ReturnGamesActivity;

import java.util.ArrayList;
import java.util.List;

import io.github.muddz.styleabletoast.StyleableToast;

public class DeleteGamesActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private GameAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private GameShopDAO mGameShopDAO;
    private ArrayList<gameItem> gameItemList;
    private List<Game> mGameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_games);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getDatabase();
        getAllGames();
    }

    private void getAllGames() {
        gameItemList = new ArrayList<>();
        mGameList = mGameShopDAO.getAllGames();
        for (Game g : mGameList) {
            gameItemList.add(new gameItem(R.drawable.ic_controller, g.getGameName(), g.getDescription()));
        }
        mRecyclerView = findViewById(R.id.deleteGamesRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new GameAdapter(gameItemList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new GameAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                deleteItem(position);
            }
        });
    }

    private void deleteItem(int position) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setMessage("Confirm Deletion: Are you sure you would like to delete this game from the database? Once deleted data cannot be retrieved.");

        alertBuilder.setPositiveButton(getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Game gameToDelete = mGameShopDAO.getGameByName(mGameList.get(position).getGameName());
                        mGameShopDAO.delete(gameToDelete);
                        removeItem(position);
                        StyleableToast.makeText(DeleteGamesActivity.this, "Deletion Successful!", R.style.successDeleteGameToast).show();
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

    private void getDatabase() {
        mGameShopDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
                .getGameShopDAO();
    }

    public static Intent intentFactory(Context context) {
        Intent intent = new Intent(context, DeleteGamesActivity.class);
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
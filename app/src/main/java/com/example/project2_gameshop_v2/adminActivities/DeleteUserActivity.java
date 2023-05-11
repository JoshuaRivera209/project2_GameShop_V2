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
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.project2_gameshop_v2.Game;
import com.example.project2_gameshop_v2.GameAdapter;
import com.example.project2_gameshop_v2.R;
import com.example.project2_gameshop_v2.User;
import com.example.project2_gameshop_v2.UserAdapter;
import com.example.project2_gameshop_v2.db.AppDataBase;
import com.example.project2_gameshop_v2.db.GameShopDAO;
import com.example.project2_gameshop_v2.gameItem;
import com.example.project2_gameshop_v2.userItem;

import java.util.ArrayList;
import java.util.List;

import io.github.muddz.styleabletoast.StyleableToast;

public class DeleteUserActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private UserAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private GameShopDAO mGameShopDAO;
    private ArrayList<userItem> userItemList;
    private List<User> mUserList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_user);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getDatabase();
        getAllUsers();
    }

    private void getAllUsers() {
        userItemList = new ArrayList<>();
        mUserList = mGameShopDAO.getAllUsers();
        for (User u : mUserList) {
            userItemList.add(new userItem(R.drawable.ic_user_box_icon, u.getUserName()));
        }
        mRecyclerView = findViewById(R.id.deleteUsersRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new UserAdapter(userItemList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new UserAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                deleteItem(position);
            }
        });
    }

    private void deleteItem(int position) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setMessage("Confirm Deletion: Are you sure you would like to delete this item from the database? Once deleted data cannot be retrieved.");

        alertBuilder.setPositiveButton(getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        User userToDelete = mGameShopDAO.getUserByUsername(mUserList.get(position).getUserName());
                        mGameShopDAO.delete(userToDelete);
                        removeItem(position);
                        StyleableToast.makeText(DeleteUserActivity.this, "Deletion Successful!", R.style.successDeleteUserToast).show();
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
        userItemList.remove(position);
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
        Intent intent = new Intent(context, DeleteUserActivity.class);
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
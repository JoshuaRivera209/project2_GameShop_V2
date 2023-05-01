package com.example.project2_gameshop_v2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.project2_gameshop_v2.db.AppDataBase;
import com.example.project2_gameshop_v2.db.GameShopDAO;

import java.util.List;

public class LandingPageActivity extends AppCompatActivity {

    private static final String USER_ID_KEY = "com.example.project2_gameshop_v2.userIdKey";
    private static final String PREFERENCES_KEY = "com.example.project2_gameshop_v2.PREFERENCES_KEY";
    private TextView mLandingDisplay;
    private Button mAdminButton;
    private Button mBrowseButton;
    private Button mSearchButton;
    private Button mReturnButton;
    private Button mViewOwnedButton;

    private GameShopDAO mGameShopDAO;

    private int mUserId = -1;
    private User mUser;

    private SharedPreferences mPreferences = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        getDatabase();
        getPrefs();
        wireupDisplay();
        checkForUser();
        loginUser(mUserId);
    }

    private void getDatabase() {
        mGameShopDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
                .getGameShopDAO();
    }

    private void wireupDisplay() {
        mBrowseButton = findViewById(R.id.buttonBrowseGames);
        mBrowseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browseIntent = BrowseGamesActivity.intentFactory(getApplicationContext());
                startActivity(browseIntent);
            }
        });
        mSearchButton = findViewById(R.id.buttonSearchGames);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent searchIntent = SearchGamesActivity.intentFactory(getApplicationContext());
                startActivity(searchIntent);
            }
        });
        mViewOwnedButton = findViewById(R.id.buttonViewOwnedGames);
        mViewOwnedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewOwnedIntent = ViewOwnedActivity.intentFactory(getApplicationContext());
                startActivity(viewOwnedIntent);
            }
        });
        mReturnButton = findViewById(R.id.buttonReturnGames);
        mReturnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnGamesIntent = ReturnGamesActivity.intentFactory(getApplicationContext());
                startActivity(returnGamesIntent);
            }
        });
        mAdminButton = findViewById(R.id.buttonManageApp);
        mAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent adminIntent = ManageAppActivity.intentFactory(getApplicationContext());
                startActivity(adminIntent);
            }
        });
    }

//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.buttonBrowseGames:
//                Intent browseIntent = BrowseGamesActivity.intentFactory(getApplicationContext());
//                startActivity(browseIntent);
//                break;
//
//            case R.id.buttonSearchGames:
//                Intent searchIntent = SearchGamesActivity.intentFactory(getApplicationContext());
//                startActivity(searchIntent);
//                break;
//
//            case R.id.buttonViewOwnedGames:
//                Intent viewOwnedIntent = ViewOwnedActivity.intentFactory(getApplicationContext());
//                startActivity(viewOwnedIntent);
//                break;
//
//            case R.id.buttonReturnGames:
//                Intent returnGamesIntent = ReturnGamesActivity.intentFactory(getApplicationContext());
//                startActivity(returnGamesIntent);
//                break;
//
//            case R.id.buttonManageApp:
//                Intent adminIntent = ManageAppActivity.intentFactory(getApplicationContext());
//                startActivity(adminIntent);
//                break;
//        }
//    }

    private void checkForUser() {
        // do we have user in intent?
        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);
        if (mUserId != -1) {
            return;
        }

        if(mPreferences != null){
            getPrefs();
        }
        mUserId = mPreferences.getInt(USER_ID_KEY, -1);

        if(mUserId != -1) {
            return;
        }

        // do we have any users at all?
        List<User> users = mGameShopDAO.getAllUsers();
        if(users.size() <= 0) {
            User defaultUser = new User("testuser1", "testuser1", false);
            mGameShopDAO.insert(defaultUser);
            mGameShopDAO.update(defaultUser);
            User adminUser = new User("admin2", "admin2", true);
            mGameShopDAO.insert(adminUser);
            mGameShopDAO.update(adminUser);
        }

        Intent intent = LoginActivity.intentFactory(this);
        startActivity(intent);
    }

    private void getPrefs() {
        mPreferences = this.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
    }

    private void loginUser(int userId) {
        mUser = mGameShopDAO.getUserById(userId);
        checkForAdmin(mUser);
        addUserToPreference(userId);
        invalidateOptionsMenu();
    }

    private void checkForAdmin(User user) {
        if(user.isAdmin()) {
            mAdminButton.setVisibility(View.VISIBLE);
            return;
        }
        mAdminButton.setVisibility(View.INVISIBLE);
    }

    private void clearUserFromIntent() {
        getIntent().putExtra(USER_ID_KEY, -1);
    }

    private void clearUserFromPref() {
        addUserToPreference(-1);
    }

    private void addUserToPreference(int userId) {
        if(mPreferences == null){
            getPrefs();
        }
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(USER_ID_KEY, userId);
        editor.apply();
    }

    private void logoutUser() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setMessage(R.string.logout);

        alertBuilder.setPositiveButton(getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearUserFromIntent();
                        clearUserFromPref();
                        mUserId = -1;
                        Intent intent = LoginActivity.intentFactory(getApplicationContext());
                        startActivity(intent);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.landing_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(mUser != null) {
            MenuItem item = menu.findItem(R.id.currentUser);
            item.setTitle(mUser.getUserName());
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.landingMenuLogout:
                logoutUser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static Intent intentFactory(Context context, int userId) {
        Intent intent = new Intent(context, LandingPageActivity.class);
        intent.putExtra(USER_ID_KEY, userId);
        return intent;
    }

}
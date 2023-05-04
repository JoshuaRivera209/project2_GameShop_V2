package com.example.project2_gameshop_v2.userActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project2_gameshop_v2.Game;
import com.example.project2_gameshop_v2.R;
import com.example.project2_gameshop_v2.User;
import com.example.project2_gameshop_v2.db.AppDataBase;
import com.example.project2_gameshop_v2.db.GameShopDAO;

import java.util.ArrayList;
import java.util.List;

public class SearchGamesActivity extends AppCompatActivity {

    private static final String USER_ID_KEY = "com.example.project2_gameshop_v2.userIdKey";
    private static final String PREFERENCES_KEY = "com.example.project2_gameshop_v2.PREFERENCES_KEY";

    private SharedPreferences mPreferences = null;

    AutoCompleteTextView mAutoCompleteTextView;
    TextView mTextView;
    Button mButton;
    private GameShopDAO mGameShopDAO;
    List<Game> allGames;
    ArrayList<String> gameTitles;
    ArrayAdapter<String> mArrayAdapter;
    private String selectedGame;

    private int mUserId = -1;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_games);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getDatabase();
        wireupDisplay();
        getUserPrefs();

        gameTitles = new ArrayList<>();
        for (Game g : allGames) {
            gameTitles.add(g.getGameName());
        }
        gameTitles.toArray();

        mArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, gameTitles);
        mAutoCompleteTextView.setAdapter(mArrayAdapter);
        mAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedGame = mArrayAdapter.getItem(position);
                mTextView.setText(mGameShopDAO.getGameByName(selectedGame).toString());
                mAutoCompleteTextView.setText("");
                if (mGameShopDAO.getGameByName(selectedGame).getCopies() != 1) {
                    mButton.setText("Sold Out");
                }
                closeSearchKeyboard();
                mButton.setVisibility(View.VISIBLE);
            }
        });
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

        allGames = mGameShopDAO.getAllGames();
    }

    private void wireupDisplay() {
        mAutoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        mTextView = findViewById(R.id.searchTextView);
        mButton = findViewById(R.id.inSearchButton);
        mButton.setVisibility(View.INVISIBLE);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mGameShopDAO.getGameByName(selectedGame).getCopies() != 1) {
                    soldOutAlert();
                } else {
                    purchaseItem();
                }

            }
        });
    }

    private void closeSearchKeyboard() {
        View currentFocus = this.getCurrentFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
    }

    private void soldOutAlert() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setMessage("Out of Stock: Sorry, this game is currently sold out. Please select another game or check back later.");

        alertBuilder.setPositiveButton(getString(R.string.okay),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        alertBuilder.create().show();
    }

    private void purchaseItem() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setMessage("Confirm Purchase: Are you sure you would like to purchase this game?");

        alertBuilder.setPositiveButton(getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Game purchasedGame = mGameShopDAO.getGameByName(selectedGame);
                        int currentCopies = purchasedGame.getCopies();
                        purchasedGame.setCopies(currentCopies - 1);
                        mGameShopDAO.update(purchasedGame);
                        mUser.getGameList().add(purchasedGame);
                        mGameShopDAO.update(mUser);
                        Toast.makeText(SearchGamesActivity.this, "Purchase Successful!", Toast.LENGTH_LONG).show();
                        mTextView.setText(mGameShopDAO.getGameByName(selectedGame).toString());
                        if (mGameShopDAO.getGameByName(selectedGame).getCopies() != 1) {
                            mButton.setText("Sold Out");
                        }
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

    public static Intent intentFactory(Context context) {
        Intent intent = new Intent(context, SearchGamesActivity.class);
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
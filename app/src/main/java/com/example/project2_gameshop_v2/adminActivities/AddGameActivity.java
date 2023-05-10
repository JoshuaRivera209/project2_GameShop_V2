package com.example.project2_gameshop_v2.adminActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project2_gameshop_v2.Game;
import com.example.project2_gameshop_v2.R;
import com.example.project2_gameshop_v2.db.AppDataBase;
import com.example.project2_gameshop_v2.db.GameShopDAO;
import com.example.project2_gameshop_v2.userActivities.SearchGamesActivity;

import java.util.List;

public class AddGameActivity extends AppCompatActivity {

    private EditText mGameTitleEditText, mGameDescriptionEditText, mGamePriceEditText, mGameCopiesEditText;
    private Button mAddGameButton;
    private String GameTitle, GameDescription;
    private double GamePrice;
    private int GameCopies;

    private GameShopDAO mGameShopDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getDatabase();
        wireupDisplay();
    }

    private void wireupDisplay() {
        mGameTitleEditText = findViewById(R.id.editTextAddGameName);
        mGameDescriptionEditText = findViewById(R.id.editTextAddGameDesc);
        mGamePriceEditText = findViewById(R.id.editTextAddGamePrice);
        mGameCopiesEditText = findViewById(R.id.editTextAddGameCopies);
        mAddGameButton = findViewById(R.id.buttonAddGameToDatabase);
        mAddGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!getValuesFromDisplay()) {
                    Toast.makeText(AddGameActivity.this, "One or more fields is invalid! (Hint: Must have at least 1 copy, price cannot be 0, and absolutely no blank fields!)", Toast.LENGTH_LONG).show();
                    return;
                } else if (!validGame()){
                    Toast.makeText(AddGameActivity.this, "This game already exists in the database!", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    addGameToDatabase();
                }

            }
        });
    }

    private boolean validGame() {
        if (mGameShopDAO.getGameByName(GameTitle) == null) {
            return true;
        }
        return false;
    }

    private void addGameToDatabase() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setMessage("Confirm: Are you sure you would like to add this game to the database?");

        alertBuilder.setPositiveButton(getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Game newGame = new Game(GameTitle, GamePrice, GameDescription, GameCopies);
                        mGameShopDAO.insert(newGame);
                        Toast.makeText(AddGameActivity.this, "Game successfully added!", Toast.LENGTH_LONG).show();
                        setFieldsBlank();
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

    private void setFieldsBlank() {
        mGameTitleEditText.setText("");
        mGameDescriptionEditText.setText("");
        mGamePriceEditText.setText("");
        mGameCopiesEditText.setText("");
    }

    private boolean getValuesFromDisplay() {
        try {
            GameTitle = mGameTitleEditText.getText().toString();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        try {
            GameDescription = mGameDescriptionEditText.getText().toString();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        try {
            GamePrice = Double.parseDouble(mGamePriceEditText.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        try {
            GameCopies = Integer.parseInt(mGameCopiesEditText.getText().toString());
        } catch (NumberFormatException e ) {
            e.printStackTrace();
            return false;
        }
         if (GameTitle == null || GameDescription == null || GamePrice <= 0.00 || GameCopies <= 0) {
             return false;
         }
         return true;
    }

    private void getDatabase() {
        mGameShopDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
                .getGameShopDAO();
    }

    public static Intent intentFactory(Context context) {
        Intent intent = new Intent(context, AddGameActivity.class);
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
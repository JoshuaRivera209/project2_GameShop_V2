package com.example.project2_gameshop_v2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project2_gameshop_v2.db.AppDataBase;
import com.example.project2_gameshop_v2.db.GameShopDAO;

import java.util.List;

public class CreateProfileActivity extends AppCompatActivity {

    EditText username, password;
    private String mUsernameString, mPasswordString;
    private Button mSignupButton, mExistingLoginButton;

    private GameShopDAO mGameShopDAO;

    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        wireupDisplay();
        getDatabase();
    }

    private void wireupDisplay() {
        username = findViewById(R.id.editTextSignupUserName);
        password = findViewById(R.id.editTextSignupPassWord);

        mSignupButton = findViewById(R.id.buttonSignUp);
        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getValuesFromDisplay();
                if (!checkForUserInDatabase()) {
                    addUserToDatabase(mUser);
                    Intent intent = LoginActivity.intentFactory(getApplicationContext());
                    startActivity(intent);
                }
            }
        });

        mExistingLoginButton = findViewById(R.id.buttonSendToLogin);
        mExistingLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = LoginActivity.intentFactory(getApplicationContext());
                startActivity(intent);
            }
        });
    }

    private void getValuesFromDisplay() {
        mUsernameString = username.getText().toString();
        mPasswordString = password.getText().toString();
    }

    private void getDatabase() {
        mGameShopDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
                .getGameShopDAO();
    }

    public static Intent intentFactory(Context context) {
        Intent intent = new Intent(context, CreateProfileActivity.class);
        return intent;
    }

    private boolean checkForUserInDatabase() {
        mUser = mGameShopDAO.getUserByUsername(mUsernameString);
        if(mUser != null) {
            Toast.makeText(this, "User: " + mUsernameString + " already exists!", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    private void addUserToDatabase(User newUser) {
        newUser = new User(mUsernameString, mPasswordString, false);
        mGameShopDAO.insert(newUser);
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
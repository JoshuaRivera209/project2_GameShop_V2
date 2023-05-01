package com.example.project2_gameshop_v2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.project2_gameshop_v2.db.AppDataBase;
import com.example.project2_gameshop_v2.db.GameShopDAO;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    private String mUsernameString, mPasswordString;
    private Button mLoginButton;

    private GameShopDAO mGameShopDAO;

    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        wireupDisplay();
        getDatabase();
    }

    private void wireupDisplay() {
        username = findViewById(R.id.editTextLoginUserName);
        password = findViewById(R.id.editTextLoginPassWord);

        mLoginButton = findViewById(R.id.buttonLogin);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getValuesFromDisplay();
                if(checkForUserInDatabase()) {
                    if(!validatePassword()) {
                        Toast.makeText(LoginActivity.this, "Invalid password", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = LandingPageActivity.intentFactory(getApplicationContext(), mUser.getUserId());
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private boolean validatePassword() {
        return mUser.getPassword().equals(mPasswordString);
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

        List<User> users = mGameShopDAO.getAllUsers();
        if (users.size() <= 0) {
            User defaultUser = new User("testuser1", "testuser1", false);
            User adminUser = new User("admin2", "admin2", true);
            mGameShopDAO.insert(defaultUser, adminUser);
        }
    }

    public static Intent intentFactory(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }

    private boolean checkForUserInDatabase() {
        mUser = mGameShopDAO.getUserByUsername(mUsernameString);
        if(mUser == null) {
            Toast.makeText(this, "no user " + mUsernameString + " found", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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

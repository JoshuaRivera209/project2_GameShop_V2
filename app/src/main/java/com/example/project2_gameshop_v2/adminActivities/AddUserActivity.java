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
import android.widget.Toast;

import com.example.project2_gameshop_v2.Game;
import com.example.project2_gameshop_v2.R;
import com.example.project2_gameshop_v2.User;
import com.example.project2_gameshop_v2.db.AppDataBase;
import com.example.project2_gameshop_v2.db.GameShopDAO;
import com.example.project2_gameshop_v2.startupActivities.LoginActivity;

public class AddUserActivity extends AppCompatActivity {

    private EditText mAddUsername, mAddPassword, mPasswordConfirm;
    private Button mAddUserButton;
    private String addUserNameString, addUserPasswordString, addUserPasswordConfirmString;
    private User mUser;

    private GameShopDAO mGameShopDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getDatabase();
        wireupDisplay();
    }

    private void wireupDisplay() {
        mAddUsername = findViewById(R.id.editTextAddUsername);
        mAddPassword = findViewById(R.id.editTextAddUserPassword);
        mPasswordConfirm = findViewById(R.id.editTextAddUserPasswordConfirm);
        mAddUserButton = findViewById(R.id.buttonAddUser);
        mAddUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getValuesFromDisplay();
                addUserToDatabaseAlert();
            }
        });
    }

    private void getValuesFromDisplay() {
        addUserNameString = mAddUsername.getText().toString();
        addUserPasswordString = mAddPassword.getText().toString();
        addUserPasswordConfirmString = mPasswordConfirm.getText().toString();
    }

    private boolean checkForUserInDatabase() {
        mUser = mGameShopDAO.getUserByUsername(addUserNameString);
        if(mUser != null) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

            alertBuilder.setMessage("Error: User: " + addUserNameString + " already exists. Please use a different username.");

            alertBuilder.setPositiveButton(getString(R.string.okay),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // We dont have to do anything here.
                        }
                    });
            alertBuilder.create().show();
            return true;
        }
        return false;
    }

    private void addUserToDatabase(User newUser) {
        newUser = new User(addUserNameString, addUserPasswordString, false);
        mGameShopDAO.insert(newUser);
    }

    private void addUserToDatabaseAlert() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setMessage("Confirm: Are you sure you would like to add a user to the database?");

        alertBuilder.setPositiveButton(getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!passwordCheck()) {
                            if (!checkForUserInDatabase()) {
                                addUserToDatabase(mUser);
                                Toast.makeText(AddUserActivity.this, "User successfully added!", Toast.LENGTH_LONG).show();
                                setFieldsBlank();
                            }
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

    private void setFieldsBlank() {
        mAddUsername.setText("");
        mAddPassword.setText("");
        mPasswordConfirm.setText("");
    }

    private boolean passwordCheck() {
        if (!addUserPasswordString.equals(addUserPasswordConfirmString)) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

            alertBuilder.setMessage("Error: Passwords do not match.");

            alertBuilder.setPositiveButton(getString(R.string.okay),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // We dont have to do anything here.
                        }
                    });
            alertBuilder.create().show();
            return true;
        }
        return false;
    }

    private void getDatabase() {
        mGameShopDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
                .getGameShopDAO();

    }

    public static Intent intentFactory(Context context) {
        Intent intent = new Intent(context, AddUserActivity.class);
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
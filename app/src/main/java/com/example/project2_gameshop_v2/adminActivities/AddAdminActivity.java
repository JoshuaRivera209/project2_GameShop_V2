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

import com.example.project2_gameshop_v2.R;
import com.example.project2_gameshop_v2.User;
import com.example.project2_gameshop_v2.db.AppDataBase;
import com.example.project2_gameshop_v2.db.GameShopDAO;

import io.github.muddz.styleabletoast.StyleableToast;

public class AddAdminActivity extends AppCompatActivity {

    private EditText mAddAdminUsername, mAddAdminPassword, mAddAdminPasswordConfirm;
    private Button mAddAdminButton;
    private String addAdminUsernameString, addAdminPasswordString, addAdminPasswordConfirmString;
    private User mUser;

    private GameShopDAO mGameShopDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_admin);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getDatabase();
        wireupDisplay();
    }

    private void wireupDisplay() {
        mAddAdminUsername = findViewById(R.id.editTextAddAdminUsername);
        mAddAdminPassword = findViewById(R.id.editTextAddAdminPassword);
        mAddAdminPasswordConfirm = findViewById(R.id.editTextAddAdminPasswordConfirm);
        mAddAdminButton = findViewById(R.id.buttonAddAdmin);
        mAddAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getValuesFromDisplay();
                addAdminToDatabaseAlert();
            }
        });
    }

    private void getValuesFromDisplay() {
        addAdminUsernameString = mAddAdminUsername.getText().toString();
        addAdminPasswordString = mAddAdminPassword.getText().toString();
        addAdminPasswordConfirmString = mAddAdminPasswordConfirm.getText().toString();
    }

    private boolean checkForUserInDatabase() {
        mUser = mGameShopDAO.getUserByUsername(addAdminUsernameString);
        if(mUser != null) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

            alertBuilder.setMessage("Error: User: " + addAdminUsernameString + " already exists. Please use a different username.");

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

    private void addAdminToDatabase(User newAdmin) {
        newAdmin = new User(addAdminUsernameString, addAdminPasswordString, true);
        mGameShopDAO.insert(newAdmin);
    }

    private void addAdminToDatabaseAlert() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setMessage("Confirm: Are you sure you would like to add a user to the database?");

        alertBuilder.setPositiveButton(getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!passwordCheck()) {
                            if (!checkForUserInDatabase()) {
                                addAdminToDatabase(mUser);
                                StyleableToast.makeText(AddAdminActivity.this, "User successfully added!", R.style.successToast).show();
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
        mAddAdminUsername.setText("");
        mAddAdminPassword.setText("");
        mAddAdminPasswordConfirm.setText("");
    }

    private boolean passwordCheck() {
        if (!addAdminPasswordString.equals(addAdminPasswordConfirmString)) {
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
        Intent intent = new Intent(context, AddAdminActivity.class);
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
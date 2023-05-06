package com.example.project2_gameshop_v2.adminActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.project2_gameshop_v2.R;

public class ManageUsersActivity extends AppCompatActivity {

    Button mDeleteUserButton, mAddUserButton, mAddAdminButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        wireupDisplay();
    }

    private void wireupDisplay() {
        mDeleteUserButton = findViewById(R.id.deleteUserButton);
        mDeleteUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent deleteUserIntent = DeleteUserActivity.intentFactory(getApplicationContext());
                startActivity(deleteUserIntent);
            }
        });
        mAddUserButton = findViewById(R.id.addUserButton);
        mAddUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addUserIntent = AddUserActivity.intentFactory(getApplicationContext());
                startActivity(addUserIntent);
            }
        });
        mAddAdminButton = findViewById(R.id.addAdminButton);
        mAddAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addAdminIntent = AddAdminActivity.intentFactory(getApplicationContext());
                startActivity(addAdminIntent);
            }
        });
    }

    public static Intent intentFactory(Context context) {
        Intent intent = new Intent(context, ManageUsersActivity.class);
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
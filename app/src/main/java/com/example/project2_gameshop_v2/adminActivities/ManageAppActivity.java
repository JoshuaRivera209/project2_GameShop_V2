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

public class ManageAppActivity extends AppCompatActivity {

    private Button mManageGamesButton, mManageUsersButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_app);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        wireupDisplay();
    }

    private void wireupDisplay() {
        mManageGamesButton = findViewById(R.id.buttonManageGames);
        mManageGamesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent manageGamesIntent = ManageGamesActivity.intentFactory(getApplicationContext());
                startActivity(manageGamesIntent);
            }
        });
        mManageUsersButton = findViewById(R.id.buttonManageUsers);
        mManageUsersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent manageUsersIntent = ManageUsersActivity.intentFactory(getApplicationContext());
                startActivity(manageUsersIntent);
            }
        });
    }

    public static Intent intentFactory(Context context) {
        Intent intent = new Intent(context, ManageAppActivity.class);
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
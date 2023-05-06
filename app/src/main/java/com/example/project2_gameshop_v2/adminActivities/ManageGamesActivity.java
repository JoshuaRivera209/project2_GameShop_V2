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
import com.example.project2_gameshop_v2.userActivities.BrowseGamesActivity;

public class ManageGamesActivity extends AppCompatActivity {

    private Button deleteGameButton, addGameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_games);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        wireupDisplay();
    }

    private void wireupDisplay() {
        deleteGameButton = findViewById(R.id.deleteGameButton);
        deleteGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent deleteGamesIntent = DeleteGamesActivity.intentFactory(getApplicationContext());
                startActivity(deleteGamesIntent);
            }
        });
        addGameButton = findViewById(R.id.addGameButton);
        addGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent deleteGamesIntent = AddGameActivity.intentFactory(getApplicationContext());
                startActivity(deleteGamesIntent);
            }
        });
    }


    public static Intent intentFactory(Context context) {
        Intent intent = new Intent(context, ManageGamesActivity.class);
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
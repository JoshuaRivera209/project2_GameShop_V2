package com.example.project2_gameshop_v2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

public class ViewOwnedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_owned);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public static Intent intentFactory(Context context) {
        Intent intent = new Intent(context, ViewOwnedActivity.class);
//        intent.putExtra(USER_ID_KEY, userId);
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
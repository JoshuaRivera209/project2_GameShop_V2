package com.example.project2_gameshop_v2.startupActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.project2_gameshop_v2.R;
import com.example.project2_gameshop_v2.userActivities.LandingPageActivity;

public class MainActivity extends AppCompatActivity {

    private Button mLoginButton, mCreateProfileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoginButton = findViewById(R.id.buttonSendToLogin);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = LoginActivity.intentFactory(getApplicationContext());
                startActivity(intent);
            }
        });

        mCreateProfileButton = findViewById(R.id.buttonSendToCreateProfile);
        mCreateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = CreateProfileActivity.intentFactory(getApplicationContext());
                startActivity(intent);
            }
        });
    }

    public static Intent intentFactory(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }
}
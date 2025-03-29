package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class BadgeActivity extends AppCompatActivity {

    private ImageView badgeImageView;
    private TextView badgeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badge2);

        badgeImageView = findViewById(R.id.badgeImageView);
        badgeTextView = findViewById(R.id.badgeTextView);

        // Get the badge name from the Intent
        Intent intent = getIntent();
        String badgeName = intent.getStringExtra("badgeName");

        // Update UI based on the badge name
        if (badgeName != null) {
            badgeTextView.setText("You earned the " + badgeName + " badge!");

            // Set badge image based on the badge name
            switch (badgeName) {
                case "Gold":
                    badgeImageView.setImageResource(R.drawable.baseline_shield_24);
                    break;
                case "Silver":
                    badgeImageView.setImageResource(R.drawable.baseline_shield_silver);
                    break;
                case "Bronze":
                    badgeImageView.setImageResource(R.drawable.baseline_shield_bronze);
                    break;
                default:
                    badgeImageView.setImageResource(R.drawable.baseline_shield_bronze);
                    break;
            }
        }
    }
}
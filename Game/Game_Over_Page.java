package com.example.project.Game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project.BadgeActivity;
import com.example.project.R;
import com.example.project.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Game_Over_Page extends AppCompatActivity
{
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    public String updatedBadge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over_page);
        Game current_game = new Game();

        // Get data from intent
        int correctAnswers = Game.correct_answers;
        int totalQuestions = current_game.MAX_QUESTION;

        writeNewScore(correctAnswers);

        // Calculate percentage
        int percentage = (correctAnswers * 100) / totalQuestions;

        // Determine star rating
        ImageView Star1 = findViewById(R.id.Star1);
        ImageView Star2 = findViewById(R.id.Star2);
        ImageView Star3 = findViewById(R.id.Star3);

        //set the colours of the stars
        if (percentage >= 80)
        {
            Star1.setColorFilter(Color.rgb(255,215,0));
            Star2.setColorFilter(Color.rgb(255,215,0));
            Star3.setColorFilter(Color.rgb(255,215,0));
        }
        else if (percentage >= 50)
        {
            Star1.setColorFilter(Color.rgb(255,215,0));
            Star2.setColorFilter(Color.rgb(255,215,0));
            Star3.setColorFilter(Color.rgb(128,128,128));
        }
        else
        {
            Star1.setColorFilter(Color.rgb(255,215,0));
            Star2.setColorFilter(Color.rgb(128,128,128));
            Star3.setColorFilter(Color.rgb(128,128,128));
        }


        // Display score
        String score_text = "You scored " + percentage + "%!";
        TextView tvScore = findViewById(R.id.Score);
        tvScore.setText(score_text);

        // Restart button
        Button btnRestart = findViewById(R.id.btnRestart);
        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Game.correct_answers = 0;
                Intent intent = new Intent(Game_Over_Page.this, Game.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void writeNewScore(int score) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getUid();
        Handler handler = new Handler();


        if (userId != null) {
            mDatabase.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    int currentScore = 0; // Default score
                    String currentBadge = ""; // Default badge
                    String username = ""; // Default badge

                    // Check if the user already exists in the database
                    if (snapshot.exists()) {
                        User existingUser = snapshot.getValue(User.class); // Retrieve the existing User object
                        if (existingUser != null) {
                            currentScore = existingUser.getNumberOfAnswers();
                            currentBadge = existingUser.getBadge();
                            username = existingUser.getUsername();
                        }
                    }

                    // Update the user's score and badge
                    int updatedScore = currentScore + score; // Add new score to the existing score

                    // If you want to update the badge conditionally, you can do it here
                    // For example, only update the badge if the user has earned a new one
                    updatedBadge = currentBadge; // Default to current badge, if no changes

                    if (updatedScore >= 30 && !currentBadge.equals("Gold") && currentBadge.equals("Silver"))
                    {
                        updatedBadge = "Gold";
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent i = new Intent(Game_Over_Page.this, BadgeActivity.class);
                                i.putExtra("badgeName","Gold");
                                startActivity(i);
                            }
                        },1000);
                    } else if (updatedScore >= 20 && !currentBadge.equals("Sliver") && currentBadge.equals("Bronze") )
                    {
                        updatedBadge = "Silver";
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent i = new Intent(Game_Over_Page.this, BadgeActivity.class);
                                i.putExtra("badgeName","Silver");
                                startActivity(i);
                            }
                        },1000);
                    }
                    else if (updatedScore >= 10 && !currentBadge.equals("Bronze")&& currentBadge.equals("Default"))
                    {
                        updatedBadge = "Bronze";

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent i = new Intent(Game_Over_Page.this, BadgeActivity.class);
                                i.putExtra("badgeName","Bronze");
                                startActivity(i);
                            }
                        },1000);

                    }

                    // Create a new User object with the updated score and badge
                    User updatedUser = new User(updatedScore, updatedBadge,username);

                    // Update the database with the new User object
                    mDatabase.child(userId).setValue(updatedUser)
                            .addOnSuccessListener(aVoid -> Log.d("Firebase", "User updated successfully"))
                            .addOnFailureListener(e -> Log.e("Firebase", "Error updating user", e));
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Log.e("Firebase", "Failed to read data", error.toException());
                }
            });
        }


        // 50 correct answers = bronze
        //100 correct answers - silver
        //200 correct answers - gold


        //main menu right after reaching one of these targets then show
        //i only want to show this once
        // b -s-g true/false
        // only on change to true do we show it


        //database check if tally gets into a new bracket then call activity for badges
    }
}
package com.example.project.Game;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.project.MainActivity;
import com.example.project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Game extends AppCompatActivity
{
    ImageView imageView;
    private DatabaseReference mDatabase;
    TextView question_text;
    Button button, button2, button3,button4;
    static int count = 0;
    static int correct_answers = 0;
    final int MAX_QUESTION = 8;
    Random random = new Random();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //return count to zero every time the game starts
        count = 0;

        //Initialize buttons and text
        //todo - change names of all these buttons you idiot

        question_text = (TextView) findViewById(R.id.game_question);
        button = (Button) findViewById(R.id.button4);
        button2 = (Button) findViewById(R.id.button5);
        button3 = (Button) findViewById(R.id.button6);
        button4 = (Button) findViewById(R.id.button7);

        //close button
        //todo - change the variable name of this too
        imageView = (ImageView) findViewById(R.id.back_button);
        imageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(Game.this, MainActivity.class);
                startActivity(i);
                finish();
            }//onClick
        });//setOnClickListener

        //call the function to access questions in database, the questions are put into a hash map, where the game loop is called using the hashmap
        //generated
        database_link();
    }//onCreate

    public void database_link()
    {
        //get the instance of the database and give it the correct path to the questions
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Question");
        mDatabase.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                HashMap<String,String> questionsMap = new HashMap<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    //assigning the values to the hashmap
                    //answers = key
                    //questions = value
                    String key = dataSnapshot.getKey();
                    String value = (String) dataSnapshot.getValue();

                    questionsMap.put(key,value);
                }//for

                //calling the start of the game loop(Generating questions) using the hashmap generated from the database
                questions_and_answers(questionsMap);
            }//onDataChange
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                //if the call to the database fails
                Log.e("Firebase", "Error fetching data", databaseError.toException());
            }//onCancelled
        });//addValueEventListener
    }//database_link


    public void questions_and_answers(HashMap<String, String> questionsMap)
    {
        //spilt the hashmap into two arraylists so random number can be generated and accessed
        List<String> answers = new ArrayList<>(questionsMap.keySet());
        List<String> questions = new ArrayList<>(questionsMap.values());

        //generate a random number and get that from the parallel arraylists
        int correctIndex = random.nextInt(questions.size());

        String correctAnswer = answers.get(correctIndex);
        String question = questions.get(correctIndex);

        //remove the question from the hashmap so questions do not repeat
        questionsMap.remove(correctAnswer);



        // create a new list that contains the correct answers plus 3 random incorrect answers
        List<String> options = new ArrayList<>();
        options.add(correctAnswer);

        while (options.size() < 4)
        {
            String randomAnswer = answers.get(random.nextInt(answers.size()));

            //For testing purposes
            //todo- REMOVE
            if (answers.size() < 4 && answers.get(correctIndex) != randomAnswer)
            {
                options.add(randomAnswer);
            }//if

            //this should never add the same answer, just a check to make sure it never occurs
            //todo - keep or remove?

            if (!options.contains(randomAnswer))
            {
                options.add(randomAnswer);
            }//if
        }//while

        //shuffle answers in the list to place them in random buttons
        Collections.shuffle(options);


        //set the buttons text to the list that has been shuffled
        question_text.setText(question);
        button.setText(options.get(0));
        button2.setText(options.get(1));
        button3.setText(options.get(2));
        button4.setText(options.get(3));

        //call the game loop using the hashmap, that has the current question removed and the correct answer
        game_loop(correctAnswer, questionsMap);
    }


    public void game_loop(String correct_Answer, HashMap<String, String> questionsMap)
    {
        //everytime this function is called a question has been asked
        count++;
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.correct_answer3);
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        ProgressBar progressBar = findViewById(R.id.progress_bar);
        progressBar.incrementProgressBy(10);

        Handler handler = new Handler();

        //onclick listener for all the buttons
        //this handles all of the ui and ux changes to inform if question was answered correctly or not
        View.OnClickListener listener = v ->
        {
            Button clickedButton = (Button) v;

            // Disable all buttons
            button.setEnabled(false);
            button2.setEnabled(false);
            button3.setEnabled(false);
            button4.setEnabled(false);

            // Provide feedback
            if (clickedButton.getText().toString().equals(correct_Answer))
            {
                mediaPlayer.start();
                correct_answers++;

                handler.postDelayed(new Runnable()
                {
                    @Override
                    public void run() {
                        clickedButton.setBackgroundColor(Color.GREEN);
                    }
                }, 100);

                handler.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        clickedButton.setBackgroundColor(Color.rgb(134, 146, 247));
                    }
                }, 1000);// Correct answer
            }
            else
            {
                clickedButton.setBackgroundColor(Color.RED);
                // Vibrate for 500 milliseconds
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                {
                    vibrator.vibrate(VibrationEffect.createOneShot(500, 200));
                } else
                {
                    //deprecated in API 26
                    vibrator.vibrate(500);
                }// Wrong answer

                handler.postDelayed(new Runnable()
                {
                    @Override
                    public void run() {
                        clickedButton.setBackgroundColor(Color.rgb(134, 146, 247));
                    }
                }, 1000);// Correct answer
            }//OnClickListener

            //ProgressBar progressBar = findViewById(R.id.progress_bar);
            //progressBar.incrementProgressBy(12);



            if (count < MAX_QUESTION)
            {
                handler.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        //if max questions is not reached yet, then a new question is generated using the updated hashmap
                        questions_and_answers(questionsMap);
                    }
                }, 1000);// Correct answer
            }//if
            else
            {
                handler.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        //once the game reaches the max questions it brings them to the game over page
                        Intent i = new Intent(Game.this, Game_Over_Page.class);
                        startActivity(i);
                        finish();

                    }
                }, 1000);//postDelayed
            }//else
        };//OnClickListener


        //buttons set to wait for click event to update ui/ux and loop the game to generated another random question
        button.setOnClickListener(listener);
        button2.setOnClickListener(listener);
        button3.setOnClickListener(listener);
        button4.setOnClickListener(listener);

        //makes sure all buttons are clickable again as the ui/ux set this to false so they are not clickable
        button.setEnabled(true);
        button2.setEnabled(true);
        button3.setEnabled(true);
        button4.setEnabled(true);

    }

}

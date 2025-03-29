package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class leaderboard extends AppCompatActivity
{
    private DatabaseReference mDatabase;
    TextView leaderboardTextView;
    ListView l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabase.orderByChild("numberOfAnswers").limitToLast(10).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                //HashMap<String,Integer> leaderboardList = new HashMap<>();
                List<String> leaderboardList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    User user = dataSnapshot.getValue(User.class);

                    String s ="\t\t\t\t\t" + user.getUsername() + ":\t\t" + user.getNumberOfAnswers();
                    leaderboardList.add(s);
                }

                displayLeaderBoard(leaderboardList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void displayLeaderBoard(List<String> leaderboardList )
    {
        Collections.reverse(leaderboardList);
        l = findViewById(R.id.list);
        ArrayAdapter<String> arr;

        arr = new ArrayAdapter<String>(this,R.layout.work,leaderboardList);
        l.setAdapter(arr);
    }
}



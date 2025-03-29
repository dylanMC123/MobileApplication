package com.example.project;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 */
public class LeaderboardFragment extends Fragment {
    private DatabaseReference mDatabase;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        ListView l = view.findViewById(R.id.list);
        ArrayList<String> array = new ArrayList<>();
        ArrayAdapter<String> arr = new ArrayAdapter<>(requireContext(), R.layout.work, array);
        l.setAdapter(arr);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabase.orderByChild("numberOfAnswers").limitToLast(10).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> leaderboardList = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    String s = "\t\t\t\t\t" + user.getUsername() + ":\t\t" + user.getNumberOfAnswers();
                    leaderboardList.add(s);
                }

                Collections.reverse(leaderboardList); // Ensure highest scores are at the top

                array.clear(); // Clear old data
                array.addAll(leaderboardList); // Update with new data
                arr.notifyDataSetChanged(); // Notify adapter of changes
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        return view;
    }
}
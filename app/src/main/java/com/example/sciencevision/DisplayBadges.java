package com.example.sciencevision;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.sciencevision.Models.Badge;
import com.example.sciencevision.Adapters.BadgeAdapter;
import com.parse.ParseUser;

import java.util.ArrayList;

public class DisplayBadges extends AppCompatActivity {
    private RecyclerView rvBadges;
    private BadgeAdapter adapter;
    private ArrayList<Badge> badges;
    private ArrayList<Integer> userParseBadges;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_badges);

        // Find RecyclerView and bind to adapter
        rvBadges = (RecyclerView) findViewById(R.id.rvBadges);

        // allows for optimizations
        rvBadges.setHasFixedSize(true);

        // Define 2 column grid layout
        final GridLayoutManager layout = new GridLayoutManager(DisplayBadges.this, 2);

        // Unlike ListView, you have to explicitly give a LayoutManager to the RecyclerView to position items on the screen.
        // There are three LayoutManager provided at the moment: GridLayoutManager, StaggeredGridLayoutManager and LinearLayoutManager.
        rvBadges.setLayoutManager(layout);

        // get data
        userParseBadges = (ArrayList<Integer>) ParseUser.getCurrentUser().get("Badges");
        badges = Badge.getBadge(userParseBadges);

        // Create an adapter
        adapter = new BadgeAdapter(DisplayBadges.this, badges);

        // Bind adapter to list
        rvBadges.setAdapter(adapter);
    }

    public void onBackPressed(){
        Intent intent = new Intent(DisplayBadges.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
package com.example.sciencevision;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.sciencevision.Models.Badge;


public class BadgeDetail extends AppCompatActivity {
    TextView name;
    TextView description;
    ImageView image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_badge);
        name = findViewById(R.id.tvBadgeName);
        description = findViewById(R.id.tvBadgeDescription);
        image = findViewById(R.id.ivBadge);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        Badge badge = intent.getParcelableExtra("Badge");
        name.setText(badge.getName());
        description.setText(badge.getDescription());
        Glide.with(this).load(badge.getThumbnail()).into(image);

    }
}
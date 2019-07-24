package com.example.sciencevision;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.EventLogTags;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sciencevision.Models.Findings;
import com.parse.ParseUser;

public class DetailActivity extends AppCompatActivity {
    TextView name;
    TextView description;
    TextView funFact;
    TextView experiment;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        name=findViewById(R.id.tvName);
        funFact=findViewById(R.id.tvFunFact);
        description=findViewById(R.id.tvDescription);
        experiment=findViewById(R.id.tvExperiment);
        image = findViewById(R.id.ivImage);
        Intent intent = getIntent();
        Findings newFinding = (intent.getParcelableExtra("User"));

        name.setText(newFinding.getName());
        description.setText(newFinding.getDescription());
        funFact.setText(newFinding.getFunFact());
        experiment.setText(newFinding.getExperiment());
        Glide.with(this).load(newFinding.getImage().getUrl()).into(image);

    }
}

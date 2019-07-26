package com.example.sciencevision;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.EventLogTags;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sciencevision.Models.Findings;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.List;

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
        name = findViewById(R.id.tvName);
        funFact = findViewById(R.id.tvFunFact);
        description = findViewById(R.id.tvDescription);
        experiment = findViewById(R.id.tvExperiment);
        image = findViewById(R.id.ivImage);
        Intent intent = getIntent();
        final Findings[] newFinding = new Findings[1];
        try {
            newFinding[0] = (Findings) (intent.getExtras().get("User"));
            name.setText(newFinding[0].getName());
            description.setText(newFinding[0].getDescription());
            funFact.setText(newFinding[0].getFunFact());
            experiment.setText(newFinding[0].getExperiment());
            Glide.with(this).load(newFinding[0].getImage().getUrl()).into(image);
        } catch (Exception e) {
            Findings.Query findingsQuery = new Findings.Query();
            findingsQuery = findingsQuery.getRecent().getUser(ParseUser.getCurrentUser());
            findingsQuery.findInBackground(new FindCallback<Findings>() {
                @Override
                public void done(List<Findings> objects, ParseException e) {
                    if (e == null) {
                        newFinding[0] = objects.get(objects.size() - 1);
                        name.setText(newFinding[0].getName());
                        description.setText(newFinding[0].getDescription());
                        funFact.setText(newFinding[0].getFunFact());
                        experiment.setText(newFinding[0].getExperiment());
                        Glide.with(getApplicationContext()).load(newFinding[0].getImage().getUrl()).into(image);
                    } else {
                        e.printStackTrace();
                    }
                }
            });
        }



    }
}

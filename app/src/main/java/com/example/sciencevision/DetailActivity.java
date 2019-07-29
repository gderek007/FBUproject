package com.example.sciencevision;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.EventLogTags;
import android.webkit.WebView;
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
    TextView tvFunFact;
    TextView tvExperiment;
    ImageView image;
    WebView wvFunFact;
    WebView wvExperiment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        name = findViewById(R.id.tvName);
        tvFunFact = findViewById(R.id.tvFunFact);
        wvFunFact = findViewById(R.id.wvFunFact);
        description = findViewById(R.id.tvDescription);
        tvExperiment = findViewById(R.id.tvExperiment);
        wvExperiment = findViewById(R.id.wvExperiment);
        image = findViewById(R.id.ivImage);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        final Findings newFinding[] = new Findings[1];
        try {
            newFinding[0] = (Findings) (intent.getExtras().get("User"));
            name.setText(newFinding[0].getName());
            description.setText(newFinding[0].getDescription());
            tvFunFact.setText(newFinding[0].getFunFact());
            tvExperiment.setText(newFinding[0].getExperiment());
            wvExperiment.loadUrl(newFinding[0].getExperiment());
            wvFunFact.loadUrl(newFinding[0].getFunFact());
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
                        tvFunFact.setText(newFinding[0].getFunFact());
                        tvExperiment.setText(newFinding[0].getExperiment());
                        wvExperiment.loadUrl(newFinding[0].getExperiment());
                        wvFunFact.loadUrl(newFinding[0].getFunFact());
                        Glide.with(getApplicationContext()).load(newFinding[0].getImage().getUrl()).into(image);
                    } else {
                        e.printStackTrace();
                    }
                }
            });
        }


    }
}

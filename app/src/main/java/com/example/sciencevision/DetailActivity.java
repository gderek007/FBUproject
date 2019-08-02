package com.example.sciencevision;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sciencevision.Models.Findings;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DetailActivity extends AppCompatActivity {
    TextView name;
    TextView description;
    TextView tvFunFact;
    TextView tvExperiment;
    ImageView image;
    WebView wvExperiment;
    FloatingActionButton fabShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        name = findViewById(R.id.tvName);
        tvFunFact = findViewById(R.id.tvFunFact);
        description = findViewById(R.id.tvDescription);
        tvExperiment = findViewById(R.id.tvExperiment);
        wvExperiment = findViewById(R.id.wvExperiment);
        image = findViewById(R.id.ivImage);
        fabShare = findViewById(R.id.fabShare);
        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
                //emailIntent.setDataAndType(Uri.parse("mailto:"), "text/plain");
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"starbri12@gmail.com"}); //TODO: replace with variable email
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your Child's New Finding!");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "filler");
                emailIntent.putExtra(Intent.EXTRA_HTML_TEXT, "<p style=\"background-image: linear-gradient(#33cc33, #008577); text-transform: uppercase;\"><b>Hello</b> World</p>");
                try {
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                    finish();
                    Log.i("Finished sending email.", "");

                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(DetailActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        final Findings newFinding[] = new Findings[1];
        if (!intent.getExtras().getBoolean("fromCamera")) { //
            newFinding[0] = (Findings) (intent.getExtras().get("User"));
            name.setText(newFinding[0].getName());
            description.setText(newFinding[0].getDescription());
            tvFunFact.setText(String.format("Fun Facts: %s", newFinding[0].getFunFact()));
            tvExperiment.setText(String.format("Fun %s Experiment:", newFinding[0].getName()));
            wvExperiment.loadUrl(newFinding[0].getExperiment());
            Glide.with(this).load(newFinding[0].getImage().getUrl()).into(image);
        } else {
            name.setText((String) intent.getExtras().get("Name"));
            description.setText((String) intent.getExtras().get("Description"));
            tvFunFact.setText(String.format("Fun Facts: %s", (String) intent.getExtras().get("FunFact")));
            tvExperiment.setText(String.format("Fun %s Experiment:", (String) intent.getExtras().get("Name")));
            wvExperiment.loadUrl((String) intent.getExtras().get("Experiment"));
            Glide.with(this).load((String) intent.getExtras().get("ImageUrl")).into(image);
        }


    }
}

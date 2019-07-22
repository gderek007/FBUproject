package com.example.sciencevision;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sciencevision.Models.Findings;
import com.parse.Parse;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.w3c.dom.Text;
import org.xml.sax.helpers.ParserFactory;

import com.example.sciencevision.Models.Findings;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {
    TextView tvName;
    TextView tvDescription;
    TextView tvExperiment;
    Findings finding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView username;
        TextView name;
        TextView FunFact;
        TextView Description;
        TextView Experiment;
        ImageView Image;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.individual_finding);
        username=findViewById(R.id.User);
        name=findViewById(R.id.Name);
        FunFact=findViewById(R.id.FunFact);
        Description=findViewById(R.id.Description);
        Experiment=findViewById(R.id.Experiment);
        Image = findViewById(R.id.Image);
        ParseUser User = ParseUser.getCurrentUser();
        Findings.Query recentPost= new Findings.Query();
        try {
            Findings newFinding = recentPost.getTop().getFirst();
            username.setText(newFinding.getUser().getUsername());
            name.setText(newFinding.getName());
            FunFact.setText(newFinding.getFunFact());
            Description.setText(newFinding.getDescription());
            Experiment.setText(newFinding.getExperiment());
            Glide.with(this).load(newFinding.getImage().getUrl()).into(Image);

        }
        catch(Exception e)
        {
            e.printStackTrace();

        }
        setContentView(R.layout.activity_detail);

        tvName = findViewById(R.id.tvFoundName);
        tvDescription = findViewById(R.id.tvDescription);
        tvExperiment = findViewById(R.id.tvFoundExperiment);
        finding = (Findings) getIntent().getExtras().get(Findings.class.getSimpleName());
        tvName.setText(finding.getName());
        tvDescription.setText(finding.getDescription());
        tvExperiment.setText(finding.getExperiment());

    }
}

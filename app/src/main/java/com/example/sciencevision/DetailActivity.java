package com.example.sciencevision;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

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
        super.onCreate(savedInstanceState);
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

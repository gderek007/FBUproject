package com.example.sciencevision;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.sciencevision.Models.Findings;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {
/*@BindView(R.id.tvFoundName) TextView tvName;
    @BindView(R.id.tvFoundDescription) TextView tvDescription;
    @BindView(R.id.tvFoundExperiment) TextView tvExperiment;
    Findings finding;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        /*ButterKnife.bind(this);
        finding = (Findings) getIntent().getExtras().get(Findings.class.getSimpleName());
        tvName.setText(finding.getItemName());
        tvDescription.setText(finding.getDescription());
        tvExperiment.setText(finding.getKeyExperiment());*/

    }
}

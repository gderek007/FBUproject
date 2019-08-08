package com.example.sciencevision.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sciencevision.Models.Findings;
import com.example.sciencevision.R;
import com.parse.ParseUser;
import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;
import java.util.List;

public class FindingsAdapter extends RecyclerView.Adapter<FindingsAdapter.ViewHolder> {
    private List<Findings> mFindings;
    private List<Findings> unfilteredFindings;
    private ParseUser user = ParseUser.getCurrentUser();
    private Context context;

    // pass in the tweets array in the constructor
    public FindingsAdapter(List<Findings> findings) {
        mFindings = findings;
    }

    // for each row, inflate the layout and cache references into View
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View findingView = inflater.inflate(R.layout.individual_finding, parent, false);
        return new ViewHolder(findingView);
    }

    // bind the values based on the position of the element
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Findings finding = mFindings.get(position);
        //populate the views according to this data


        try {
            holder.tvName.setText(finding.getUser().getUsername() + " explored " + finding.getName());
            holder.tvNameExpand.setText(finding.getName()); //EXPAND
            holder.ivProPic.setVisibility(View.VISIBLE);
            Glide.with(context).load(finding.getProPic().getUrl())
                    .circleCrop()
                    .into(holder.ivProPic);
        } catch (java.lang.Exception e) {
            holder.tvName.setText(finding.getName());
            holder.tvNameExpand.setText(finding.getName()); //EXPAND
            holder.ivProPic.setVisibility(View.GONE);
            //TODO: Expand profile pic
        }


        holder.tvCreatedAt.setText(finding.getNiceTime());
        holder.tvCreatedAtExpand.setText(finding.getNiceTime()); //EXPAND
        int radius = 30;
        int margin = 10;
        Glide.with(context).load(finding.getImage().getUrl())
                .circleCrop()
                .override(600, 600)
                .into(holder.ivImage);


        // EXPAND
        Glide.with(context).load(finding.getImage().getUrl())
                .circleCrop()
                .override(800, 800)
                .into(holder.ivImageExpand);

        // EXPAND
        holder.tvDescription.setText(finding.getDescription());
        holder.tvFunFact.setText(finding.getFunFact());
        holder.tvExperiment.setText("Click to see experiment!");

        holder.tvExperiment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Experiment");

                WebView wv = new WebView(context);
                wv.setInitialScale(1);
                wv.getSettings().setLoadWithOverviewMode(true);
                wv.getSettings().setUseWideViewPort(true);
                wv.getSettings().setJavaScriptEnabled(true);
                wv.loadUrl(finding.getExperiment());
                wv.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);

                        return true;
                    }
                });

                alert.setView(wv);
                alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                alert.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mFindings.size();
    }

    // create ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvUser;
        TextView tvName;
        TextView tvCreatedAt;
        ImageView ivImage;
        ImageView ivProPic;

        FoldingCell fcFinding;

        TextView tvNameExpand;
        TextView tvCreatedAtExpand;
        ImageView ivImageExpand;
        TextView tvDescription;
        TextView tvFunFact;
        TextView tvExperiment;

        public ViewHolder(View itemView) {
            super(itemView);
            // perform findViewById lookups
            tvUser = itemView.findViewById(R.id.tvUser);
            tvName = itemView.findViewById(R.id.tvName);
            ivImage = itemView.findViewById(R.id.ivImage);
            ivProPic = itemView.findViewById(R.id.ivProPic);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
            fcFinding = itemView.findViewById(R.id.fcFinding);

            tvNameExpand = itemView.findViewById(R.id.tvNameExpand);
            tvCreatedAtExpand = itemView.findViewById(R.id.tvCreatedAtExpand);
            ivImageExpand = itemView.findViewById(R.id.ivImageExpand);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvFunFact = itemView.findViewById(R.id.tvFunFact);
            tvExperiment = itemView.findViewById(R.id.tvExperiment);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            fcFinding.toggle(false);

        }
    }

    public void clear() {
        mFindings.clear();
        notifyDataSetChanged();
    }

    public void filterList(ArrayList<Findings> filteredList) {
        if (filteredList.size() != 0) {
            notifyDataSetChanged();
            mFindings = filteredList;
        }

    }

    public void add() {

    }
}
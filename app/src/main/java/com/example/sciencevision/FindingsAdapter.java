package com.example.sciencevision;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sciencevision.Models.Findings;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.List;
import java.util.jar.JarException;

public class FindingsAdapter extends RecyclerView.Adapter<FindingsAdapter.ViewHolder> {
    private List<Findings> mFindings;
    private ParseUser user = ParseUser.getCurrentUser();
    Context context;

    // pass in the tweets array in the constructor
    public FindingsAdapter(List<Findings> findings) {
        mFindings = findings;
    }

    // for each row, inflate the layout and cache references into View
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View findingView = inflater.inflate(R.layout.individual_finding, parent, false);
        ViewHolder viewHolder = new ViewHolder(findingView);
        return viewHolder;
    }

    // bind the values based on the position of the element
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Findings finding = mFindings.get(position);
        //populate the views according to this data
        //try catch needed because of weird bug that returns no User for finding.getUser() when the query has only the User's findings
        try {
            holder.tvUser.setText(finding.getUser().getUsername());
        }
        catch(java.lang.Exception e){
            holder.tvUser.setText(user.getUsername());
        }
        holder.tvName.setText(finding.getName());
        holder.tvDescription.setText(finding.getDescription());
        holder.tvFunFact.setText(finding.getFunFact());
        holder.tvExperiment.setText(finding.getExperiment());
        Glide.with(context).load(finding.getImage().getUrl()).into(holder.ivImage);
    }

    @Override
    public int getItemCount() {
        return mFindings.size();
    }

    // create ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvUser;
        public TextView tvName;
        public TextView tvDescription;
        public TextView tvFunFact;
        public TextView tvExperiment;
        public ImageView ivImage;

        public ViewHolder(View itemView) {
            super(itemView);
            // perform findViewById lookups
            tvUser = (TextView) itemView.findViewById(R.id.tvUser);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            tvFunFact = (TextView) itemView.findViewById(R.id.tvFunFact);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvExperiment = (TextView) itemView.findViewById(R.id.tvExperiment);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Findings finding = mFindings.get(position);
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("User", finding);
            context.startActivity(intent);
        }
    }

    public void clear() {
        mFindings.clear();
        notifyDataSetChanged();
    }
}
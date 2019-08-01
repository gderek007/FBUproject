package com.example.sciencevision;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sciencevision.Models.Findings;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

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
            holder.tvUser.setText(finding.getUser().getUsername());
        } catch (java.lang.Exception e) {
            holder.tvUser.setText(user.getUsername());
        }
        holder.tvName.setText(finding.getName());
        holder.tvDescription.setText(finding.getDescription());
        holder.tvFunFact.setText(String.format("Fun Facts: %s", finding.getFunFact()));
        holder.tvExperiment.setText(String.format("Fun %s Experiment: %s", finding.getName(), finding.getExperiment()));
        int radius = 30;
        int margin = 10;
        Glide.with(context).load(finding.getImage().getUrl())
                .fitCenter()
                .transform(new RoundedCornersTransformation(radius, margin))
                .into(holder.ivImage);
    }

    @Override
    public int getItemCount() {
        return mFindings.size();
    }

    // create ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvUser;
        TextView tvName;
        TextView tvDescription;
        TextView tvFunFact;
        TextView tvExperiment;
        ImageView ivImage;

        public ViewHolder(View itemView) {
            super(itemView);
            // perform findViewById lookups
            tvUser = itemView.findViewById(R.id.tvUser);
            tvName = itemView.findViewById(R.id.tvName);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvFunFact = itemView.findViewById(R.id.tvFunFact);
            tvName = itemView.findViewById(R.id.tvName);
            tvExperiment = itemView.findViewById(R.id.tvExperiment);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Findings finding = mFindings.get(position);
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("User", finding);
            intent.putExtra("fromCamera", false);
            context.startActivity(intent);
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
package com.example.sciencevision;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.sciencevision.Models.Findings;
import com.parse.ParseUser;
import java.util.List;

public class FindingsAdapter extends RecyclerView.Adapter<FindingsAdapter.ViewHolder>{
    private List<Findings> mFindings;
    Context context;
    // pass in the tweets array in the constructor
    public FindingsAdapter(List<Findings> findings){
        mFindings = findings;
    }
    // for each row, inflate the layout and cache references into View
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View findingView = inflater.inflate(R.layout.individual_finding,parent,false);
        ViewHolder viewHolder = new ViewHolder(findingView);
        return viewHolder;
    }

    // bind the values based on the position of the element
    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {
        // get the data according to position
        Findings findings = mFindings.get(position);
        //populate the views according to this data
        final ParseUser user=findings.getUser();
        holder.User.setText(user.getUsername());
        holder.Name .setText(findings.getName());
        holder.Description.setText(findings.getDescription());
        holder.FunFact.setText(findings.getFunFact());
        holder.Experiment.setText(findings.getExperiment());
        Glide.with(context).load(findings.getImage().getUrl()).into(holder.Image);

    }
    @Override
    public int getItemCount() {
        return mFindings.size();
    }

    // create ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView User;
        public TextView Name;
        public ImageView Image;
        public TextView Description;
        public TextView FunFact;
        public TextView Experiment;

        public ViewHolder(View itemView) {
            super(itemView);
            // perform findViewById lookups
            User = (TextView) itemView.findViewById(R.id.User);
            Name = (TextView) itemView.findViewById(R.id.tvName);
            Image = (ImageView) itemView.findViewById(R.id.ivImage);
            Description = (TextView) itemView.findViewById(R.id.tvDescription);
            FunFact = (TextView) itemView.findViewById(R.id.tvFunFact);
            Name = (TextView) itemView.findViewById(R.id.tvName);
            Experiment = (TextView) itemView.findViewById(R.id.tvExperiment);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Findings finding = mFindings.get(position);
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("User",finding);
            context.startActivity(intent);
        }
    }
    public void clear() {
        mFindings.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Findings> list) {
        mFindings.addAll(list);
        notifyDataSetChanged();
    }
}
package com.example.sciencevision.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.sciencevision.BadgeDetail;
import com.example.sciencevision.Models.Badge;
import com.example.sciencevision.R;

import java.util.ArrayList;

public class BadgeAdapter extends RecyclerView.Adapter<BadgeAdapter.ViewHolder> {
    private Activity mContext;
    private ArrayList<Badge> mBadges;

    public BadgeAdapter(Activity context, ArrayList<Badge> badges) {
        mContext = context;
        mBadges = badges;
    }

    // Inflate the view based on the viewType provided.
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.individual_badge, parent, false);
        return new ViewHolder(itemView, mContext);
    }

    // Display data at the specified position
    public void onBindViewHolder(ViewHolder holder, int position) {
        Badge badge = mBadges.get(position);
        holder.rootView.setTag(badge);
        holder.tvName.setText(badge.getName());
        holder.ivBadge.setImageResource(R.drawable.badge);
//        Glide.with(mContext).load(badge.getThumbnail()).into(holder.ivProfile);
    }

    @Override
    public int getItemCount() {
        return mBadges.size();
    }

    // Provide a reference to the views for each contact item
    public class ViewHolder extends RecyclerView.ViewHolder {
        final View rootView;
        final ImageView ivBadge;
        final TextView tvName;
        final View vPalette;

        public ViewHolder(View itemView, final Context context) {
            super(itemView);
            rootView = itemView;
            ivBadge = (ImageView) itemView.findViewById(R.id.ivBadge);
            tvName = (TextView) itemView.findViewById(R.id.tvBadgeName);
            vPalette = itemView.findViewById(R.id.vPalette);

            // Navigate to contact details activity on click of card view.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Badge badge = (Badge) v.getTag();
                    if (badge != null) {
                        Intent intent = new Intent(mContext, BadgeDetail.class);
                        intent.putExtra("Badge", badge);
                        mContext.startActivity(intent);

                    }
                }
            });
        }
    }
}

package com.example.sciencevision;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sciencevision.Models.ProfilePictures;
import com.parse.ParseObject;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {
    public static String result;
    private List<ProfilePictures> mProfilePictures;
    private ItemClickListener mItemClickListener;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    Context context;


    // data is passed into the constructor
    ProfileAdapter(List<ProfilePictures> profilePictures, ItemClickListener itemClickListener) {
        this.mProfilePictures = profilePictures;
        this.mItemClickListener = itemClickListener;
    }

    // inflates the row layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View findingView = inflater.inflate(R.layout.individual_profile, parent, false);
        ViewHolder viewHolder = new ViewHolder(findingView, mItemClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ParseObject picture = mProfilePictures.get(position);
        Glide.with(context).load(picture.getParseFile("Avatar").getUrl()).into(holder.ivAvatar);
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivAvatar;
        ItemClickListener itemClickListener;

        public ViewHolder(View itemView, ItemClickListener itemClickListener) {
            super(itemView);
            ivAvatar = (ImageView) itemView.findViewById(R.id.ivAvatar);
            ivAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(getAdapterPosition());
                }
            });
            this.itemClickListener = itemClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onItemClick(getAdapterPosition());
        }

    }

    public void clear() {
        mProfilePictures.clear();
        notifyDataSetChanged();
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mProfilePictures.size();
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(int position);
    }
}
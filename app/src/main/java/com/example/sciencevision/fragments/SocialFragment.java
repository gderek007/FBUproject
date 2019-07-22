package com.example.sciencevision.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sciencevision.FindingsAdapter;
import com.example.sciencevision.MainActivity;
import com.example.sciencevision.Models.Findings;
import com.example.sciencevision.R;
import com.example.sciencevision.SearchClient;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class SocialFragment extends Fragment {
    private ParseUser User;
    private String description;
    private ParseFile image;
    private RecyclerView rvFindings;
    FindingsAdapter adapter;
    ArrayList<Findings> findings;
    private SwipeRefreshLayout swipeContainer;

    public SocialFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_social, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        User=ParseUser.getCurrentUser();
        findings = new ArrayList<>();
        swipeContainer = view.findViewById(R.id.swipeContainer);
        adapter = new FindingsAdapter(findings);
        rvFindings= view.findViewById(R.id.rvFindings);
        rvFindings.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFindings.setAdapter(adapter);

        ParseQuery<Findings> query = ParseQuery.getQuery(Findings.class);
        // Specify the object id
        query.getInBackground(User.getObjectId(), new GetCallback<Findings>() {
            public void done(Findings item, ParseException e) {
                if (e == null) {
                    Log.d("Query","Query has been loaded!");
                } else {
                    // something went wrong
                    e.printStackTrace();
                }
            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadTopPosts();
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_red_dark);

        loadTopPosts();
    }

    private void loadTopPosts() {
        final Findings.Query postsQuery = new Findings.Query();
        postsQuery.getRecent().withUser();
        postsQuery.findInBackground(new FindCallback<Findings>() {
            @Override
            public void done(List<Findings> objects, ParseException e) {
                adapter.clear();
                if (e == null) {
                    //brute force method to get top 20 posts
                    if(objects.size()>20)
                    {
                        for (int i = objects.size()-20; i < objects.size(); i++) {
                            findings.add(0,objects.get(i));
                            rvFindings.scrollToPosition(0);
                        }
                    }
                    else
                    {
                        for (int i = 0; i < objects.size(); i++) {
                            findings.add(0,objects.get(i));
                            rvFindings.scrollToPosition(0);
                        }
                    }
                    swipeContainer.setRefreshing(false);
                } else {
                    e.printStackTrace();
                }
            }
        });
    }



}

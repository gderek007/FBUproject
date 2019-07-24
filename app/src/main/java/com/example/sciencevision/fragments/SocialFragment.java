package com.example.sciencevision.fragments;


import android.os.Bundle;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sciencevision.FindingsAdapter;
import com.example.sciencevision.Models.Findings;
import com.example.sciencevision.R;
import com.parse.FindCallback;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class SocialFragment extends Fragment {
    private ParseUser User;
    private String tvDescription;
    private ParseFile ivImage;
    private RecyclerView rvFindings;
    private FindingsAdapter adapter;
    private ArrayList<Findings> arrayFinding;
    private SwipeRefreshLayout swipeContainer;

    public SocialFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_social, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        User = ParseUser.getCurrentUser();
        arrayFinding = new ArrayList<>();

        swipeContainer = view.findViewById(R.id.swipeContainer);
        rvFindings = view.findViewById(R.id.rvFindings);

        adapter = new FindingsAdapter(arrayFinding);
        rvFindings.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFindings.setAdapter(adapter);

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
        Findings.Query findingsQuery = new Findings.Query();

        findingsQuery.getRecent().withUser();

        findingsQuery.findInBackground(new FindCallback<Findings>() {
            @Override
            public void done(List<Findings> objects, ParseException e) {
                adapter.clear();
                if (e == null) {
                    //brute force method to get top 20 posts
                    if (objects.size() > 20) {
                        for (int i = objects.size() - 20; i < objects.size(); i++) {
                            arrayFinding.add(0, objects.get(i));
                            rvFindings.scrollToPosition(0);
                        }
                    } else {
                        for (int i = 0; i < objects.size(); i++) {
                            arrayFinding.add(0, objects.get(i));
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

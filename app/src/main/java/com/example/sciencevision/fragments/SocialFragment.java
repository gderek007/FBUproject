package com.example.sciencevision.fragments;


import android.os.Bundle;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.sciencevision.EndlessRecyclerViewScrollListener;
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
    private EditText etSearch;
    private ParseUser User;
    private String tvDescription;
    private ParseFile ivImage;
    private RecyclerView rvFindings;
    private FindingsAdapter adapter;
    private ArrayList<Findings> findings;
    private SwipeRefreshLayout swipeContainer;
    private EndlessRecyclerViewScrollListener scrollListener;

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
        findings = new ArrayList<>();

        etSearch = view.findViewById(R.id.etSearch);
        swipeContainer = view.findViewById(R.id.swipeContainer);
        rvFindings = view.findViewById(R.id.rvFindings);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        adapter = new FindingsAdapter(findings);
        rvFindings.setLayoutManager(linearLayoutManager);
        rvFindings.setAdapter(adapter);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadTopPosts();
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_red_dark);
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadMore();
            }
        };
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString() == "") {
                    filter(s.toString());
                }
            }
        });
        rvFindings.addOnScrollListener(scrollListener);
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
                            findings.add(0, objects.get(i));
                            adapter.notifyItemInserted(0);
                            rvFindings.scrollToPosition(0);
                        }
                    } else {
                        for (int i = 0; i < objects.size(); i++) {
                            findings.add(0, objects.get(i));
                            adapter.notifyItemInserted(0);
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

    private void loadMore() {
        Findings.Query findingsQuery = new Findings.Query();
        findingsQuery.getRecent();
        findingsQuery.findInBackground(new FindCallback<Findings>() {
            @Override
            public void done(List<Findings> objects, ParseException e) {
                //adapter.clear();
                if (e == null) {
                    //brute force method to get top 20 posts
                    if (objects.size() > 20 + findings.size()) {
                        for (int i = objects.size() - 20 - findings.size(); i < objects.size() - 20; i++) {
                            findings.add(findings.size() - 1, objects.get(i));
                            adapter.notifyItemInserted(findings.size() - 1);
                        }
                    } else {
                        for (int i = 0; i < objects.size(); i++) {
                            findings.add(findings.size() - 1, objects.get(i));
                            adapter.notifyItemInserted(findings.size() - 1);
                        }
                    }
                    swipeContainer.setRefreshing(false);
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
    //filter that performs basic search call
    private void filter(String text) {
        ArrayList<Findings> filteredList = new ArrayList<>();
        for (Findings item : findings) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter.filterList(filteredList);
    }


}

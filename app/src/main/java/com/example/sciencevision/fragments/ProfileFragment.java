package com.example.sciencevision.fragments;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sciencevision.EndlessRecyclerViewScrollListener;
import com.example.sciencevision.FindingsAdapter;
import com.example.sciencevision.Models.Findings;
import com.example.sciencevision.R;
import com.example.sciencevision.SearchClient;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private EditText etSearch;
    private ParseUser User;
    private TextView tvUser;
    private TextView tvNumberOfFindings;
    private ImageButton btnLogout;
    private ImageView ivProfile;
    private Button btnBadge;
    private RecyclerView rvUserFindings;
    private FindingsAdapter adapter;
    private ArrayList<Findings> findings;
    private ArrayList<Integer> badges;
    private EndlessRecyclerViewScrollListener scrollListener;
    private SearchClient searchClient;
    private int numberOfFindings = 0;
    private boolean smartNetwork = true;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        User = ParseUser.getCurrentUser();

        tvUser = view.findViewById(R.id.tvUser);
        tvNumberOfFindings = view.findViewById(R.id.tvNumberOfFindings);
        etSearch = view.findViewById(R.id.etSearch);
        ivProfile = view.findViewById(R.id.ivBadge);
        rvUserFindings = view.findViewById(R.id.rvUserFindings);

        searchClient = new SearchClient();

        tvUser.setText(User.getUsername() + "'s Profile");
        tvNumberOfFindings.setText("You have " + User.get("NumberOfFindings") + " Findings!");
        Glide.with(this).load(User.getParseFile("ProfilePicture").getUrl()).into(ivProfile);
        findings = new ArrayList<>();
        adapter = new FindingsAdapter(findings);
        rvUserFindings = view.findViewById(R.id.rvUserFindings);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvUserFindings.setLayoutManager(linearLayoutManager);
        rvUserFindings.setAdapter(adapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("")) {
                    filter(s.toString());
                } else {
                    filter("");
                }
            }
        });

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadMore();
            }
        };
        rvUserFindings.addOnScrollListener(scrollListener);
        loadTopPosts();

    }

    private void loadTopPosts() {
        Findings.Query findingsQuery = new Findings.Query();

        findingsQuery.getUser(User).orderByAscending("createdAt");

        findingsQuery.findInBackground(new FindCallback<Findings>() {
            @Override
            public void done(List<Findings> objects, ParseException e) {
                Log.d("Amount", Integer.toString(adapter.getItemCount()));

//                adapter.clear();
//                findings.clear();
                if (e == null) {
                    //brute force method to get top 20 posts
                    if (objects.size() > 20) {
                        for (int i = objects.size() - 20; i < objects.size(); i++) {
                            findings.add(0, objects.get(i));
                            adapter.notifyItemInserted(0);
                            rvUserFindings.scrollToPosition(0);
                        }
                    } else {
                        for (int i = 0; i < objects.size(); i++) {
                            findings.add(0, objects.get(i));
                            adapter.notifyItemInserted(0);
                            rvUserFindings.scrollToPosition(0);
                        }
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadMore() {
        Findings.Query findingsQuery = new Findings.Query();
        findingsQuery.getUser(User).orderByDescending("createdAt").setSkip(findings.size());
        findingsQuery.findInBackground(new FindCallback<Findings>() {
            @Override
            public void done(List<Findings> objects, ParseException e) {
                if (e == null) {
                    //brute force method to get top 20 posts
                    if (objects.size() > 20) {
                        for (int i = objects.size() - 20; i < objects.size(); i++) {
                            findings.add(findings.size(), objects.get(i));
                            adapter.notifyItemInserted(findings.size() - 1);
                        }
                    } else {
                        for (int i = 0; i < objects.size(); i++) {
                            findings.add(findings.size(), objects.get(i));
                            adapter.notifyItemInserted(findings.size() - 1);
                        }
                    }
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

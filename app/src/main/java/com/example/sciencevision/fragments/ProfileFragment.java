package com.example.sciencevision.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sciencevision.FindingsAdapter;
import com.example.sciencevision.Models.Findings;
import com.example.sciencevision.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private ParseUser User;
    private TextView tvUser;
    private ImageView ivProfile;
    private RecyclerView rvUserFindings;
    private FindingsAdapter adapter;
    private ArrayList<Findings> findings;

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
        ivProfile = view.findViewById(R.id.ivProfile);
        rvUserFindings = view.findViewById(R.id.rvUserFindings);

        tvUser.setText(User.getUsername());
        findings = new ArrayList<>();
        adapter = new FindingsAdapter(findings);
        rvUserFindings = view.findViewById(R.id.rvUserFindings);
        rvUserFindings.setLayoutManager(new LinearLayoutManager(getContext()));
        rvUserFindings.setAdapter(adapter);

        loadTopPosts();
    }

    private void loadTopPosts() {
        Findings.Query findingsQuery = new Findings.Query();
        findingsQuery = findingsQuery.getRecent().getUser(ParseUser.getCurrentUser());
        findingsQuery.findInBackground(new FindCallback<Findings>() {
            @Override
            public void done(List<Findings> objects, ParseException e) {
                adapter.clear();
                if (e == null) {
                    //brute force method to get top 20 posts
                    if (objects.size() > 20) {
                        for (int i = objects.size() - 20; i < objects.size(); i++) {
                            findings.add(0, objects.get(i));
                            rvUserFindings.scrollToPosition(0);
                        }
                    } else {
                        for (int i = 0; i < objects.size(); i++) {
                            findings.add(0, objects.get(i));
                            rvUserFindings.scrollToPosition(0);
                        }
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }


}

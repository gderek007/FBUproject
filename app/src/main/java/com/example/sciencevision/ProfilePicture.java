package com.example.sciencevision;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sciencevision.Models.Badge;
import com.example.sciencevision.Models.ProfilePictures;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;


public class ProfilePicture extends AppCompatActivity implements ProfileAdapter.ItemClickListener {

    private ProfileAdapter adapter;
    private ArrayList<ProfilePictures> mProfilePictures;
    private RecyclerView rvProfile;
    private ImageView ivPreview;
    private Button btnProfile;
    private ParseObject picture;
    private ArrayList<Integer> badges;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_picture);

        mProfilePictures = new ArrayList<>();
        badges = new ArrayList<>();


        // set up the RecyclerView
        rvProfile = findViewById(R.id.rvProfile);
        ivPreview = findViewById(R.id.ivPreview);
        btnProfile = findViewById(R.id.btnProfile);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        adapter = new ProfileAdapter(mProfilePictures, this);
        rvProfile.setLayoutManager(horizontalLayoutManager);
        rvProfile.setAdapter(adapter);

        loadTopPosts();

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(picture!=null) {
                    ParseUser.getCurrentUser().put("ProfilePicture", picture.getParseFile("Avatar"));
                    ParseUser.getCurrentUser().put("Badges", badges);
                    ParseUser.getCurrentUser().put("NumberOfFindings", 0);
                    ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Log.d("ProfilePicture", "Uploaded Profile Picture");
                            } else {
                                e.printStackTrace();
                            }
                        }
                    });
                    Intent intent = new Intent(ProfilePicture.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Choose a Profile Picture!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private void loadTopPosts() {
        ProfilePictures.Query avatarsQuery = new ProfilePictures.Query();
        avatarsQuery.getTop();
        avatarsQuery.findInBackground(new FindCallback<ProfilePictures>() {
            @Override
            public void done(List<ProfilePictures> objects, ParseException e) {
                Log.d("Avatar", objects.toString());
                adapter.clear();
                if (e == null) {
                    //brute force method to get top 20 posts
                    if (objects.size() > 20) {
                        for (int i = objects.size() - 20; i < objects.size(); i++) {
                            mProfilePictures.add(0, objects.get(i));
                            adapter.notifyItemInserted(0);
                            rvProfile.scrollToPosition(0);
                        }
                    } else {
                        for (int i = 0; i < objects.size(); i++) {
                            mProfilePictures.add(0, objects.get(i));
                            adapter.notifyItemInserted(0);
                            rvProfile.scrollToPosition(0);
                        }
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Log.d("Click", "onItemClick: " + Integer.toString(position));
        picture = mProfilePictures.get(position);
        Glide.with(this).load(picture.getParseFile("Avatar").getUrl()).into(ivPreview);
    }
}
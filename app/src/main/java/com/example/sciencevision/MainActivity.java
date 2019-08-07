package com.example.sciencevision;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.sciencevision.fragments.FindingFragment;
import com.example.sciencevision.fragments.ProfileFragment;
import com.example.sciencevision.fragments.SocialFragment;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Fabs
        FloatingActionButton fabLogout = findViewById(R.id.logout);
        FloatingActionButton fabProfilePicture = findViewById(R.id.changeProfile);
        FloatingActionButton fabBadges = findViewById(R.id.fabBadges);
        fabLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseUser.logOut();
                Intent intent = new Intent(MainActivity.this, LaunchActivity.class);
                startActivity(intent);
                finish();
            }
        });
        fabProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChangeProfilePicture.class);
                startActivity(intent);
                finish();

            }
        });
        fabBadges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DisplayBadges.class);
                startActivity(intent);


            }
        });


        final FragmentManager fragmentManager = getSupportFragmentManager();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        // define your fragments here
        final Fragment fragmentFinding = new FindingFragment();
        final Fragment fragmentProfile = new ProfileFragment();
        final Fragment fragmentSocial = new SocialFragment();

        // handle navigation selection
        bottomNavigationView.setItemBackgroundResource(R.color.colorPrimaryDark);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_finding:
                                fragment = fragmentFinding;
                                break;
                            case R.id.action_profile:
                                fragment = fragmentProfile;
                                break;
                            case R.id.action_social:
                                fragment = fragmentSocial;
                                break;
                            default:
                                fragment = fragmentProfile;
                                break;
                        }
                        fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                        return true;
                    }
                });
        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_profile);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }


}

package com.example.sciencevision;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.sciencevision.fragments.FindingFragment;
import com.example.sciencevision.fragments.ProfileFragment;
import com.example.sciencevision.fragments.SocialFragment;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Fragment fragment;
    Fragment fragmentFinding;
    Fragment fragmentSocial;
    Fragment fragmentProfile;
    MenuItem prevMenuItem;

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
        ViewPager viewPager = findViewById(R.id.pager);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: " + position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);

            }


            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setupViewPager(viewPager);



        // handle navigation selection
        bottomNavigationView.setItemBackgroundResource(R.color.colorPrimaryDark);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_finding:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.action_profile:
                                viewPager.setCurrentItem(1);
                                break;
                            case R.id.action_social:
                                viewPager.setCurrentItem(2);
                                break;
                            default:
                                viewPager.setCurrentItem(1);
                                break;
                        }

                        return false;
                    }
                });
        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_profile);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            fragmentFinding.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        // define your fragments here
        fragmentFinding = new FindingFragment();
        fragmentProfile = new ProfileFragment();
        fragmentSocial = new SocialFragment();
        viewPagerAdapter.addFragment(fragmentFinding);
        viewPagerAdapter.addFragment(fragmentProfile);
        viewPagerAdapter.addFragment(fragmentSocial);
        viewPager.setAdapter(viewPagerAdapter);
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }
    }

    @Override
    public void onBackPressed() {

    }

}

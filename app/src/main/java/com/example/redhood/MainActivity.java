package com.example.redhood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.redhood.fragments.HomeFragment;
import com.example.redhood.fragments.SetsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment(), "home").commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;
                String tag = "";
                switch(item.getItemId()) {
                    case R.id.nav_home:
                        selectedFragment = new HomeFragment();
                        tag = "home";
                        break;
                    case R.id.nav_sets:
                        selectedFragment = new SetsFragment();
                        tag = "sets";
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        selectedFragment, tag).addToBackStack(null).commit();
                return true;
            };
}
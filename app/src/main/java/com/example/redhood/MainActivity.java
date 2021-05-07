package com.example.redhood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.redhood.fragments.HomeFragment;
import com.example.redhood.fragments.RecognitionFragment;
import com.example.redhood.fragments.SetsFragment;
import com.example.redhood.fragments.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment(), "home").commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            RecognitionFragment myFragment = (RecognitionFragment)getSupportFragmentManager().findFragmentByTag("recognition");
            if (myFragment != null && myFragment.isVisible()) {
                myFragment.setPic();
            }
        }
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
                    case R.id.nav_settings:
                        selectedFragment = new SettingsFragment();
                        tag = "settings";
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        selectedFragment, tag).addToBackStack(null).commit();
                return true;
            };
}
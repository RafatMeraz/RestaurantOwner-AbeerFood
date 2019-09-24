package com.example.restaurant_abeerfood.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.restaurant_abeerfood.R;
import com.example.restaurant_abeerfood.database.SharedPrefManager;
import com.example.restaurant_abeerfood.fragments.HomeFragment;
import com.example.restaurant_abeerfood.fragments.OrderFragment;
import com.example.restaurant_abeerfood.fragments.ProfileFragment;
import com.example.restaurant_abeerfood.model.User;
import com.example.restaurant_abeerfood.utils.Constraints;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    public static BottomNavigationView navView;
    private boolean doubleClickedExit = false;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    FragmentTransaction t = getSupportFragmentManager().beginTransaction();
                    Fragment mFrag = new HomeFragment();
                    t.replace(R.id.homeFrameLayout, mFrag);
                    t.commit();
                    return true;
                case R.id.navigation_orders:
                    FragmentTransaction t1 = getSupportFragmentManager().beginTransaction();
                    Fragment mFrag1 = new OrderFragment();
                    t1.replace(R.id.homeFrameLayout, mFrag1);
                    t1.commit();
                    return true;
                case R.id.navigation_profile:
                    FragmentTransaction t2 = getSupportFragmentManager().beginTransaction();
                    Fragment mFrag2 = new ProfileFragment();
                    t2.replace(R.id.homeFrameLayout, mFrag2);
                    t2.commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        if (!SharedPrefManager.getInstance(this).isLoggedIn()){
            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
            finish();
        } else {
            Constraints.currentUser = new User(
                    SharedPrefManager.getInstance(this).getUserID(),
                    SharedPrefManager.getInstance(this).getUserEmail(),
                    SharedPrefManager.getInstance(this).getUserName()
            );
        }

        navView = findViewById(R.id.bottomNavigationView);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

//        frameLayout = findViewById(R.id.frameLayout);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.homeFrameLayout, new HomeFragment());
        ft.commit();


        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationMenuView bottomNavigationMenuView =
                (BottomNavigationMenuView) navView.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(3); // number of menu from left


    }

    @Override
    public void onBackPressed() {
        if (doubleClickedExit){
            super.onBackPressed();
            return;
        }

        this.doubleClickedExit = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleClickedExit = false;
            }
        }, 2000);
    }
}
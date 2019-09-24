package com.example.restaurant_abeerfood.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.restaurant_abeerfood.R;
import com.example.restaurant_abeerfood.utils.Constraints;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

public class NetConnectionCheckActivity extends AppCompatActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView refreshIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_connection_check);

        initialization();
    }

    @SuppressLint("ResourceAsColor")
    private void initialization() {
        refreshIV = findViewById(R.id.refershIV);
        swipeRefreshLayout = findViewById(R.id.netConnectionSwipeRefreshLayout);

        refreshIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constraints.isConnectedToInternet(getApplicationContext())){
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finish();
                } else {
                    DynamicToast.makeError(getApplicationContext(), "Please check your internet connection!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                if (Constraints.isConnectedToInternet(getApplicationContext())){
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finish();
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    DynamicToast.makeError(getApplicationContext(), "Please check your internet connection!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        swipeRefreshLayout.setColorSchemeColors(
                R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark
        );
    }
}

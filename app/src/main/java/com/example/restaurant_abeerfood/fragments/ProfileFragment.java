package com.example.restaurant_abeerfood.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.ConditionVariable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.restaurant_abeerfood.R;
import com.example.restaurant_abeerfood.activities.SignInActivity;
import com.example.restaurant_abeerfood.database.SharedPrefManager;
import com.example.restaurant_abeerfood.model.Shop;
import com.example.restaurant_abeerfood.utils.Constraints;
import com.example.restaurant_abeerfood.utils.MySingleton;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private TextView shopNameTV, slugTV, locationTV, phoneTV, openCloseTV;
    private CardView requestBalanceTransferButton, logoutButton;
    private Button editProfileButton;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView shopIV;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        shopNameTV = rootView.findViewById(R.id.profileShopNameTV);
        slugTV = rootView.findViewById(R.id.profileShopSlugTV);
        locationTV = rootView.findViewById(R.id.profileShopLocationTV);
        phoneTV = rootView.findViewById(R.id.profileShopPhoneTV);
        openCloseTV = rootView.findViewById(R.id.profileShopOpenCloseTV);
        requestBalanceTransferButton = rootView.findViewById(R.id.profileShopCashOutRequestButton);
        logoutButton = rootView.findViewById(R.id.profileShopLogOutButton);
        swipeRefreshLayout = rootView.findViewById(R.id.profileSwipeRefreshLayout);
        editProfileButton = rootView.findViewById(R.id.profileEditButton);
        shopIV = rootView.findViewById(R.id.shopProfileImgIV);


        shopNameTV.setText(Constraints.currentShop.getName());
        slugTV.setText(Constraints.currentShop.getSlug());
        locationTV.setText(Constraints.currentShop.getLocation());
        phoneTV.setText(Constraints.currentShop.getPhoneNumber());
        openCloseTV.setText(Constraints.currentShop.getOpenAt()+"-"+Constraints.currentShop.getCloseAt());

        if (Constraints.currentShop.getImage() != "default.png" || Constraints.currentShop.getImage() != "default.jpg")
            Picasso.with(getActivity()).load(Constraints.IMG_BASE_URL + Constraints.currentShop.getImage()).into(shopIV);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constraints.currentShop = null;
                SharedPrefManager.getInstance(getActivity()).logout();
                startActivity(new Intent(getActivity(), SignInActivity.class));
                getActivity().finish();
            }
        });
        return rootView;
    }

}

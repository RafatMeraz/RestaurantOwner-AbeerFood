package com.example.restaurant_abeerfood.fragments;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.restaurant_abeerfood.R;
import com.example.restaurant_abeerfood.activities.NetConnectionCheckActivity;
import com.example.restaurant_abeerfood.adapters.OrderAdapter;
import com.example.restaurant_abeerfood.model.Order;
import com.example.restaurant_abeerfood.utils.Constraints;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends Fragment {


    private RecyclerView orderRV;
    private ArrayList<Order> orderArrayList;
    private SwipeRefreshLayout orderSwipeTORefreshLayout;

    public OrderFragment() {
        // Required empty public constructor
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_order, container, false);
        orderRV = view.findViewById(R.id.orderAllOrderRecyclerView);
        orderSwipeTORefreshLayout = view.findViewById(R.id.orderSwipeToRefreshLayout);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        orderRV.setLayoutManager(layoutManager);

        orderArrayList = new ArrayList<>();

        orderSwipeTORefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Constraints.isConnectedToInternet(getActivity())){
                    loadAllOrders(Constraints.currentShop.getId());
                } else {
                    startActivity(new Intent(getActivity(), NetConnectionCheckActivity.class));
                    getActivity().finish();
                }
            }
        });
        orderSwipeTORefreshLayout.setColorSchemeColors(
                R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark
        );

        if (Constraints.isConnectedToInternet(getActivity())){
            loadAllOrders(Constraints.currentShop.getId());
        } else {
            startActivity(new Intent(getActivity(), NetConnectionCheckActivity.class));
            getActivity().finish();
        }
        return view;
    }

    private void loadAllOrders(final int id) {
        orderArrayList.clear();
        orderSwipeTORefreshLayout.setRefreshing(true);
        StringRequest request = new StringRequest(Request.Method.POST,
                Constraints.ORDERS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i=0; i<array.length(); i++){
                        JSONObject object = array.getJSONObject(i);
                        int driver = 0;
                        if (object.get("driver_id").equals(null)){
                            driver = 0;
                        } else{
                            driver = object.getInt("driver_id");
                        }
                        Order order = new Order(
                                object.getInt("id"),
                                object.getInt("user_id"),
                                object.getInt("shop_id"),
                                object.getInt("total_quantity"),
                                driver,
                                object.getInt("status"),
                                object.getDouble("total_price"),
                                object.getDouble("latitude"),
                                object.getDouble("longitude"),
                                object.getString("item_list"),
                                object.getString("address"),
                                object.getString("note"),
                                object.getInt("payment_id"),
                                object.getString("transaction_id")
                        );
                        orderArrayList.add(order);
                    }
                    for (Order order: orderArrayList) {
                        Log.e("onResponse: ", order.getItemList());
                    }
                    OrderAdapter adapter = new OrderAdapter(orderArrayList, getActivity());
                    orderRV.setAdapter(adapter);
                    orderSwipeTORefreshLayout.setRefreshing(false);
                } catch (JSONException e) {
                    DynamicToast.makeError(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                DynamicToast.makeError(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("shop_id", String.valueOf(id));
                return params;
            }
        };

        Volley.newRequestQueue(getActivity()).add(request);
    }

}

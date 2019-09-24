package com.example.restaurant_abeerfood.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.restaurant_abeerfood.R;
import com.example.restaurant_abeerfood.model.Food;
import com.example.restaurant_abeerfood.model.Order;
import com.example.restaurant_abeerfood.utils.Constraints;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {

    ArrayList<Order> orderArrayList;
    Context context;

    public OrderAdapter(ArrayList<Order> orderArrayList, Context context) {
        this.orderArrayList = orderArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_order_layout, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        myViewHolder.orderId.setText("Order ID#"+orderArrayList.get(i).getId());
        myViewHolder.priceTV.setText("$ "+ orderArrayList.get(i).getPrice());
        myViewHolder.noteTV.setText(orderArrayList.get(i).getNote());
        myViewHolder.foodListTV.setText(orderArrayList.get(i).getItemList());

        if (orderArrayList.get(i).getStatus()==0) {
            myViewHolder.acceptButton.setEnabled(true);
            myViewHolder.acceptButton.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            myViewHolder.acceptButton.setEnabled(false);
            myViewHolder.acceptButton.setText("Accepted!");
            myViewHolder.acceptButton.setTextColor(context.getResources().getColor(R.color.dark_white));
            myViewHolder.acceptButton.setBackgroundColor(context.getResources().getColor(R.color.holo_green));
        }
        myViewHolder.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFoodStatus(orderArrayList.get(i).getId(), 1);
            }

            private void changeFoodStatus(final int id, final int status) {
                StringRequest request = new StringRequest(Request.Method.POST,
                        Constraints.CHANGE_ORDER_STATUS, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("error")) {
                                DynamicToast.makeError(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            } else {
                                myViewHolder.acceptButton.setEnabled(false);
                                myViewHolder.acceptButton.setText("Accepted!");
                                myViewHolder.acceptButton.setTextColor(context.getResources().getColor(R.color.dark_white));
                                myViewHolder.requestRider.setEnabled(true);
                                myViewHolder.requestRider.setTextColor(context.getResources().getColor(R.color.white));
                                myViewHolder.acceptButton.setBackgroundColor(context.getResources().getColor(R.color.holo_green));
                                DynamicToast.makeSuccess(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        DynamicToast.makeError(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("id", String.valueOf(id));
                        params.put("status", String.valueOf(status));
                        return params;
                    }
                };
                Volley.newRequestQueue(context).add(request);
            }

        });

        if (orderArrayList.get(i).getStatus() == 1) {
            myViewHolder.requestRider.setEnabled(true);
            myViewHolder.requestRider.setTextColor(context.getResources().getColor(R.color.white));
        } else if (orderArrayList.get(i).getStatus() == 2){
            myViewHolder.requestRider.setEnabled(false);
            myViewHolder.requestRider.setText("WAITING");
        } else {
            myViewHolder.requestRider.setEnabled(false);
            myViewHolder.requestRider.setTextColor(context.getResources().getColor(R.color.dark_white));
            myViewHolder.requestRider.setText("DELIVERED");
        }

        myViewHolder.requestRider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFoodStatus(orderArrayList.get(i).getId(), 2);
            }

            private void changeFoodStatus(final int id, final int status) {
                StringRequest request = new StringRequest(Request.Method.POST,
                        Constraints.CHANGE_ORDER_STATUS, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("error")) {
                                DynamicToast.makeError(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            } else {
                                myViewHolder.requestRider.setEnabled(false);
                                myViewHolder.requestRider.setText("WAITING");
                                DynamicToast.makeSuccess(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        DynamicToast.makeError(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("id", String.valueOf(id));
                        params.put("status", String.valueOf(status));
                        return params;
                    }
                };
                Volley.newRequestQueue(context).add(request);
            }

        });

    }


    @Override
    public int getItemCount() {
        return orderArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView orderId, foodListTV, noteTV, priceTV;
        Button acceptButton, requestRider;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.OrdersOrderIdTV);
            foodListTV = itemView.findViewById(R.id.OrdersOrderItemsTV);
            noteTV = itemView.findViewById(R.id.OrdersOderNoteTV);
            priceTV = itemView.findViewById(R.id.OrdersOrderTotalPriceTV);
            acceptButton = itemView.findViewById(R.id.orderAcceptButton);
            requestRider = itemView.findViewById(R.id.orderRequestRiderButton);
        }
    }

}
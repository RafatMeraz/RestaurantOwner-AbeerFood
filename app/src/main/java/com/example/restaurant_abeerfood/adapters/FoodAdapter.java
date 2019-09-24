package com.example.restaurant_abeerfood.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.restaurant_abeerfood.utils.Constraints;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.MyViewHolder> {

    ArrayList<Food> foodArrayList;
    Context context;

    public FoodAdapter(ArrayList<Food> foodArrayList, Context context) {
        this.foodArrayList = foodArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_food_layout, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        myViewHolder.foodNameTV.setText(foodArrayList.get(i).getTitle());
        myViewHolder.priceTV.setText("$ "+foodArrayList.get(i).getPrice());
        myViewHolder.descriptionTV.setText(foodArrayList.get(i).getAbout());
        if (foodArrayList.get(i).getAvailable() == 0) {
            myViewHolder.foodStatus.setChecked(false);
            myViewHolder.rootView.setBackgroundColor(context.getResources().getColor(R.color.dark_white));
            myViewHolder.availableTV.setText("Unavailable");
            myViewHolder.availableTV.setTextColor(Color.parseColor("#ff0000"));
        } else {
            myViewHolder.foodStatus.setChecked(true);
        }
        myViewHolder.foodStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int status = 0;
                if (myViewHolder.foodStatus.isChecked()){
                    status = 1;
                }
                changeFoodStatus(foodArrayList.get(i).getId(), status);
            }

            private void changeFoodStatus(final int id,final int status) {
                StringRequest request = new StringRequest(Request.Method.POST,
                        Constraints.CHANGE_FOOD_STATUS, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("error")){
                                DynamicToast.makeError(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            } else {
                                if (myViewHolder.foodStatus.isChecked()){
                                    myViewHolder.rootView.setBackgroundColor(context.getResources().getColor(R.color.white));
                                    myViewHolder.availableTV.setText("Available");
                                    myViewHolder.availableTV.setTextColor(context.getResources().getColor(R.color.holo_green));
                                } else {
                                    myViewHolder.rootView.setBackgroundColor(context.getResources().getColor(R.color.dark_white));
                                    myViewHolder.availableTV.setText("Unavailable");
                                    myViewHolder.availableTV.setTextColor(Color.parseColor("#ff0000"));
                                }
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
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("id", String.valueOf(id));
                        params.put("available", String.valueOf(status));
                        return params;
                    }
                };
                Volley.newRequestQueue(context).add(request);
            }

        });

    }


    @Override
    public int getItemCount() {
        return foodArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView foodNameTV, descriptionTV, priceTV, availableTV;
        CardView rootView;
        Switch foodStatus;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            foodNameTV = itemView.findViewById(R.id.foodNameTV);
            descriptionTV = itemView.findViewById(R.id.aboutFoodTV);
            priceTV = itemView.findViewById(R.id.foodPriceTV);
            availableTV = itemView.findViewById(R.id.foodAvailableTV);
            rootView = itemView.findViewById(R.id.singleFoodCardView);
            foodStatus = itemView.findViewById(R.id.foodStatusSwitch);
        }
    }

}
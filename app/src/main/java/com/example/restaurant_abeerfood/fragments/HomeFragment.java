package com.example.restaurant_abeerfood.fragments;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.restaurant_abeerfood.R;
import com.example.restaurant_abeerfood.activities.HomeActivity;
import com.example.restaurant_abeerfood.activities.NetConnectionCheckActivity;
import com.example.restaurant_abeerfood.adapters.FoodAdapter;
import com.example.restaurant_abeerfood.database.SharedPrefManager;
import com.example.restaurant_abeerfood.model.Food;
import com.example.restaurant_abeerfood.model.Shop;
import com.example.restaurant_abeerfood.utils.Constraints;
import com.example.restaurant_abeerfood.utils.MySingleton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private SwipeRefreshLayout rootSwipeRefreshLayout;
    private ArrayList<Food> foodList;
    private RecyclerView foodListRV;
    private Switch statusSwitch;
    private FloatingActionButton addFoodFAB;
    private EditText foodNameET, foodSlugET, foodAboutET, foodPriceET;
    private final int PICK_IMG_REQUEST = 75;
    private Uri saveUri;
    private Bitmap bitmap;
    private AlertDialog.Builder addMenuDialog;
    private final String[] categories = {"Fast Food", "Chinese", "Thai"};
    private Spinner categorySpinner;
    private FoodAdapter adapter;

    public HomeFragment() {
        // Required empty public constructor
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_home, container, false);
        rootSwipeRefreshLayout = rootView.findViewById(R.id.homeRefreshLayout);
        foodList = new ArrayList<>();
        foodListRV = rootView.findViewById(R.id.homeAllFoodsRecyclerView);
        statusSwitch = rootView.findViewById(R.id.homeStatusSwitch);
        addFoodFAB = rootView.findViewById(R.id.addFoodFAB);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        foodListRV.setLayoutManager(layoutManager);

        if (Constraints.isConnectedToInternet(getActivity())){
            loadShopDetails(Constraints.currentUser.getId());
        } else {
            startActivity(new Intent(getActivity(), NetConnectionCheckActivity.class));
            getActivity().finish();
        }

        rootSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadShopDetails(Constraints.currentUser.getId());
            }
        });
        rootSwipeRefreshLayout.setColorSchemeColors(
                R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark
        );
        statusSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constraints.isConnectedToInternet(getActivity())){
                    if (statusSwitch.isChecked()==true){
                        changeStatus(1);
                    } else {
                        changeStatus(0);
                    }
                } else {
                    startActivity(new Intent(getActivity(), NetConnectionCheckActivity.class));
                    getActivity().finish();
                }
            }
        });

        addFoodFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddFoodDialog();
            }
        });
        return rootView;
    }

    private void showAddFoodDialog() {
        addMenuDialog = new AlertDialog.Builder(getActivity());
        addMenuDialog.setTitle("Add new Food");
        addMenuDialog.setMessage("Please Add These Info");

        LayoutInflater inflater = this.getLayoutInflater();
        View addMenuLayout = inflater.inflate(R.layout.add_new_food_layout, null);
        foodNameET = addMenuLayout.findViewById(R.id.newFoodNameET);
        foodSlugET = addMenuLayout.findViewById(R.id.newFoodSlugET);
        foodAboutET = addMenuLayout.findViewById(R.id.newFoodAboutET);
        foodPriceET = addMenuLayout.findViewById(R.id.newFoodPriceET);
        categorySpinner = addMenuLayout.findViewById(R.id.categorySpinner);


        List<String> list = new ArrayList<String>();
        list.add("Fast food");
        list.add("Chinese");
        list.add("Thai");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(dataAdapter);

        foodNameET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        foodSlugET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        foodAboutET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        foodPriceET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

//        selectImgButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                chooseImage();
//            }
//        });

        addMenuDialog.setView(addMenuLayout);

        addMenuDialog.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (Constraints.isConnectedToInternet(getActivity())){
                    uploadNewFood();
                } else {
                    startActivity(new Intent(getActivity(), NetConnectionCheckActivity.class));
                    getActivity().finish();
                }
            }
        });
        addMenuDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        addMenuDialog.show();

    }

    private void uploadNewFood() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constraints.UPLOAD_NEW_FOOD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject array = new JSONObject(response);
                            if (array.getBoolean("error")){
                                DynamicToast.makeError(getActivity(), "New Food Upload Failed!", Toast.LENGTH_SHORT).show();
                            }else {
                                rootSwipeRefreshLayout.setRefreshing(true);
                                loadShopFoodList(Constraints.currentShop.getId());

                                DynamicToast.makeSuccess(getActivity(), "New Food Uploaded!", Toast.LENGTH_SHORT).show();
                            }
//                            uploadNewFoodImage();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(Constraints.currentUser.getId()));
                params.put("shop_id", String.valueOf(Constraints.currentShop.getId()));
                params.put("category_id", String.valueOf(categorySpinner.getSelectedItemId()+1));
                params.put("title", foodNameET.getText().toString());
                params.put("slug", foodSlugET.getText().toString());
                params.put("about", foodAboutET.getText().toString());
                params.put("price", foodPriceET.getText().toString());
                return params;
            }
        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);


    }

    private void uploadNewFoodImage() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constraints.UPLOAD_PROFILE_IMAGE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject array = new JSONObject(response);
                            if (array.getBoolean("error")){
                                DynamicToast.makeError(getActivity(), "Profile Image Upload Failed!", Toast.LENGTH_SHORT).show();
                            }else {

                                DynamicToast.makeSuccess(getActivity(), "Profile Image Uploaded!", Toast.LENGTH_SHORT).show();
                            }
                            Log.e( "onResponse: ", array.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(Constraints.currentUser.getId()));
                params.put("image", imageToString(bitmap));
                return params;
            }
        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);


    }

    private String imageToString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMG_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMG_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            saveUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), saveUri);
//                selectImgButton.setText("Image Selected!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void changeFoodStatus(int id, int status) {
        Log.e("changeFoodStatus: ", id+""+status);
    }

    private void changeStatus(final int i) {
        StringRequest request = new StringRequest(Request.Method.POST,
                Constraints.CHANGE_STATUS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("error")) {
                        DynamicToast.makeError(getActivity(),  jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    } else if (jsonObject.getBoolean("error")==false){
                        if (statusSwitch.isChecked()){
                            DynamicToast.makeSuccess(getActivity(), "Shop is OPEN now!", Toast.LENGTH_SHORT).show();
                        } else {
                            DynamicToast.makeError(getActivity(), "Shop is CLOSE now!", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("status", String.valueOf(i));
                params.put("shop_id", String.valueOf(Constraints.currentShop.getId()));
                return params;
            }
        };

        MySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

    private void loadShopDetails(final int id){
        rootSwipeRefreshLayout.setRefreshing(true);
        StringRequest request = new StringRequest(Request.Method.POST,
                Constraints.SHOP_DETAILS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("error")) {
                        DynamicToast.makeError(getActivity(),  jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    } else if (jsonObject.getBoolean("error")==false){
                        JSONObject object = jsonObject.getJSONObject("0");
                        Constraints.currentShop = new Shop(
                                object.getInt("id"),
                                object.getInt("is_open"),
                                object.getString("shop_name"),
                                object.getString("slug"),
                                object.getString("phone_number"),
                                object.getString("trade_license"),
                                object.getString("open_at"),
                                object.getString("close_at"),
                                object.getString("image"),
                                object.getString("location")
                        );
                        if (Constraints.currentShop.getIsOpen()==1){
                            statusSwitch.setChecked(true);
                        } else {
                            statusSwitch.setChecked(false);
                        }
                        loadShopFoodList(Constraints.currentShop.getId());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(id));
                return params;
            }
        };

        MySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

    private void loadShopFoodList(final int id) {

        foodList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constraints.FOODS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting shop object from json array
                                JSONObject shop = array.getJSONObject(i);
                                //adding the shop to shop list
                                foodList.add(new Food(
                                        shop.getInt("id"),
                                        shop.getInt("available"),
                                        shop.getString("title"),
                                        shop.getString("slug"),
                                        shop.getString("about"),
                                        shop.getString("image"),
                                        shop.getDouble("price")
                                ));
                            }

                            //creating adapter object and setting it to recyclerview
                            adapter = new FoodAdapter(foodList, getActivity());
                            foodListRV.setAdapter(adapter);
                            rootSwipeRefreshLayout.setRefreshing(false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("shop_id", String.valueOf(id));
                return params;
            }
        };

        //adding our stringrequest to queue
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }

    public void checkInputs(){
        if (!TextUtils.isEmpty(foodNameET.getText().toString())){
            if (!TextUtils.isEmpty(foodSlugET.getText().toString())){
                if (!TextUtils.isEmpty(foodAboutET.getText().toString())){
                    if (!TextUtils.isEmpty(foodPriceET.getText().toString())){

                    } else {
                        foodPriceET.setError("Enter your food price");
                    }
                } else {
                    foodAboutET.setError("Enter your food Description");
                }
            } else {
                foodSlugET.setError("Enter your food slug");
            }
        } else {
            foodNameET.setError("Enter your food name");
        }
    }

}

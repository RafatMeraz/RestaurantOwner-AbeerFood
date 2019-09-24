package com.example.restaurant_abeerfood.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


import com.example.restaurant_abeerfood.model.Shop;
import com.example.restaurant_abeerfood.model.User;


public class Constraints {
    public static final String IMG_BASE_URL = "http://www.mykingscoder.xyz/Android/images/";
    public static final String UPDATE = "Update";
    public static final String DELETE = "Delete";
    public static final String STATUS = "Change status";
    private static final String BASE_URL = "http://www.mykingscoder.xyz/Android/v2/";
    public static final String CHANGE_STATUS = BASE_URL +"changeStatus.php";
    public static final String CHANGE_FOOD_STATUS = BASE_URL + "changeFoodStatus.php";
    public static final String CHANGE_ORDER_STATUS = BASE_URL + "changeOrderStatus.php";
    public static final String USER_LOGIN_URL = BASE_URL +"author.php";
    public static final String CNANGE_STATUS_URL = BASE_URL +"changeStatus.php";
    public static final String NEW_SHOPS_URL = BASE_URL +"newShops.php";
    public static final String SLIDERS_URL = BASE_URL +"sliders.php";
    public static final String FOODS_URL = BASE_URL +"allFoodsOfShop.php";
    public static final String SHOP_DETAILS_URL = BASE_URL +"getShopInfo.php";
    public static final String USER_DETAILS_URL = BASE_URL +"userDetails.php";
    public static final String UPDATE_USER_URL = BASE_URL +"updateUser.php";
    public static final String CATEGORY_URL = BASE_URL +"foodCategory.php";
    public static final String ORDERS_URL = BASE_URL +"allOrderOfShop.php";
    public static final String UPLOAD_NEW_FOOD = BASE_URL +"insertFood.php";
    public static final String UPLOAD_PROFILE_IMAGE_URL = BASE_URL +"uploadFoodImg.php";

    public static User currentUser;
    public static User currentUserDetails;
    public static Shop currentShop;


    public static boolean isConnectedToInternet(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null){
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null){
                for (int i=0; i<info.length; i++){
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }
}


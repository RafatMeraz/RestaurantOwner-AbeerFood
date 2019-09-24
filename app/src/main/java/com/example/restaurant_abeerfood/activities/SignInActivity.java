package com.example.restaurant_abeerfood.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.restaurant_abeerfood.R;
import com.example.restaurant_abeerfood.database.SharedPrefManager;
import com.example.restaurant_abeerfood.utils.Constraints;
import com.example.restaurant_abeerfood.utils.MySingleton;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends AppCompatActivity {

    private EditText emailET, passET;
    private Button signInButton;
    private TextView forgotPassTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        initialization();
    }

    private void initialization() {
        emailET = findViewById(R.id.signInEmailET);
        passET = findViewById(R.id.signInPassET);
        signInButton = findViewById(R.id.signInButton);
        forgotPassTV = findViewById(R.id.signInForgotPasswordTV);

        emailET.addTextChangedListener(new TextWatcher() {
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
        passET.addTextChangedListener(new TextWatcher() {
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

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constraints.isConnectedToInternet(getApplicationContext()))
                    loginUser();
                else {
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finish();
                }
            }
        });
    }

    private void checkInputs() {
        if (!TextUtils.isEmpty(emailET.getText().toString()) && Patterns.EMAIL_ADDRESS.matcher(emailET.getText().toString()).matches()) {
            if (!TextUtils.isEmpty(passET.getText().toString()) && (passET.getText().toString().length() >= 6)) {
                signInButton.setEnabled(true);
                signInButton.setTextColor(getApplication().getResources().getColor(android.R.color.white));

            } else {
                signInButton.setEnabled(false);
                passET.setError("Enter your password more than 6 characters.");
                signInButton.setTextColor(getApplication().getResources().getColor(R.color.dark_white));
            }
        } else {
            signInButton.setEnabled(false);
            emailET.setError("Enter your valid email address.");
            signInButton.setTextColor(getApplication().getResources().getColor(R.color.dark_white));

        }
    }

    private void loginUser() {
        final String email = emailET.getText().toString();
        final String password = passET.getText().toString();

        final ProgressDialog mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Logging");
        mProgressDialog.setMessage("Please wait for a while...");
        mProgressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constraints.USER_LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            mProgressDialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("error")) {
                                DynamicToast.makeError(getApplicationContext(),  jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            } else if (jsonObject.getBoolean("error")==false){
                                JSONObject object = jsonObject.getJSONObject("0");
                                String userName = object.getString("name");
                                String email = object.getString("email");
                                int id = object.getInt("id");
                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(id, userName, email);
                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.hide();
                Toast.makeText(getApplicationContext(), "Response error : "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }

}

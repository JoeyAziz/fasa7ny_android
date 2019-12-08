package com.example.minitest.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.minitest.R;
import com.example.minitest.main.network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Profile extends AppCompatActivity {

    //Layout components
    TextView name_view;
    TextView email_view;
    Button delete_btn;
    Button fav_loc_button;

    //shared pref.
    SharedPreferences pref;

    String result_json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //VIEWS
        name_view = (TextView)findViewById(R.id.name_view);
        email_view = (TextView)findViewById(R.id.email_view);
        delete_btn = (Button)findViewById(R.id.delete_acc_button);
        fav_loc_button = findViewById(R.id.fav_loc_button);

        //PREF.
        pref = getSharedPreferences("user_details",MODE_PRIVATE);

        getUser();

        //DELETE BUTTON
        delete_btn.setOnClickListener(new View.OnClickListener() {//ON CLICK
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Profile.this, DeleteAccount.class) );
            }
        });

        //GOTO PROFILE BUTTON
        fav_loc_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Profile.this, fav_locations.class));
            }
        });
    }

    //GET USER
    private void getUser(){
        RequestQueue queue = Volley.newRequestQueue(this);
        //INPUT URL ROUTE
        String url = network.getBackend_ip() +"user/"+ pref.getString("email",null);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest (Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String  response) {
                        try {

                            //CONVERT RESPONSE TO JSON OBJ
                            JSONObject jObject = new JSONObject(response);
                            JSONArray jArray = jObject.getJSONArray("data");
                            JSONObject JObject_array = (JSONObject) jArray.get(0);
                            result_json = JObject_array.getString("first_name");
                            result_json = result_json + "  " + JObject_array.getString("last_name");

                        }catch(JSONException e){}
                        name_view.setText(result_json);
                        email_view.setText(pref.getString("email",null));
                    }
                }, new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        //result_lab.setText("That didn't work!");
                    }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

}

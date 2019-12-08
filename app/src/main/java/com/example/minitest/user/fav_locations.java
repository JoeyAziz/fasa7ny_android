package com.example.minitest.user;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.minitest.R;
import com.example.minitest.fasahny_functions.ShowMarkerInfo;
import com.example.minitest.main.Loading_Screen;
import com.example.minitest.main.network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class fav_locations extends AppCompatActivity {
    TableLayout likes_table;

    String requestBody;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_locations);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        likes_table = findViewById(R.id.likes_table);
        likes_table.setBackgroundResource(R.drawable.g);

        pref = getSharedPreferences("user_details",MODE_PRIVATE);
        //get info from pref file
        getUserFavLoc(pref.getString("email",null));
    }

    //Get user's fav. Locations
    private void getUserFavLoc(String email) {
        RequestQueue queue = Volley.newRequestQueue(this);
        //INPUT URL ROUTE
        String url = network.getBackend_ip() + "user/favourite/get";
        try {
            //JSON OBJECT TO BE SENT IN ROUTE
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("email", email);
            requestBody = jsonBody.toString();

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //CONVERT RESPONSE TO JSON OBJ
                            CreateHeaders();
                            try {

                                JSONObject jObject = new JSONObject(response);
                                JSONArray jArray  = jObject.getJSONArray("data");


                                for(int i=0; i<jArray.length(); i++) {
                                    JSONObject JObject_array = (JSONObject) jArray.get(0);
                                    String name = JObject_array.getString("location_name");
                                    String budget = JObject_array.getString("budget");
                                    String country = JObject_array.getString("country_name");
                                    String city = JObject_array.getString("city_name");

                                    TableRow row = new TableRow(fav_locations.this);
                                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                                    row.setLayoutParams(lp);
                                    TextView name_ = new TextView(fav_locations.this);
                                    name_.setPadding(10,0,10,5);
                                    TextView budget_ = new TextView(fav_locations.this);
                                    budget_.setPadding(10,0,10,5);
                                    TextView country_ = new TextView(fav_locations.this);
                                    country_.setPadding(10,0,10,5);
                                    TextView city_ = new TextView(fav_locations.this);
                                    city_.setPadding(10,0,10,5);

                                    name_.setText(name);
                                    budget_.setText(budget);
                                    country_.setText(country);
                                    city_.setText(city);

                                    CheckBox like_button = new CheckBox(fav_locations.this);
                                    like_button.setChecked(true);
                                    like_button.setBackgroundResource(R.drawable.like_button_background);
                                    like_button.setPadding(10,0,10,5);


                                    row.addView(name_);
                                    row.addView(budget_);
                                    row.addView(country_);
                                    row.addView(city_);
                                    row.addView(like_button);
                                    likes_table.addView(row, i+1);
                                }
                            }
                            catch(Exception e){
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "No Likes :(", Toast.LENGTH_SHORT).show();

                }
            }) {
                @Override
                public String getBodyContentType() {
                    return String.format("application/json; charset=utf-8");
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        return null;
                    }
                }
            };
            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        } catch (JSONException e) {
        }

    }

    public void CreateHeaders(){
        TableRow row = new TableRow(fav_locations.this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(lp);
        TextView name_ = new TextView(fav_locations.this);
        name_.setPadding(10,0,10,10);
        TextView budget_ = new TextView(fav_locations.this);
        budget_.setPadding(10,0,10,10);
        TextView country_ = new TextView(fav_locations.this);
        country_.setPadding(10,0,10,10);
        TextView city_ = new TextView(fav_locations.this);
        city_.setPadding(10,0,10,10);

        name_.setText("NAME");
        name_.setTextColor(Color.parseColor("#03A9F4"));
        budget_.setText("BUDGET");
        budget_.setTextColor(Color.parseColor("#03A9F4"));
        country_.setText("COUNTRY");
        country_.setTextColor(Color.parseColor("#03A9F4"));
        city_.setText("CITY");
        city_.setTextColor(Color.parseColor("#03A9F4"));

        row.addView(name_);
        row.addView(budget_);
        row.addView(country_);
        row.addView(city_);
        likes_table.addView(row, 0);
    }
}

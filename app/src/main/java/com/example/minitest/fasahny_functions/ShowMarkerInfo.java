package com.example.minitest.fasahny_functions;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.minitest.R;
import com.example.minitest.main.Loading_Screen;
import com.example.minitest.main.network;
import com.example.minitest.user.fav_locations;
import com.google.android.gms.maps.model.Marker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class ShowMarkerInfo extends AppCompatActivity {

    //layout components
    CheckBox like_button;
    TextView info_view;
    ImageView marker_image;

    //other variables
    public Activity act;
    private String URL;
    String image_url;
    String requestBody;
    static Loading_Screen ld;
    private Marker pressedMarker;
    SharedPreferences pref;
    static String loc_id;
    String loc_name;
    String loc_country;
    String loc_city;
    String loc_budget;
    String loc_lat;
    String loc_lon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_marker_info);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        pref = getSharedPreferences("photo_details",MODE_PRIVATE);
        //get info from pref file
        loc_name = pref.getString("location_name",null);
        loc_country = pref.getString("location_country",null);
        loc_city = pref.getString("location_city",null);
        loc_lat = pref.getString("location_lat",null);
        loc_lon = pref.getString("location_lon",null);
        getLocInfo(loc_country, loc_city, loc_name, loc_lat, loc_lon);

        info_view = findViewById(R.id.info_view);
        act = this;
        ld = new Loading_Screen();
        this.URL = pref.getString("location_name",null);
        getLocationPic(URL);
        marker_image = findViewById(R.id.marker_image_view);
        like_button = findViewById(R.id.like_button);

    }
    public void setURL(String url){
        image_url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="
                +url+"&key="+ "AIzaSyDSgMKj2v2Du6FacPu0ifxndH5AUNzcey8";

        new ImageDownloader(marker_image).execute(image_url);
        //System.out.println(image_url);
    }

    class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public ImageDownloader(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap mIcon = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                mIcon = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
            }
            return mIcon;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
            ld.finishLoader(ShowMarkerInfo.this);
        }
    }

    public void SetInfoView(String s){
        info_view.setText(s);
    }

    public void setMarker(Marker marker){
        this.pressedMarker = marker;
    }

    @Override
    public void onBackPressed() {
        //DISABLE BACK BUTTON
        SharedPreferences pref2 = getSharedPreferences("user_details",MODE_PRIVATE);
        //add location to user's list
        if(like_button.isChecked())
            addLocationToUser(pref2.getString("email", null), loc_id);

        act.finish();
    }

    //LOCATION Picture
    private void getLocationPic(String location_name){
        location_name = location_name.trim();
        location_name = location_name.replaceAll("\\s", "%20");


        RequestQueue queue = Volley.newRequestQueue(this);
        String api_key = "AIzaSyDSgMKj2v2Du6FacPu0ifxndH5AUNzcey8";
        String url =
                " https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input="+
                        location_name+"&inputtype=textquery&fields=photos&key="
                        +api_key;
        System.out.println(url);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {

                        //CONVERT RESPONSE TO JSON OBJ
                        try {
                            ld.startLoader(ShowMarkerInfo.this);
                            JSONObject jObject = new JSONObject(response);
                            JSONArray jArray  = jObject.getJSONArray("candidates");
                            JSONObject JObject_array = (JSONObject) jArray.get(0);
                            /*PHOTO*/
                            JSONArray photo_array = JObject_array.getJSONArray("photos");
                            JSONObject JObject_photo_array = (JSONObject) photo_array.get(0);
                            String photo_url = JObject_photo_array.getString("photo_reference");
                            /***********/

                            setURL(photo_url);
                        }
                        catch(Exception e){
                            String img = "https://images.unsplash.com/photo-1524850011238-e3d235c7d4c9?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=787&q=80";
                            new ImageDownloader(marker_image).execute(img);
                        }
                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
            }
        })
        {
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
    }

    //get location information
    private void getLocInfo(String country, String city, String name, String lat, String lon) {
        RequestQueue queue = Volley.newRequestQueue(this);
        //INPUT URL ROUTE
        String url = network.getBackend_ip() + "area/getLocationInfo";
        try {
            //JSON OBJECT TO BE SENT IN ROUTE
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("country", country);
            jsonBody.put("city", city);
            jsonBody.put("name", name);
            jsonBody.put("lat", lat);
            jsonBody.put("lon", lon);
            requestBody = jsonBody.toString();

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //CONVERT RESPONSE TO JSON OBJ
                            try {

                                JSONObject jObject = new JSONObject(response);
                                JSONArray jArray  = jObject.getJSONArray("data");
                                JSONObject JObject_array = (JSONObject) jArray.get(0);
                                loc_id = JObject_array.getString("loc_id");
                                loc_name = JObject_array.getString("location_name");
                                loc_country = JObject_array.getString("country_name");
                                loc_city = JObject_array.getString("city_name");
                                loc_budget = JObject_array.getString("budget");

                                String info = "Name: " + loc_name + "\nBudget: "+ loc_budget + "\nCountry: "+ loc_country + "\nCity: " + loc_city;
                                SetInfoView(info);
                            }
                            catch(Exception e){ }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

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

    //Add location user's fav. list

    private void addLocationToUser(String email, String loc_id_) {
        RequestQueue queue = Volley.newRequestQueue(this);
        //INPUT URL ROUTE
        String url = network.getBackend_ip() + "user/favourite/location";
        try {
            //JSON OBJECT TO BE SENT IN ROUTE
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("email", email);
            jsonBody.put("loc_id", loc_id_);
            requestBody = jsonBody.toString();

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

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

}

package com.example.minitest.fasahny_functions;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Filter;
import android.widget.Toast;

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
import com.example.minitest.user.Login;
import com.example.minitest.user.Register;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ShowRoutes extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    String requestBody;

    static Loading_Screen ld;

    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_routes);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        pref = getSharedPreferences("photo_details",MODE_PRIVATE);
        ld = new Loading_Screen();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getLocationsInAreaWithBudget(Filter_Choices.country_value, Filter_Choices.city_value, Filter_Choices.budget_value);
        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
    }

    ArrayList<String> info = new ArrayList<>();
    private void getLocationsInAreaWithBudget(String country, String city, double budget) {
        //save info in the pref. file
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("location_country",  country);
        editor.putString("location_city", city);

        editor.commit();

        RequestQueue queue = Volley.newRequestQueue(this);
        //INPUT URL ROUTE
        String url = network.getBackend_ip() + "fasahny/GetLocationsWithBudgetInArea";
        try {
            //JSON OBJECT TO BE SENT IN ROUTE
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("country", country);
            jsonBody.put("city", city);
            jsonBody.put("budget", budget);

            requestBody = jsonBody.toString();


            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //SWITCH TO GOOGLE MAPS
                            LatLngBounds.Builder builder = new LatLngBounds.Builder();
                            //CONVERT RESPONSE TO JSON OBJ
                            JSONObject jObject = null;
                            try {
                                jObject = new JSONObject(response);

                                JSONArray jArray = jObject.getJSONArray("data");
                                for (int i = 0; i < jArray.length(); i++) {
                                    JSONObject JObject_array = (JSONObject) jArray.get(i);
                                    double lat = Double.valueOf(JObject_array.getString("latitude"));
                                    double lon = Double.valueOf(JObject_array.getString("longitude"));
                                    String name = (JObject_array.getString("location_name"));

                                    info.add(name);
                                    info.add(String.valueOf(lat));
                                    info.add(String.valueOf(lon));

                                    LatLng location = new LatLng(lat, lon);
                                    BitmapDrawable b=(BitmapDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.flag, null);
                                    Bitmap marker = b.getBitmap();
                                    mMap.addMarker(new MarkerOptions().position(location).title(name).icon(BitmapDescriptorFactory.fromBitmap(marker))).showInfoWindow();
                                    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                        @Override
                                        public void onInfoWindowClick(Marker marker) {

                                            Intent intent = new Intent(ShowRoutes.this, ShowMarkerInfo.class);
                                            new ShowMarkerInfo().setMarker(marker);
                                            SharedPreferences.Editor editor = pref.edit();
                                            for(String s : info){
                                                if(s.equals(marker.getTitle())){
                                                    int index = info.indexOf(s);
                                                    String lat = info.get(index+1);
                                                    String lon = info.get(index+2);
                                                    editor.putString("location_lat",  lat);
                                                    editor.putString("location_lon",  lon);
                                                }
                                            }
                                            editor.putString("location_name",  marker.getTitle());

                                            editor.commit();
                                            startActivity(intent);

                                        }
                                    });
                                    builder.include(location);
                                }
                                LatLngBounds bounds = builder.build();

                                int width = getResources().getDisplayMetrics().widthPixels;
                                int height = getResources().getDisplayMetrics().heightPixels;
                                int padding = (int) (width * 0.10); // offset from edges of the map 10% of screen

                                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);

                                mMap.animateCamera(cu);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            ld.finishLoader(ShowRoutes.this);

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    ld.finishLoader(ShowRoutes.this);
                    Toast.makeText(getApplicationContext(), "CHANGE BUDGET TO FIND MORE PLACES", Toast.LENGTH_SHORT).show();
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

package com.example.minitest.admin_functions;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Delete_Area extends AppCompatActivity {

    Button delete_button;
    Spinner country_spinner;
    Spinner city_spinner;

    List<String> countries = new ArrayList();
    List<String> cities = new ArrayList();

    String requestBody;
    String country_value = "--Select";
    String city_value = "--Select";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete__area);


        country_spinner = findViewById(R.id.area_country_list);
        city_spinner = findViewById(R.id.area_city_list);

        addItemsToCountryList();
        addListenerOnaddItemsToCountryList();

        addItemsToCityList();
        addListenerOnaddItemsToCityList();


        delete_button = findViewById(R.id.area_delete_area);
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(! ( country_value.equals("--Select") || city_value.equals("--Select")  ) ){
                    deleteArea(country_value, city_value);
                }
            }
        });

    }

    //COUNTRY SPINNER <PART>
    private List<String> getAvailableCountries() {

        countries.clear();
        RequestQueue queue = Volley.newRequestQueue(this);
        //INPUT URL ROUTE
        String url = network.getBackend_ip() + "area/getCountries";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //CONVERT RESPONSE TO JSON OBJ
                        JSONObject jObject = null;
                        try {
                            jObject = new JSONObject(response);

                            JSONArray jArray = jObject.getJSONArray("data");
                            for(int i=0; i<jArray.length(); i++) {
                                JSONObject JObject_array = (JSONObject) jArray.get(i);
                                countries.add(JObject_array.getString("country_name"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Failed Transaction", Toast.LENGTH_SHORT).show();
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

        return countries;
    }

    private void addItemsToCountryList() {
        List<String> list = getAvailableCountries();

        list.add("--Select");
        for(String s : list){
            list.add(s);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        country_spinner.setAdapter(dataAdapter);
    }

    private void addListenerOnaddItemsToCountryList() {
        country_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //stuff here to handle item selection
                country_value = parent.getItemAtPosition(position).toString();
                if(!country_value.equals(("--Select")))
                    Toast.makeText(getApplicationContext(),parent.getItemAtPosition(position).toString() + " is selected",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //CITY SPINNER <PART>
    private List<String> getAvailableCities() {

        cities.clear();
        RequestQueue queue = Volley.newRequestQueue(this);
        //INPUT URL ROUTE
        String url = network.getBackend_ip() + "area/getCities";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //CONVERT RESPONSE TO JSON OBJ
                        JSONObject jObject = null;
                        try {
                            jObject = new JSONObject(response);

                            JSONArray jArray = jObject.getJSONArray("data");
                            for(int i=0; i<jArray.length(); i++) {
                                JSONObject JObject_array = (JSONObject) jArray.get(i);
                                cities.add(JObject_array.getString("city_name"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Failed Transaction", Toast.LENGTH_SHORT).show();
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

        return cities;
    }

    private void addItemsToCityList() {
        List<String> list = getAvailableCities();

        list.add("--Select");
        for(String s : list){
            list.add(s);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        city_spinner.setAdapter(dataAdapter);
    }

    private void addListenerOnaddItemsToCityList() {
        city_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //stuff here to handle item selection
                city_value = parent.getItemAtPosition(position).toString();
                if(!city_value.equals(("--Select")))
                    Toast.makeText(getApplicationContext(),parent.getItemAtPosition(position).toString() + " is selected",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //ON AREA DELETE <BUTTON>
    private List<String> deleteArea(String country, String city) {

        RequestQueue queue = Volley.newRequestQueue(this);
        //INPUT URL ROUTE
        String url = network.getBackend_ip() + "admin/area/delete";

        try {
            //JSON OBJECT TO BE SENT IN ROUTE
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("country", country);
            jsonBody.put("city", city);
            requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getApplicationContext(), "Area Deleted", Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Area Does not Exist", Toast.LENGTH_SHORT).show();
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
        }catch(JSONException e ){}
        return countries;
    }
}

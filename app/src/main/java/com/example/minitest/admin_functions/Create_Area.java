package com.example.minitest.admin_functions;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Create_Area extends AppCompatActivity {

    Button create_area_button;
    Spinner country_spinner;
    EditText city_value;
    List country_list = new ArrayList();
    String country_value = "--Select";
    String requestBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__area);

        country_spinner = (Spinner) findViewById(R.id.area_country_list);
        city_value = findViewById(R.id.area_city_field);

        getAllCountries();
        addItemsToCountryList();
        addListenerOnaddItemsToCountryList();

        create_area_button = findViewById(R.id.area_create_area);
        create_area_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!( country_value.equals("--Select") || city_value.getText().toString().isEmpty() )){
                    createArea(country_value, city_value.getText().toString());
                }
            }
        });
    }

    private void createArea(String country, String city){
        RequestQueue queue = Volley.newRequestQueue(this);
        //INPUT URL ROUTE
        String url = network.getBackend_ip() +"admin/area/create";
        try {
            //JSON OBJECT TO BE SENT IN ROUTE
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("country", country);
            jsonBody.put("city",  city);
            requestBody = jsonBody.toString();


            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getApplicationContext(),"Area created successfully",Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    Toast.makeText(getApplicationContext(),"Failed Transaction",Toast.LENGTH_SHORT).show();
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
        }catch (JSONException e){}

    }


    private void getAllCountries(){
        new Thread(){
            public void run() {
                String[] locales = Locale.getISOCountries();
                List<String> res = new ArrayList<>();

                for (String countryCode : locales) {
                    Locale obj = new Locale("", countryCode);
                    country_list.add(obj.getDisplayCountry());
                /*System.out.println("Country Code = " + obj.getCountry()
                        + ", Country Name = " + obj.getDisplayCountry());*/
                }
            }
        }.start();
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

    private void addItemsToCountryList() {
        List<String> list = country_list;
        try {
            list.add("--Select");
            for (String s : list) {
                list.add(s);
            }
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            country_spinner.setAdapter(dataAdapter);
        }catch(Exception e){
        }
    }

}

package com.example.minitest.admin_functions;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

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

public class Set_Admin extends AppCompatActivity {
    ToggleButton show_admins;
    Button submit_button;
    Spinner admin_list;
    Switch admin_status_value;

    String requestBody;
    String email_value = "--Select";
    List admins = new ArrayList();


    boolean showAdmins = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set__admin);

        admin_status_value = findViewById(R.id.set_admin_value);

        addItemsToAdminList();
        addListenerOnaddItemsToAdminList();

        //Filter button
        show_admins = findViewById(R.id.toggleButton);
        show_admins.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showAdmins = isChecked;
                addItemsToAdminList();
                addListenerOnaddItemsToAdminList();
            }
        });

        //Submit button
        submit_button = findViewById(R.id.submit_admin_button);
        submit_button.setOnClickListener(new View.OnClickListener() {//ON CLICK
            @Override
            public void onClick(View view) {
            if(!email_value.equals("--Select")){ //if not --Select
                setAdminStatus(email_value, admin_status_value.isChecked());
            }
            }
        });
    }

    private void setAdminStatus(String email, boolean val){
        RequestQueue queue = Volley.newRequestQueue(this);
        //INPUT URL ROUTE
        String url = network.getBackend_ip() +"admin/makeAdmin";
        try {
            //JSON OBJECT TO BE SENT IN ROUTE
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("email", email);
            jsonBody.put("val",  val);
            requestBody = jsonBody.toString();


            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getApplicationContext(),"Success Changes",Toast.LENGTH_SHORT).show();
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


    private void addItemsToAdminList() {

        admin_list = (Spinner) findViewById(R.id.admin_list);
        List<String> list;
        if(showAdmins)
           list = getAllAdmins();
        else list = getAllUsers();

        list.add("--Select");
        for(String s : list){
            list.add(s);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        admin_list.setAdapter(dataAdapter);
    }

    private void addListenerOnaddItemsToAdminList() {
        admin_list = (Spinner) findViewById(R.id.admin_list);
        admin_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //stuff here to handle item selection
                email_value = parent.getItemAtPosition(position).toString();
                if(!email_value.equals(("--Select")))
                    Toast.makeText(getApplicationContext(),parent.getItemAtPosition(position).toString() + " is selected",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private List<String> getAllAdmins() {
        admins.clear();
        RequestQueue queue = Volley.newRequestQueue(this);
        //INPUT URL ROUTE
        String url = network.getBackend_ip() + "admin/getAdmins";

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
                                    admins.add(JObject_array.getString("email"));
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

        return admins;
    }

    private List<String> getAllUsers() {
        admins.clear();
        RequestQueue queue = Volley.newRequestQueue(this);
        //INPUT URL ROUTE
        String url = network.getBackend_ip() + "admin/getUsers";

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
                                admins.add(JObject_array.getString("email"));
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

        return admins;
    }

}

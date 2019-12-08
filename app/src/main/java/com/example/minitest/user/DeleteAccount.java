package com.example.minitest.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

public class DeleteAccount extends AppCompatActivity {

    Button delete_btn;
    EditText email;
    EditText pass;

    String requestBody;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        pref = getSharedPreferences("user_details",MODE_PRIVATE);
        email = (EditText) findViewById((R.id.email_field));
        pass = (EditText) findViewById((R.id.pass_field));

        //DELETE ACCOUNT BUTTON
        delete_btn = (Button) findViewById(R.id.delete_acc_button);
        delete_btn.setOnClickListener(new View.OnClickListener() {//ON CLICK
            @Override
            public void onClick(View view) {
                if( email.equals(pref.getString("email", null))){
                    deleteUser();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Wrong credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void deleteUser(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = network.getBackend_ip() +"user/delete";
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("email", email.getText().toString());
            jsonBody.put("pass", pass.getText().toString());
            requestBody = jsonBody.toString();

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getApplicationContext(), "You can register again :) ", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(DeleteAccount.this, Register.class) );
                        }
                    }, new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    Toast.makeText(getApplicationContext(), "Wrong credentials", Toast.LENGTH_SHORT).show();
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
}

package com.example.minitest.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.minitest.R;
import com.example.minitest.main.Home;
import com.example.minitest.main.Loading_Screen;
import com.example.minitest.main.network;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


public class Login extends AppCompatActivity {

    //Layout components
    Button login_btn;
    EditText email;
    EditText pass;
    TextView result_lab;
    TextView goto_register;

    //Shared pref.
    SharedPreferences pref;

    //variables
    String requestBody;

    //Pause screen
    Loading_Screen ld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ld = new Loading_Screen();

        //Fields
        email = (EditText) findViewById((R.id.email_field));
        pass = (EditText) findViewById((R.id.pass_field));

        //SHARED PREF.
        pref = getSharedPreferences("user_details",MODE_PRIVATE);

        //LOGIN BUTTON
        login_btn = (Button) findViewById(R.id.login_button);
        login_btn.setOnClickListener(new View.OnClickListener() {//ON CLICK
            @Override
            public void onClick(View view) {

                ld.startLoader(Login.this);
                loginUser();
            }
        });

        //Goto Register
        goto_register = findViewById(R.id.goto_register);
        goto_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });
    }
    private void loginUser() {
       RequestQueue queue = Volley.newRequestQueue(this);
       //INPUT URL ROUTE
       String url = network.getBackend_ip() + "login/";
       try {
           //JSON OBJECT TO BE SENT IN ROUTE
           JSONObject jsonBody = new JSONObject();
           jsonBody.put("email", email.getText().toString());
           jsonBody.put("pass", pass.getText().toString());
           requestBody = jsonBody.toString();

           // Request a string response from the provided URL.
           StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                   new Response.Listener<String>() {
                       @Override
                       public void onResponse(String response) {

                           SharedPreferences.Editor editor = pref.edit();
                           editor.putString("email", email.getText().toString());
                           editor.putString("pass", pass.getText().toString());
                           editor.commit();
                           checkUserIsAdmin(email.getText().toString());
                       }
                   }, new Response.ErrorListener() {
               @Override
               public void onErrorResponse(VolleyError error) {
                   ld.finishLoader(Login.this);
                   Toast.makeText(getApplicationContext(), "Credentials are not valid", Toast.LENGTH_SHORT).show();

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

   //Check if User is admin
   protected void checkUserIsAdmin(String email){
       RequestQueue queue = Volley.newRequestQueue(this);
       //INPUT URL ROUTE
       String url = network.getBackend_ip() +"admin/checkAdmin/"+email;
       // Request a string response from the provided URL.
       StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
               new Response.Listener<String>() {
                   @Override
                   public void onResponse(String response) {
                       ld.finishLoader(Login.this);
                       pref.edit().putBoolean("admin", true).commit();
                       startActivity(new Intent(Login.this, Home.class));
                   }
               }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {
               ld.finishLoader(Login.this);
               pref.edit().putBoolean("admin", false).commit();
               startActivity(new Intent(Login.this, Home.class));
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

   }

    @Override
    public void onBackPressed() {
        //DISABLE BACK BUTTON
    }

}

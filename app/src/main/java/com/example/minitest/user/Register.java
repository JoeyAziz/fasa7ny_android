package com.example.minitest.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.minitest.main.Loading_Screen;
import com.example.minitest.main.network;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class Register extends AppCompatActivity {
    //Layout components
    Button register_btn;
    EditText first_name;
    EditText last_name;
    EditText email;
    EditText pass;
    TextView goto_login;


    //variables
    String requestBody;

    //Pause screen
    Loading_Screen ld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Fields
        first_name = (EditText) findViewById((R.id.fname_field));
        last_name = (EditText) findViewById((R.id.lname_field));
        email = (EditText) findViewById((R.id.email_field));
        pass = (EditText) findViewById((R.id.pass_field));
        goto_login = (TextView) findViewById((R.id.goto_login)) ;

        //loading screen
        ld = new Loading_Screen();

        //REGISTER BUTTON
        register_btn = (Button) findViewById(R.id.register_button);
        register_btn.setOnClickListener(new View.OnClickListener() {//ON CLICK
            @Override
            public void onClick(View view) {
                String f,l,e,p;
                f = first_name.getText().toString();
                l = last_name.getText().toString();
                e = email.getText().toString();
                p = pass.getText().toString();

                if(f.isEmpty() || l.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Your NAME is missing!",Toast.LENGTH_LONG).show();
                }
                else if(e.length() < 4 || e.contains("!") || e.contains("@") || e.contains("#") || e.contains("$") || e.contains("%")
                        || e.contains("^")|| e.contains("&")|| e.contains("*")|| e.contains("(")|| e.contains(")")|| e.contains("-")|| e.contains("=")){
                    Toast.makeText(getApplicationContext(),"change your USERNAME!",Toast.LENGTH_LONG).show();
                }
                else if(p.length() < 4 || p.length() > 20){
                    Toast.makeText(getApplicationContext(),"change your PASSWORD",Toast.LENGTH_LONG).show();
                }
                else {
                    ld.startLoader(Register.this);
                    registerUser();
                }
            }
        });

        //GOTO LOGIN
        goto_login = findViewById(R.id.goto_login);
        goto_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this, Login.class) );
            }
        });
    }
    private void registerUser(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = network.getBackend_ip() +"register/";
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("email", email.getText().toString());
            jsonBody.put("pass", pass.getText().toString());
            jsonBody.put("fname", first_name.getText().toString());
            jsonBody.put("lname", last_name.getText().toString());
            requestBody = jsonBody.toString();

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getApplicationContext(),"Registered!!",Toast.LENGTH_LONG).show();
                            ld.finishLoader(Register.this);
                            startActivity(new Intent(Register.this, Login.class) );
                        }
                    }, new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    ld.finishLoader(Register.this);
                    Toast.makeText(getApplicationContext(),"Email already registered",Toast.LENGTH_LONG).show();
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

    @Override
    public void onBackPressed() {
        //DISABLE BACK BUTTON
    }
}

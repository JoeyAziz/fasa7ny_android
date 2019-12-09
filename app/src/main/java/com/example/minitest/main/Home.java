package com.example.minitest.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.minitest.R;
import com.example.minitest.fasahny_functions.Filter_Choices;
import com.example.minitest.user.Profile;

public class Home extends AppCompatActivity {
    //layout components
    Button profile_button;
    Button admin_button;
    Button fasahny_button;

    //shared pref.
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //get hared perf. info
        pref = getSharedPreferences("user_details",MODE_PRIVATE);

        //Profile BUTTON
        profile_button = (Button) findViewById(R.id.profile_button);
        profile_button.setOnClickListener(new View.OnClickListener() {//ON CLICK
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, Profile.class) );
            }
        });

        //ADMIN BUTTON
        admin_button = (Button) findViewById(R.id.admin_button);

        if(!pref.getBoolean("admin", false)){
            admin_button.setVisibility(View.GONE);
        }else admin_button.setVisibility(View.VISIBLE);

        admin_button.setOnClickListener(new View.OnClickListener() {//ON CLICK
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, AdminHome.class) );
            }
        });

        //FASAHNY BUTTON
        fasahny_button = (Button) findViewById(R.id.fas7ny_button);
        fasahny_button.setOnClickListener(new View.OnClickListener() {//ON CLICK
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, Filter_Choices.class) );
            }
        });
    }

    @Override
    public void onBackPressed() {
        //DISABLE BACK BUTTON
    }

}

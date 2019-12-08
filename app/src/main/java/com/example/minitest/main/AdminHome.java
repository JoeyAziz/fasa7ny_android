package com.example.minitest.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.minitest.R;
import com.example.minitest.admin_functions.Add_Location;
import com.example.minitest.admin_functions.Create_Area;
import com.example.minitest.admin_functions.Delete_Area;
import com.example.minitest.admin_functions.Remove_Location;
import com.example.minitest.admin_functions.Set_Admin;
import com.example.minitest.user.Login;

public class AdminHome extends AppCompatActivity {
    Button add_loc_button;
    Button delete_loc_button;
    Button set_admin_button;
    Button create_area_button;
    Button delete_area_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //GOTO ADD_LOCATION BUTTON
        add_loc_button = (Button) findViewById(R.id.add_loc_button);
        add_loc_button.setOnClickListener(new View.OnClickListener() {//ON CLICK
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminHome.this, Add_Location.class) );
            }
        });

        //GOTO DELETE_LOCATION BUTTON
        delete_loc_button = (Button) findViewById(R.id.delete_loc_button);
        delete_loc_button.setOnClickListener(new View.OnClickListener() {//ON CLICK
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminHome.this, Remove_Location.class) );
            }

        });

        //GOTO SET_ADMIN BUTTON
        set_admin_button = (Button) findViewById(R.id.set_admin_button);
        set_admin_button.setOnClickListener(new View.OnClickListener() {//ON CLICK
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminHome.this, Set_Admin.class) );
            }

        });

        //GOTO CREATE_AREA BUTTON
        create_area_button = (Button) findViewById(R.id.create_area_button);
        create_area_button.setOnClickListener(new View.OnClickListener() {//ON CLICK
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminHome.this, Create_Area.class) );
            }

        });

        //GOTO DELETE_AREA BUTTON
        delete_area_button = (Button) findViewById(R.id.delete_area_button);
        delete_area_button.setOnClickListener(new View.OnClickListener() {//ON CLICK
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminHome.this, Delete_Area.class) );
            }

        });
    }
}

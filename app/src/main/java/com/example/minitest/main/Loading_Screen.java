package com.example.minitest.main;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.minitest.R;
import com.example.minitest.user.Login;

public class Loading_Screen extends AppCompatActivity {

    Intent intent;
    static Activity act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading__screen);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        act = this;
    }

    @Override
    public void onBackPressed() {
        //DISABLE BACK BUTTON
    }

    public void startLoader(Activity activity){
        intent = new Intent(activity, Loading_Screen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        activity.startActivity(intent);
    }

    public void finishLoader(Activity activity){
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        Loading_Screen.act.finish();
    }
}

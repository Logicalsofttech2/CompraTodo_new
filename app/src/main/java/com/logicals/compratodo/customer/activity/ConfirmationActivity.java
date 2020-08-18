package com.logicals.compratodo.customer.activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.logicals.compratodo.R;
import com.logicals.compratodo.customer.mainhomeactivity.UserHomeActivity;
import com.logicals.compratodo.preferences.Session;

public class ConfirmationActivity extends AppCompatActivity {
    private static int SPLASH_SCREEN_TIME_OUT = 2000;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
             startActivity(new Intent(ConfirmationActivity.this, UserHomeActivity.class));
             finish();
            }

        }, SPLASH_SCREEN_TIME_OUT);




    }







}
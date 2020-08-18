package com.logicals.compratodo.login_signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.logicals.compratodo.R;
import com.logicals.compratodo.customer.mainhomeactivity.UserHomeActivity;
import com.logicals.compratodo.https.HttpsRequest;
import com.logicals.compratodo.interfacess.Consts;
import com.logicals.compratodo.interfacess.Helper;
import com.logicals.compratodo.model.UserDTO;
import com.logicals.compratodo.preferences.Session;
import com.logicals.compratodo.preferences.SharedPrefrence;
import com.logicals.compratodo.vendor.activity.CraeteCatlogActivity;
import com.logicals.compratodo.vendor.activity.VendorHome;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;

public class splashActivity extends AppCompatActivity {
    private static int SPLASH_SCREEN_TIME_OUT = 2000;
    ImageView imageView;
    SharedPrefrence sharedPrefrence;
    Context mContext;
    UserDTO userDTO;
    HashMap<String,String > param=new HashMap<>();
    private Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);


        session = new Session(this);


        displayFirebaseRegId();



        mContext=splashActivity.this;
        String languagename = Locale.getDefault().getDisplayLanguage();
        String country = Locale.getDefault().getCountry();
        sharedPrefrence= SharedPrefrence.getInstance(splashActivity.this);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(sharedPrefrence.isLoggedIn()){
                  userDTO=sharedPrefrence.getParentUser(Consts.USER_DTO);
                    if (userDTO.getRole().equalsIgnoreCase("1")) {
                        Intent in = new Intent(mContext, UserHomeActivity.class);
                        in.putExtra(Consts.USER_ID, userDTO.getId());
                        in.putExtra(Consts.EMAIL, userDTO.getEmail());
                        startActivity(in);
                        finish();
                    } else {
                        Check_CreateCatlogApi(); }
                }else{
                    if (sharedPrefrence.getchooseRole("role")==1){
                        startActivity(new Intent(splashActivity.this, UserHomeActivity.class));
                        finish();
                    }else if (sharedPrefrence.getchooseRole("role")==2){
                        startActivity(new Intent(splashActivity.this, VendorLoginActivity.class));
                        finish();
                    }else {
                        Intent i = new Intent(splashActivity.this, ChooseRoleActivity.class);
                          i.putExtra("intent","home");
                        startActivity(i);
                        finish();

                    }


                }
            }

        }, SPLASH_SCREEN_TIME_OUT);
    }

    private void Check_CreateCatlogApi() {
           // ProjectUtils.showProgressDialog(mContext, true, getResources().getString(R.string.please_wait));
            param.put(Consts.USER_ID, userDTO.getId());

            new HttpsRequest(Consts.CHECK_CATLOG_STATUS, param, mContext).stringPost("TAG", new Helper() {
                @Override
                public void backResponse(boolean flag, String msg, JSONObject response) {

                  //  ProjectUtils.pauseProgressDialog();

                    try {
                        String statusString = response.getString("status");
                        if (flag) {
                            Intent in = new Intent(mContext, VendorHome.class);
                            in.putExtra(Consts.USER_ID, userDTO.getId());
                            in.putExtra(Consts.EMAIL, userDTO.getEmail());
                            startActivity(in);
                            finish();

                        } else {

                           Intent i = new Intent(splashActivity.this, CraeteCatlogActivity.class);
                           startActivity(i);
                           finish();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });
        }




    private void displayFirebaseRegId() {

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String token = instanceIdResult.getToken();
                // send it to server
                session.saveToken(token);
                Log.e("refresh_tokentoken", token);
                Log.e("save_token", session.getTokenId());
            }
        });


    }










}
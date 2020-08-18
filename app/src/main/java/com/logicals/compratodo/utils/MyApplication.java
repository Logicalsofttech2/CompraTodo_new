package com.logicals.compratodo.utils;


import android.annotation.SuppressLint;
import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;
import com.google.firebase.iid.FirebaseInstanceId;
import com.logicals.compratodo.interfacess.Consts;
import com.logicals.compratodo.preferences.SharedPrefrence;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MyApplication extends Application {



    SharedPrefrence sharedPrefrence;
    @SuppressLint("LogNotTimber")
    @Override
    public void onCreate() {
        super.onCreate();

        printHashKey();


        sharedPrefrence =SharedPrefrence.getInstance(getApplicationContext());

     /*   Log.e("getToken", "Token: "
                + FirebaseInstanceId.getInstance().getToken());
*/
        FirebaseInstanceId.getInstance().getToken();

        sharedPrefrence.setValue(Consts.FIREBASETOKEN,FirebaseInstanceId.getInstance().getToken());



    }


    @SuppressLint("LogNotTimber")
    public void printHashKey(){

        // Add code to print out the key hash
        try {
            @SuppressLint("PackageManagerGetSignatures")
            PackageInfo info = getPackageManager().getPackageInfo(
                    Consts.APP_PATH,
                    PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }

        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
//            Log.e(, "printHashKey: " );
        }
    }

}

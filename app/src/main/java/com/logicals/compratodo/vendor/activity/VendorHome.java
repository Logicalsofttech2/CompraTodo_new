package com.logicals.compratodo.vendor.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.logicals.compratodo.R;
import com.logicals.compratodo.vendor.interfaces.CardIds;

import java.util.ArrayList;

import static androidx.navigation.Navigation.findNavController;

public class VendorHome extends AppCompatActivity implements CardIds {
    public static NavController navController;
    boolean doubleBackToExitPressedOnce = false;
    public static ArrayList<String> getProductId=new ArrayList<>();


            @Override
            protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_vendor_home);
            BottomNavigationView navView = findViewById(R.id.nav_view);
            AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home, R.id.navigation_vendorProfile, R.id.navigation_notifications, R.id.navigation_save).build();
            navController = findNavController(this, R.id.nav_host_fragment);
            NavigationUI.setupWithNavController(navView, navController);
        }

/*     new AlertDialog.Builder(VendorHome.this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Closing Application")
                    .setMessage("Are you sure you want to close this App?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (Build.VERSION.SDK_INT >= 16 && Build.VERSION.SDK_INT < 21) {
                                // getActivity().finishAffinity();
                                Intent i=new Intent(Intent.ACTION_MAIN);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                finish();
                            } else if (Build.VERSION.SDK_INT >= 21) {
                                Intent i=new Intent(Intent.ACTION_MAIN);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                finish();
                                //  getActivity().finishAndRemoveTask();
                            }
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();*//*

            Log.d("TAG", "SettingsFragment not found in backStack, navigate manually");

        }

    }


*/











    @Override
    public void onBackPressed() {
        if (navController.popBackStack(R.id.navigation_home, true)) {
            Log.d("TAG", "SettingsFragment found in backStack");
            super.onBackPressed();
        } else {
            if (doubleBackToExitPressedOnce) {

                new AlertDialog.Builder(VendorHome.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Closing Application")
                        .setMessage("Are you sure you want to close this App?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (Build.VERSION.SDK_INT >= 16 && Build.VERSION.SDK_INT < 21) {
                                    // getActivity().finishAffinity();
                                  /*Intent i=new Intent(Intent.ACTION_MAIN);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    finish();*/
                                    onBackPressed();
                                    System.exit(0);
                                    return;
                                } else if (Build.VERSION.SDK_INT >= 21) {
                                   /* Intent i=new Intent(Intent.ACTION_MAIN);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    finish();
                                    */
                                    onBackPressed();
                                    System.exit(0);
                                    return;
                                    //  getActivity().finishAndRemoveTask();
                                }
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
            }
            this.doubleBackToExitPressedOnce = true;
            navController.navigate(R.id.navigation_home);
        }
    }

    @Override
    public void getCardids(ArrayList<String> values) {




        getProductId=values;
        Log.e("values",getProductId.toString());



    }



}





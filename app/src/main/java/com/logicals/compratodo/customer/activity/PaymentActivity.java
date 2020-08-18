package com.logicals.compratodo.customer.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.view.View;
import android.widget.Toast;

import com.logicals.compratodo.R;
import com.logicals.compratodo.databinding.ActivityPaymentBinding;
import com.logicals.compratodo.https.HttpsRequest;
import com.logicals.compratodo.interfacess.Consts;
import com.logicals.compratodo.interfacess.Helper;
import com.logicals.compratodo.model.UserDTO;
import com.logicals.compratodo.preferences.SharedPrefrence;
import com.logicals.compratodo.preferences.Shared_Pref;
import com.logicals.compratodo.utils.GPSTracker;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class PaymentActivity extends AppCompatActivity {
    ActivityPaymentBinding binding;
    Context mContext;
    String product_id;
    SharedPrefrence sharedPrefrence;
    UserDTO userDTO;
    String address;
    String final_amount;
    HashMap<String, String> param = new HashMap<>();
    GPSTracker gps;
    double Lattitude,Longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment);
        mContext = PaymentActivity.this;
        product_id = Shared_Pref.getProductId(mContext);
        sharedPrefrence = SharedPrefrence.getInstance(mContext);
        userDTO = sharedPrefrence.getParentUser(Consts.USER_DTO);
        try {
            getLocation();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (getIntent() != null) {
            final_amount = getIntent().getStringExtra(Consts.FINAL_AMOUNT);
            address = getIntent().getStringExtra(Consts.ADDRESS);
            binding.finalAmount.setText("USD " + final_amount);
        }


        binding.LinCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               place_order_api(Consts.CASH);

             //   Toast.makeText(mContext, ""+product_id+" "+userDTO.getId()+" "+address+" "+"paymentmode"+" "+final_amount+" "+"1", Toast.LENGTH_SHORT).show();


            }
        });


        binding.LinOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                place_order_api(Consts.Online);
            }
        });

    }

    private void place_order_api(String paymentmode) {
        param.put(Consts.PRODUCT_ID, product_id);
        param.put(Consts.USER_ID, userDTO.getId());
        param.put(Consts.ADDRESS_ID, address);
        param.put(Consts.Payment_Method, paymentmode);
        param.put(Consts.TOTALAMOUNT, final_amount);
        param.put(Consts.LAT, String.valueOf(Lattitude));
        param.put(Consts.LAG, String.valueOf(Longitude));

        new HttpsRequest(Consts.PLACEORDER, param, mContext).stringPost("TAG", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        startActivity(new Intent(PaymentActivity.this, ConfirmationActivity.class));
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void getLocation() throws IOException {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            gps = new GPSTracker(mContext);
            if (gps.canGetLocation()) {
                Lattitude = gps.getLatitude();
                Longitude = gps.getLongitude();
                /*Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(this, Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(Lattitude, Longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName();

                }catch (Exception e){
                }*/
            } else {
                gps.showSettingsAlert();
            }

        }
    }

}
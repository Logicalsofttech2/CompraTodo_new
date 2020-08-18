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
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.logicals.compratodo.R;
import com.logicals.compratodo.databinding.ActivityREPaymentBinding;
import com.logicals.compratodo.databinding.ActivitySnglPdPaymentBinding;
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

public class SnglPdPaymentActivity extends AppCompatActivity {

    ActivitySnglPdPaymentBinding binding;
    Context mContext;
    String product_id;
    SharedPrefrence sharedPrefrence;
    UserDTO userDTO;
    String address,order_id;
    String final_amount;
    HashMap<String, String> param = new HashMap<>();
    double Lattitude,Longitude;
    GPSTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_sngl_pd_payment);


        binding = DataBindingUtil.setContentView(this, R.layout.activity_sngl_pd_payment);
        mContext = SnglPdPaymentActivity.this;
        product_id = Shared_Pref.getProductId(mContext);
        sharedPrefrence = SharedPrefrence.getInstance(mContext);
        userDTO = sharedPrefrence.getParentUser(Consts.USER_DTO);

        if (getIntent() != null) {
            final_amount = getIntent().getStringExtra(Consts.FINAL_AMOUNT);
            address = getIntent().getStringExtra(Consts.ADDRESS);
            binding.finalAmount.setText("USD " + final_amount);
            order_id = getIntent().getStringExtra(Consts.ORDER_ID);
        }

        try {
            getLocation();
        } catch (IOException e) {
            e.printStackTrace();
        }
        binding.LinCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                place_order_api(Consts.CASH);
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
        param.put(Consts.QUANTITY, "1");
        param.put(Consts.LAT, String.valueOf(Lattitude));
        param.put(Consts.LAG, String.valueOf(Longitude));


        new HttpsRequest(Consts.PLACESINGLEpRODUCT, param, mContext).stringPost("TAG", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        startActivity(new Intent(SnglPdPaymentActivity.this, ConfirmationActivity.class));
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
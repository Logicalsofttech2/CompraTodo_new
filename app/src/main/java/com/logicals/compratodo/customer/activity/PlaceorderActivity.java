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
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.logicals.compratodo.R;
import com.logicals.compratodo.customer.model.BillingDTO;
import com.logicals.compratodo.databinding.ActivityPlaceorderBinding;
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

public class PlaceorderActivity extends AppCompatActivity implements OnMapReadyCallback {
    GPSTracker gps;
    Context mContext;
    double Lattitude,Longitude;
    ActivityPlaceorderBinding binding;
    SupportMapFragment mapFragment;
    Circle circle;
    String address,product_id,final_amount;
    HashMap<String, String> param = new HashMap<>();
    SharedPrefrence sharedPrefrence;
    UserDTO userDTO;
    String check_api, morder_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_placeorder);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.frg);
        mContext=PlaceorderActivity.this;
        mapFragment.getMapAsync(this);
        sharedPrefrence = SharedPrefrence.getInstance(PlaceorderActivity.this);
        userDTO = sharedPrefrence.getParentUser(Consts.USER_DTO);
        String mAddress=Shared_Pref.getAddresId(mContext);
        binding.tvAddressSelect.setText(mAddress);
        product_id= Shared_Pref.getProductId(mContext);

        if (getIntent()!=null){

            check_api=getIntent().getStringExtra(Consts.BILLING);
            morder_id=getIntent().getStringExtra(Consts.ORDER_ID);

            if (check_api.equals(Consts.ADDTOCART)){
                getBilling(product_id,Consts.ADDTOCARTBILLING);
            }
            else if (check_api.equals(Consts.REBELLING)){
                getREBilling(morder_id,Consts.RE_BILLING);
            }
            else if (check_api.equals(Consts.PRODUCTBILLING)){
                getBilling(product_id,Consts.BILLINGPRODUCT);
            }






        }







       binding.tvSelectAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlaceorderActivity.this, CurrentaddressActivity.class);
                intent.putExtra("lat",Lattitude);
                intent.putExtra("lng",Longitude);
                intent.putExtra(Consts.BILLING,check_api);
                startActivity(intent);
            }
        });




        try {
            getLocation();
        }
        catch (IOException e) {
            e.printStackTrace();
        }



        binding.tvPayBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (check_api.equals(Consts.REBELLING)){
                    Intent intent = new Intent(PlaceorderActivity.this, REPaymentActivity.class);
                    intent.putExtra(Consts.FINAL_AMOUNT,final_amount) ;
                    intent.putExtra(Consts.ORDER_ID,morder_id);
                    intent.putExtra(Consts.ADDRESS,Shared_Pref.getAddresId(mContext));
                    startActivity(intent);

                }else if (check_api.equals(Consts.PRODUCTBILLING)){

                    Intent intent = new Intent(PlaceorderActivity.this, SnglPdPaymentActivity.class);
                    intent.putExtra(Consts.FINAL_AMOUNT,final_amount) ;
                    intent.putExtra(Consts.ADDRESS,Shared_Pref.getAddresId(mContext));
                    startActivity(intent);

                }
                else if (check_api.equals(Consts.ADDTOCART)){

                    Intent intent = new Intent(PlaceorderActivity.this, PaymentActivity.class);
                    intent.putExtra(Consts.FINAL_AMOUNT,final_amount) ;
                    intent.putExtra(Consts.ADDRESS,Shared_Pref.getAddresId(mContext));
                    startActivity(intent);

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
                Geocoder geocoder;
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
                    Shared_Pref.setAddresId(mContext,address);
                }catch (Exception e){
                }
            } else {
                gps.showSettingsAlert();
            }

        }
    }


    public float getZoomLevel(Circle circle) {
        float zoomLevel = 0;
        if (circle != null) {
            double radius = circle.getRadius();
            double scale = radius / 150;
            zoomLevel = (int) (16 - Math.log(scale) / Math.log(2));
        }
        return zoomLevel + .5f;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.clear(); //clear old markers
        circle = googleMap.addCircle(new CircleOptions()
                .center(new LatLng(Lattitude, Longitude))
                .radius(2000) // Converting Miles into Meters...
                .strokeColor(Color.BLUE).fillColor(Color.parseColor("#500084d3"))
                .strokeWidth(5));
        circle.isVisible();
        float currentZoomLevel = getZoomLevel(circle);
        float animateZomm = currentZoomLevel + 2;
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Lattitude, Longitude), animateZomm));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(currentZoomLevel), 10, null);
        LatLng latLng = new LatLng(Lattitude, Longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(latLng.latitude + " : " + latLng.longitude);
        googleMap.addMarker(markerOptions);
    }

    private void getBilling(String product_id,String URl) {
        param.put(Consts.PRODUCT_ID, product_id);
        param.put(Consts.USER_ID, userDTO.getId());
        new HttpsRequest(URl, param, mContext).stringPost("TAG", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        BillingDTO billingDTO = new Gson().fromJson(response.getJSONObject("data").toString(), BillingDTO.class);
                        binding.setBillingdetails(billingDTO);
                        final_amount=billingDTO.getFinal_amount();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    private void getREBilling(String orderid,String URl) {
        param.put(Consts.ORDER_ID, orderid);
        param.put(Consts.USER_ID, userDTO.getId());
        new HttpsRequest(URl, param, mContext).stringPost("TAG", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        BillingDTO    billingDTO = new Gson().fromJson(response.getJSONObject("data").toString(), BillingDTO.class);
                        binding.setBillingdetails(billingDTO);
                        final_amount=billingDTO.getFinal_amount();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                }

            }
        });


    }









}
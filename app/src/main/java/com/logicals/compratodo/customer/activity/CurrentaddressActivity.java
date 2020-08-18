package com.logicals.compratodo.customer.activity;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.logicals.compratodo.R;
import com.logicals.compratodo.interfacess.Consts;

import java.util.List;
import java.util.Locale;

public class CurrentaddressActivity extends AppCompatActivity implements OnMapReadyCallback {
    SupportMapFragment mapFragment;
    Circle circle;
    ImageView imageView;
    double lat,lng;
    String check_api,address;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currentaddress);
        imageView=findViewById(R.id.iv_back);



        if (getIntent()!=null){
            lat=getIntent().getDoubleExtra("lat",0.0);
            lng=getIntent().getDoubleExtra("lng",0.0);
            check_api=getIntent().getStringExtra(Consts.BILLING);
        }

        findViewById(R.id.saveaddress).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CurrentaddressActivity.this, AddresslistActivity.class);
                intent.putExtra(Consts.BILLING,check_api);
                startActivity(intent);
            }
        });
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.frg);
        mapFragment.getMapAsync(this);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        findViewById(R.id.addaddress).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CurrentaddressActivity.this, AddAddressActivity.class);
                 intent.putExtra("Type","Add");
                intent.putExtra(Consts.BILLING,check_api);
                startActivity(intent);

            }
        });

        findViewById(R.id.placeorder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CurrentaddressActivity.this, PlaceorderActivity.class);
                intent.putExtra(Consts.ADDRESS,address);
                intent.putExtra(Consts.BILLING,check_api);
                startActivity(intent);

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
                .center(new LatLng(lat, lng))
                .radius(2000) // Converting Miles into Meters...
                .strokeColor(Color.BLUE).fillColor(Color.parseColor("#500084d3"))
                .strokeWidth(5));
        circle.isVisible();
        float currentZoomLevel = getZoomLevel(circle);
        float animateZomm = currentZoomLevel + 2;
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), animateZomm));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(currentZoomLevel), 10, null);
        LatLng latLng = new LatLng(lat, lng);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(latLng.latitude + " : " + latLng.longitude);
        googleMap.addMarker(markerOptions);

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(lat,lng,1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
             address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();




        }catch (Exception e){

        }



    }
}
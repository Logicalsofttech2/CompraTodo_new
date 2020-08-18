package com.logicals.compratodo.vendor.ui.profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.logicals.compratodo.R;
import com.logicals.compratodo.databinding.ActivityVendorprofiledetailsBinding;
import com.logicals.compratodo.interfacess.Consts;
import com.logicals.compratodo.model.UserDTO;
import com.logicals.compratodo.network.NetworkManager;
import com.logicals.compratodo.preferences.SharedPrefrence;
import com.logicals.compratodo.profile.UpdateProfile;
import com.logicals.compratodo.utils.ProjectUtils;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class Vendorprofiledetails extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {


    ActivityVendorprofiledetailsBinding binding;
    private static final int REQUEST_LOCATION = 0;
    private Location mCurrentLocation = new Location("test");
    Context mContext;
    Circle circle;
    private static GoogleMap mMap;
    Marker marker;
    private Handler handler = new Handler();
    Timer timer = new Timer();

    private static String TAG = Vendorprofiledetails.class.getSimpleName();
    HashMap<String, String> param = new HashMap<>();
    SharedPrefrence sharedPrefrence;
    UserDTO userDTO;

    private int markerCount;
    private GoogleApiClient mGoogleApiClient;
    Double lat,lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_vendorprofiledetails);
        mContext = Vendorprofiledetails.this;
        sharedPrefrence = SharedPrefrence.getInstance(mContext);
        userDTO=sharedPrefrence.getParentUser(Consts.USER_DTO);
        setdata();


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        binding.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in =new Intent(mContext, UpdateProfile.class);
                in.putExtra(Consts.SCREEN_TAG,"2");
                startActivity(in);
            }
        });




     /*    mapFragment = SupportMapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map_frame, mapFragment);
        fragmentTransaction.commit();

         mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (mapFragment != null) {
//            binding.mapView.getMapAsync(this);
            mapFragment.getMapAsync(this::onMapReady);



        }*/
    }




    private void setdata() {
        binding.tvName.setText(userDTO.getName());
        if (userDTO.getRUC().equalsIgnoreCase("")) {
            binding.tvRUC.setText(getResources().getString(R.string.rucnumber) + " " + getResources().getString(R.string.not_available));
        } else {
            binding.tvRUC.setText(getResources().getString(R.string.rucnumber) + " " + userDTO.getRUC());
        }
        if (userDTO.getRUC().equalsIgnoreCase("")) {
            binding.tvDNI.setText(getResources().getString(R.string.dninumber) + " " + getResources().getString(R.string.not_available));
        } else {
            binding.tvDNI.setText(getResources().getString(R.string.dninumber) + " " + userDTO.getUsername());
        }
        binding.tvAccount.setText(getResources().getString(R.string.account_num) + " " + userDTO.getAccount_no());
        binding.tvPhone.setText(getResources().getString(R.string.phone_no) + " " + userDTO.getMobile());

        if (userDTO.getRole().equalsIgnoreCase("2")) {

            binding.tvAccountType.setText(getResources().getString(R.string.account_type) + " " + getResources().getString(R.string.retail));
        } else if (userDTO.getRole().equalsIgnoreCase("3")) {

            binding.tvAccountType.setText(getResources().getString(R.string.account_type) + " " + getResources().getString(R.string.wholesale));
        } else if (userDTO.getRole().equalsIgnoreCase("4")) {

            binding.tvAccountType.setText(getResources().getString(R.string.account_type) + " " + getResources().getString(R.string.farmer));
        }

        Glide.with(mContext)
                .load(Consts.USER_IMAGE_URL + userDTO.getImage())
                .error(R.drawable.logo)
                .into(binding.civProfilePic);
        if (userDTO.getHealth_certificate().equalsIgnoreCase("")) {
            binding.llHealthCertificate.setVisibility(View.GONE);
        } else {
            binding.llHealthCertificate.setVisibility(View.VISIBLE);

            Glide.with(mContext)
                    .load( Consts.USER_IMAGE_URL +userDTO.getHealth_certificate())
                    .error(R.drawable.logo)
                    .into(binding.ivHealthCertificate);
        }

        if (userDTO.getPermit().equalsIgnoreCase("")) {
            binding.llPermit.setVisibility(View.GONE);
        } else {
            binding.llPermit.setVisibility(View.VISIBLE);

            Glide.with(mContext)
                    .load( Consts.USER_IMAGE_URL +userDTO.getPermit())
                    .error(R.drawable.logo)
                    .into(binding.ivPermit);
        }


/*
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                mMap = mMap;
                if (mMap != null) {


                    lat= Double.valueOf(userDTO.getLat());
                    lng= Double.valueOf(userDTO.getLang());
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng),18);
                    mMap.animateCamera(cameraUpdate);
                    marker = mMap.addMarker(new MarkerOptions().
                            position(new LatLng(lat, lng)).title("My Location"));
                    //marker1 = mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble("22.752204613704894"), Double.parseDouble("75.89695639908314"))).title(""));



                    circle = mMap.addCircle(new CircleOptions()
                            .center(new LatLng(Double.valueOf(userDTO.getLat()),Double.valueOf(userDTO.getLang())))
                            .radius(2000) // Converting Miles into Meters...
                            .strokeColor(Color.BLUE).fillColor(Color.parseColor("#500084d3"))
                            .strokeWidth(5));
                    circle.isVisible();
                    float currentZoomLevel = getZoomLevel(circle);
                    float animateZomm = currentZoomLevel + 2;

                    Log.e("Zoom Level:", currentZoomLevel + "");
                    Log.e("Zoom Level Animate:", animateZomm + "");

                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), animateZomm));

                    mMap.animateCamera(CameraUpdateFactory.zoomTo(currentZoomLevel), 14, null);

                    Log.e("Circle Lat Long:", userDTO.getLat() + ", " + userDTO.getLang());


                }
                if (mMap != null) {
                    // ProjectUtils.showProgressDialog(mContext, true, "Please wait while updating location.").show();
                    handler.postDelayed(mTask, 4000);
                }



            }
        });*/


    }


    Runnable mTask = new Runnable() {
        @Override
        public void run() {


            ProjectUtils.pauseProgressDialog();
            if (mGoogleApiClient.isConnected()) {
                //  startLocationUpdates();
                if (NetworkManager.isConnectToInternet(mContext)) {
                    displayLocation();
                } else {
                }
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        if (NetworkManager.isConnectToInternet(mContext)) {
                            displayLocation();
                        }
                    }
                }, 0, 4000);
            } else {

            }
        }
    };



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

        mMap = googleMap;
        if (mMap != null) {


            lat= Double.valueOf(userDTO.getLat());
            lng= Double.valueOf(userDTO.getLang());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng),18);
            mMap.animateCamera(cameraUpdate);
            marker = mMap.addMarker(new MarkerOptions().
                    position(new LatLng(lat, lng)).title("My Location"));
            //marker1 = mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble("22.752204613704894"), Double.parseDouble("75.89695639908314"))).title(""));



            circle = mMap.addCircle(new CircleOptions()
                    .center(new LatLng(Double.valueOf(userDTO.getLat()),Double.valueOf(userDTO.getLang())))
                    .radius(2000) // Converting Miles into Meters...
                    .strokeColor(Color.BLUE).fillColor(Color.parseColor("#500084d3"))
                    .strokeWidth(5));
            circle.isVisible();
            float currentZoomLevel = getZoomLevel(circle);
            float animateZomm = currentZoomLevel + 2;

            Log.e("Zoom Level:", currentZoomLevel + "");
            Log.e("Zoom Level Animate:", animateZomm + "");

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), animateZomm));

            mMap.animateCamera(CameraUpdateFactory.zoomTo(currentZoomLevel), 14, null);

            Log.e("Circle Lat Long:", userDTO.getLat() + ", " + userDTO.getLang());


        }
        if (mMap != null) {
            // ProjectUtils.showProgressDialog(mContext, true, "Please wait while updating location.").show();
            handler.postDelayed(mTask, 4000);
        }



    }



    @Override
    protected void onResume() {
        super.onResume();
//        mapFragment.onResume();
//        binding.mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (this.mGoogleApiClient != null) {
            this.mGoogleApiClient.connect();
        }
//        mapFragment.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        mapFragment.onStop();
    }
    @Override
    protected void onPause() {
//        mapFragment.onPause();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
//        mapFragment.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
//        mapFragment.onLowMemory();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }



    //Method to display the location on UI
    private void displayLocation() {
        try {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                // Check Permissions Now
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION);

            } else {

                mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                if (mCurrentLocation != null) {

                    lat= Double.valueOf(userDTO.getLat());
                    lng= Double.valueOf(userDTO.getLang());


                    mCurrentLocation.setLatitude(lat);
                    mCurrentLocation.setLongitude(lng);

                    addMarker(mMap, lat, lng);

                } else {
                    Toast.makeText(mContext, getResources().getString(R.string.location_enabled),
                            Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {

        }
    }

    // Add A Map Pointer To The MAp
    public void addMarker(GoogleMap googleMap, double lat, double lon) {

        if (markerCount == 1) {
            Log.e("lat", mCurrentLocation.getLatitude() + "");
            Log.e("lat", mCurrentLocation.getLongitude() + "");



        } else if (markerCount == 0) {
            mMap = googleMap;
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 18);
            mMap.animateCamera(cameraUpdate);



            circle = mMap.addCircle(new CircleOptions()
                    .center(new LatLng(Double.valueOf(userDTO.getLat()),Double.valueOf(userDTO.getLang())))
                    .radius(2000) // Converting Miles into Meters...
                    .strokeColor(Color.BLUE).fillColor(Color.parseColor("#500084d3"))
                    .strokeWidth(5));
            circle.isVisible();
            float currentZoomLevel = getZoomLevel(circle);
            float animateZomm = currentZoomLevel + 2;

            Log.e("Zoom Level:", currentZoomLevel + "");
            Log.e("Zoom Level Animate:", animateZomm + "");

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), animateZomm));

            mMap.animateCamera(CameraUpdateFactory.zoomTo(currentZoomLevel), 14, null);

            Log.e("Circle Lat Long:", userDTO.getLat() + ", " + userDTO.getLang());


//            marker1 = mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(locationDTO.getC_lati()), Double.parseDouble(locationDTO.getC_longi()))).title(getResources().getString(R.string.joblocation)));


            markerCount = 1;
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                return;
            }
            startLocationUpdates();
        }
    }

    protected void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Check Permissions Now
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        }
    }



}
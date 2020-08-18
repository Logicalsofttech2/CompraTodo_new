package com.logicals.compratodo.vendor.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.logicals.compratodo.customer.activity.CatlogActivity;
import com.logicals.compratodo.utils.GPSTracker;
import com.logicals.compratodo.R;
import com.logicals.compratodo.databinding.ActivityCraeteCatlogBinding;
import com.logicals.compratodo.https.HttpsRequest;
import com.logicals.compratodo.interfacess.Consts;
import com.logicals.compratodo.interfacess.Helper;
import com.logicals.compratodo.model.MainCategoryDTO;
import com.logicals.compratodo.model.UserDTO;
import com.logicals.compratodo.preferences.SharedPrefrence;
import com.logicals.compratodo.ui.adapter.CountryCode;
import com.logicals.compratodo.utils.ProjectUtils;
import com.logicals.compratodo.vendor.ui.uploadproduct.UploadProductActivity;
import com.schibstedspain.leku.LocationPickerActivity;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.schibstedspain.leku.LocationPickerActivityKt.ADDRESS;
import static com.schibstedspain.leku.LocationPickerActivityKt.LATITUDE;
import static com.schibstedspain.leku.LocationPickerActivityKt.LOCATION_ADDRESS;
import static com.schibstedspain.leku.LocationPickerActivityKt.LONGITUDE;
import static com.schibstedspain.leku.LocationPickerActivityKt.TIME_ZONE_DISPLAY_NAME;
import static com.schibstedspain.leku.LocationPickerActivityKt.TIME_ZONE_ID;
import static com.schibstedspain.leku.LocationPickerActivityKt.TRANSITION_BUNDLE;
import static com.schibstedspain.leku.LocationPickerActivityKt.ZIPCODE;

public class CraeteCatlogActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {
    Context mContext;
    Circle circle;
    GPSTracker gps;
    String gps_address;
    private static GoogleMap mMap;
    Marker marker;
    ArrayList<MainCategoryDTO> mainCategoryDTOS = new ArrayList<>();
    private static final int REQUEST_LOCATION = 1;
    private static String TAG = CatlogActivity.class.getSimpleName();
    HashMap<String, String> param = new HashMap<>();
    HashMap<String, String> parampro = new HashMap<>();
    SharedPrefrence sharedPrefrence;
    UserDTO userDTO;
    int MAP_BUTTON_REQUEST_CODE = 100;
    private GoogleApiClient mGoogleApiClient;
    double lat, lng;
    ActivityCraeteCatlogBinding binding;
    CountryCode adapter;
    LocationManager locationManager;
    MainCategoryDTO mainCategoryDTO;
    double latitude,longitude;
    SupportMapFragment mapFragment;
    String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_craete_catlog);
        mContext = CraeteCatlogActivity.this;
        sharedPrefrence = SharedPrefrence.getInstance(mContext);
        userDTO = sharedPrefrence.getParentUser(Consts.USER_DTO);
        getLocation();
        getAddress();
        setdata();
        getCategory();


        /*SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocataddConnectionCallbacks(this)
                .addOnConnectionFailedListener(thisionServices.API)
                .)
                .build();*/


        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
//AIzaSyDNGNXz6WK_QbPz4w2-AUrVzYBI_ASemj0
        binding.tvChangeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent locationPickerIntent = new LocationPickerActivity.Builder()
                        .withLocation(latitude, longitude)
                        .withGoogleTimeZoneEnabled()
                        .withGeolocApiKey("AIzaSyDNGNXz6WK_QbPz4w2-AUrVzYBI_ASemj0")
                        .withSearchZone("hi_IN")
                        // .withSearchZone(SearchZoneRect(LatLng(latitude, longitude), LatLng(latitude, longitude)))
                        // .withDefaultLocaleSearchZone()
                        .shouldReturnOkOnBackPressed()
                        // .withStreetHidden()
                        // .withCityHidden()
                        //  .withZipCodeHidden()
                        // .withSatelliteViewHidden
                        //.withGooglePlacesEnabled()
                        // .withVoiceSearchHidden()
                        //.withUnnamedRoadHidden()
                        .build(getApplicationContext());
                       /*.withLocation(latitude, longitude)
                         .withGeolocApiKey("AIzaSyDNGNXz6WK_QbPz4w2-AUrVzYBI_ASemj0")
                        .shouldReturnOkOnBackPressed()
                        .withStreetHidden()
                        .withCityHidden()
                        .withZipCodeHidden()
                        .withSatelliteViewHidden()
                        .withGooglePlacesEnabled()
                        .withGoogleTimeZoneEnabled()
                        .withVoiceSearchHidden()
                        .build(getApplicationContext());*/


                locationPickerIntent.putExtra("test", "this is a test");
                startActivityForResult(locationPickerIntent, MAP_BUTTON_REQUEST_CODE);
            }
        });


    }
    private void getCategory() {
        new HttpsRequest(Consts.MAIN_CATEGORY, mContext).stringGet(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        Type listType = new TypeToken<ArrayList<MainCategoryDTO>>() {}.getType();
                        mainCategoryDTOS = new Gson().fromJson(response.getJSONArray("data").toString(), listType);
                        adapter = new CountryCode(mContext, R.layout.food_type_list, mainCategoryDTOS);
                        adapter.setDropDownViewResource(R.layout.food_type_list);
                        binding.spinner2.setAdapter(adapter);
                        binding.spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
                                Log.e("TAG", "onItemSelected: " + position);
                                mainCategoryDTO = mainCategoryDTOS.get(position);
                                param.put(Consts.MAIN_CATE_ID,mainCategoryDTO.getId());
                                Log.e(TAG, "onItemSelected: " + mainCategoryDTO.getName());
                                binding.ivspin.setVisibility(View.GONE);
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parentView) {
                            }
                        });
                    } catch (Exception e) {
                    }
                } else {
                    ProjectUtils.showToast(mContext, msg);
                }
            }
        });
    }


    private void setdata() {


        binding.tvAddCatelog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               addCatelog();
            }
        });








    }

    private void addCatelog() {
        param.put(Consts.LAT, String.valueOf(latitude));
        param.put(Consts.LANG, String.valueOf(longitude));
        param.put(Consts.USER_ID,userDTO.getId());
        param.put(Consts.ADDRESS,gps_address);
        param.put(Consts.MAIN_CATE_ID,mainCategoryDTO.getId());
        new HttpsRequest(Consts.ADDCATLOG, param, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {

                    Intent in = new Intent(mContext, UploadProductActivity.class);
                    startActivity(in);
                    finish();

                } else {
                    ProjectUtils.showToast(mContext, msg);
                }
            }
        });
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
       /* if (mMap != null) {


          //  lat = Double.valueOf(userDTO.getLat());
           // lng = Double.valueOf(userDTO.getLang());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 18);
            mMap.animateCamera(cameraUpdate);
            marker = mMap.addMarker(new MarkerOptions().
                   position(new LatLng(lat, lng)).title("My Location"));
            //marker1 = mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble("22.752204613704894"), Double.parseDouble("75.89695639908314"))).title(""));

            circle = mMap.addCircle(new CircleOptions()
                    .center(new LatLng(latitude, longitude))
                    .radius(2000) // Converting Miles into Meters...
                    .strokeColor(Color.BLUE).fillColor(Color.parseColor("#500084d3"))
                    .strokeWidth(5));

            circle.isVisible();

            float currentZoomLevel = getZoomLevel(circle);
            float animateZomm = currentZoomLevel + 2;

            Log.e("Zoom Level:", currentZoomLevel + "");
            Log.e("Zoom Level Animate:", animateZomm + "");

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), animateZomm));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(currentZoomLevel), 14, null);
            Log.e("Circle Lat Long:", userDTO.getLat() + ", " + userDTO.getLang());




        }*/
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.clear(); //clear old markers
        circle = googleMap.addCircle(new CircleOptions()
                .center(new LatLng(latitude, longitude))
                .radius(2000) // Converting Miles into Meters...
                .strokeColor(Color.BLUE).fillColor(Color.parseColor("#500084d3"))
                .strokeWidth(5));
        circle.isVisible();
        float currentZoomLevel = getZoomLevel(circle);
        float animateZomm = currentZoomLevel + 2;
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), animateZomm));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(currentZoomLevel), 10, null);
        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(latLng.latitude + " : " + latLng.longitude);
        googleMap.addMarker(markerOptions);


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





    private void getLocation() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            gps = new GPSTracker(mContext);
            if (gps.canGetLocation()) {
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();
            } else {
                gps.showSettingsAlert();
            }
        }
    }







    @Override
    protected void onStart() {
        super.onStart();
        if (this.mGoogleApiClient != null) {
            this.mGoogleApiClient.connect();
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            Log.e("RESULT****", "OK"+requestCode);
            if (requestCode == 1) {
                String latitude = String.valueOf(data.getDoubleExtra(LATITUDE, 0.0));
                Log.e("LATITUDE****", latitude.toString());
                String longitude = String.valueOf(data.getDoubleExtra(LONGITUDE, 0.0));
                Log.e("LONGITUDE****", longitude.toString());
                String address = data.getStringExtra(LOCATION_ADDRESS);
                Log.e("ADDRESS****", address.toString());
                String postalcode = data.getStringExtra(ZIPCODE);
                Log.e("POSTALCODE****", postalcode.toString());
                String bundle = String.valueOf(data.getBundleExtra(TRANSITION_BUNDLE));
                Log.e("BUNDLE TEXT****", bundle);
                String fullAddress = data.getStringExtra(ADDRESS);
                if (fullAddress != null) {
                    Log.e("FULL ADDRESS****", fullAddress.toString());
                }
                String timeZoneId = data.getStringExtra(TIME_ZONE_ID);
                Log.e("TIME ZONE ID****", timeZoneId);
                String timeZoneDisplayName = data.getStringExtra(TIME_ZONE_DISPLAY_NAME);
                Log.e("TIME ZONE NAME****", timeZoneDisplayName);
            } else if (requestCode == 100) {
                latitude = data.getDoubleExtra(LATITUDE, 0.0);
                Log.e("LATITUDE****", String.valueOf(latitude));
                longitude = data.getDoubleExtra(LONGITUDE, 0.0);
                Log.e("LONGITUDE****", String.valueOf(longitude));
                address = data.getStringExtra(LOCATION_ADDRESS);
                binding.tvAddress.setText(address);
                Log.e("ADDRESS****", address.toString());
            }
        }
        if (resultCode == Activity.RESULT_CANCELED) {
            Log.e("RESULT****", "CANCELLED");
        }
    }

    public void getAddress(){
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
             gps_address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
            binding.tvAddress.setText(gps_address);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }









}
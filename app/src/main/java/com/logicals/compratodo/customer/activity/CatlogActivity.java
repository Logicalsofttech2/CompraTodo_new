package com.logicals.compratodo.customer.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.logicals.compratodo.R;
import com.logicals.compratodo.databinding.ActivityCatlogBinding;
import com.logicals.compratodo.https.HttpsRequest;
import com.logicals.compratodo.interfacess.Consts;
import com.logicals.compratodo.interfacess.Helper;
import com.logicals.compratodo.model.MainCategoryDTO;
import com.logicals.compratodo.model.UserDTO;
import com.logicals.compratodo.preferences.SharedPrefrence;
import com.logicals.compratodo.ui.adapter.CountryCode;
import com.logicals.compratodo.utils.ProjectUtils;
import com.logicals.compratodo.vendor.activity.VendorHome;
import com.schibstedspain.leku.LocationPickerActivity;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import static com.schibstedspain.leku.LocationPickerActivityKt.ADDRESS;
import static com.schibstedspain.leku.LocationPickerActivityKt.LATITUDE;
import static com.schibstedspain.leku.LocationPickerActivityKt.LOCATION_ADDRESS;
import static com.schibstedspain.leku.LocationPickerActivityKt.LONGITUDE;
import static com.schibstedspain.leku.LocationPickerActivityKt.TIME_ZONE_DISPLAY_NAME;
import static com.schibstedspain.leku.LocationPickerActivityKt.TIME_ZONE_ID;
import static com.schibstedspain.leku.LocationPickerActivityKt.TRANSITION_BUNDLE;
import static com.schibstedspain.leku.LocationPickerActivityKt.ZIPCODE;

public class CatlogActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    Context mContext;
    Circle circle;
    private static GoogleMap mMap;
    Marker marker;
    ArrayList<MainCategoryDTO> mainCategoryDTOS = new ArrayList<>();

    private static String TAG = CatlogActivity.class.getSimpleName();
    HashMap<String, String> param = new HashMap<>();
    HashMap<String, String> parampro = new HashMap<>();
    SharedPrefrence sharedPrefrence;
    UserDTO userDTO;
    int MAP_BUTTON_REQUEST_CODE = 100;
    private GoogleApiClient mGoogleApiClient;
    Double lat, lng;
    ActivityCatlogBinding binding;
    CountryCode adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_catlog);
        mContext = CatlogActivity.this;


        sharedPrefrence = SharedPrefrence.getInstance(mContext);
        userDTO = sharedPrefrence.getParentUser(Consts.USER_DTO);
        setdata();
        getCategory();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    private void getCategory() {

        new HttpsRequest(Consts.MAIN_CATEGORY, mContext).stringGet(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {

                    try {

                        Type listType =
                                new TypeToken<ArrayList<MainCategoryDTO>>() {
                                }.getType();
                        mainCategoryDTOS = new Gson().fromJson(response.getJSONArray("data").toString(), listType);

                        adapter = new CountryCode(mContext,
                                R.layout.food_type_list, mainCategoryDTOS);
                        adapter.setDropDownViewResource(R.layout.food_type_list);

                        binding.spinner2.setAdapter(adapter);
                        // Set adapter to spinner

                        // Listener called when spinner item selected
                        binding.spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
                                // your code here

                                // Get selected row data to show on screen
                                Log.e("TAG", "onItemSelected: " + position);

                                MainCategoryDTO mainCategoryDTO = mainCategoryDTOS.get(position);


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

        binding.tvAddress.setText(userDTO.getAddress());

        binding.tvChangeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent locationPickerIntent = new LocationPickerActivity.Builder()
                        .withLocation(Double.valueOf(userDTO.getLat()), Double.valueOf(userDTO.getLang()))
                        .withGeolocApiKey("AIzaSyCMFIxvPhSJ5xb9YIfH3mEF8KgsTWgYEuo")
                        .shouldReturnOkOnBackPressed()
                        .withStreetHidden()
                        .withCityHidden()
                        .withZipCodeHidden()
                        .withSatelliteViewHidden()
                        //.withGooglePlacesEnabled()
                        .withGoogleTimeZoneEnabled()
                        .withVoiceSearchHidden()
                        .build(getApplicationContext());

                startActivityForResult(locationPickerIntent, MAP_BUTTON_REQUEST_CODE);

            }
        });

        binding.tvAddCatelog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCatelog();

            }
        });

        binding.tvDeleteCatelog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCatelog();

            }
        });


    }






    private void addCatelog() {
        param.put(Consts.LAT,userDTO.getLat());
        param.put(Consts.LANG,userDTO.getLang());
        param.put(Consts.USER_ID,userDTO.getId());
        param.put(Consts.ADDRESS,userDTO.getAddress());




        new HttpsRequest(Consts.ADDCATLOG, param, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    getProfileData();
//                    Toast.makeText(mContext, userDTO.getId(), Toast.LENGTH_SHORT).show();
                } else {
                    ProjectUtils.showToast(mContext, msg);
                }
            }
        });
    }



    private void deleteCatelog() {
        param.put(Consts.USER_ID,userDTO.getId());
        new HttpsRequest(Consts.CATELOGDELETE, param, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    getProfileData();

                } else {
                    ProjectUtils.showToast(mContext, msg);
                }
            }
        });
    }

    private void getProfileData() {
        parampro.put(Consts.USER_ID,userDTO.getId());
        new HttpsRequest(Consts.GET_USER_PROFILE, parampro, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {

                    try {
                        userDTO = new Gson().fromJson(response.getJSONObject("data").toString(), UserDTO.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    sharedPrefrence.setParentUser(userDTO, Consts.USER_DTO);
                    Intent in = new Intent(mContext, VendorHome.class);
                    startActivity(in);
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
        if (mMap != null) {


            lat = Double.valueOf(userDTO.getLat());
            lng = Double.valueOf(userDTO.getLang());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 18);
            mMap.animateCamera(cameraUpdate);
            marker = mMap.addMarker(new MarkerOptions().
                    position(new LatLng(lat, lng)).title("My Location"));
            //marker1 = mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble("22.752204613704894"), Double.parseDouble("75.89695639908314"))).title(""));


            circle = mMap.addCircle(new CircleOptions()
                    .center(new LatLng(Double.valueOf(userDTO.getLat()), Double.valueOf(userDTO.getLang())))
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
    protected void onStart() {
        super.onStart();
        if (this.mGoogleApiClient != null) {
            this.mGoogleApiClient.connect();
        }
    }


    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            Log.e("RESULT****", "OK");
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
            } else if (requestCode == 2) {
                String latitude = String.valueOf(data.getDoubleExtra(LATITUDE, 0.0));
                Log.e("LATITUDE****", latitude.toString());
                String longitude = String.valueOf(data.getDoubleExtra(LONGITUDE, 0.0));
                Log.e("LONGITUDE****", longitude.toString());
                String address = data.getStringExtra(LOCATION_ADDRESS);
                Log.e("ADDRESS****", address.toString());
            }
        }
        if (resultCode == Activity.RESULT_CANCELED) {
            Log.e("RESULT****", "CANCELLED");
        }
    }


}
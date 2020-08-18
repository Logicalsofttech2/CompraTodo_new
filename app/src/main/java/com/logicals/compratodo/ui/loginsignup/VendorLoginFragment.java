package com.logicals.compratodo.ui.loginsignup;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.logicals.compratodo.R;
import com.logicals.compratodo.login_signup.CommonSignupActivity;
import com.logicals.compratodo.vendor.activity.VendorHome;
import com.logicals.compratodo.databinding.FragmentVendorLoginBinding;
import com.logicals.compratodo.https.HttpsRequest;
import com.logicals.compratodo.interfacess.Consts;
import com.logicals.compratodo.interfacess.Helper;
import com.logicals.compratodo.model.UserDTO;
import com.logicals.compratodo.preferences.SharedPrefrence;
import com.logicals.compratodo.utils.ProjectUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class VendorLoginFragment extends Fragment {

    private static String TAG = CustomerLoginFragment.class.getSimpleName();

    HashMap<String, String> param = new HashMap<>();
    UserDTO userDTO;
    SharedPrefrence sharedPrefrence;
    FragmentVendorLoginBinding binding;
    FusedLocationProviderClient fusedLocationClient;
    final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    String lat="",lng="",token_id="";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
     binding= DataBindingUtil.inflate(inflater,R.layout.fragment_vendor_login, container, false);

        sharedPrefrence=SharedPrefrence.getInstance(getActivity());
        Log.e(TAG, "onCreateView: "+sharedPrefrence.getValue(Consts.FIREBASETOKEN) );
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              checkData();
               }
        });

        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CommonSignupActivity.class)

                );

            }
        });
        binding.tvForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(),ForgetPassword.class);
                startActivity(in);

            }
        });
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
            findLoc();
        } else {
            findLoc();
        }




   return binding.getRoot();
    }


    private void findLoc() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
            findLoc();
        } else {


            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), location -> {

                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {


                            lat= String.valueOf(location.getLatitude());
                            lng= String.valueOf(location.getLongitude());


                            Geocoder geocoder = new Geocoder(getActivity());
                            try {
                                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),
                                        location.getLongitude(), 1);

                                final String address = addresses.get(0).getAddressLine(0);
                                final String localAddress = addresses.get(0).getFeatureName();
                                final String street = addresses.get(0).getSubLocality();
                                final String city = addresses.get(0).getLocality();
                                final String state = addresses.get(0).getAdminArea();
                                final String country = addresses.get(0).getCountryName();
                                final String postalCode = addresses.get(0).getPostalCode();

                                String title = address + " street " + street
                                        + " localAddress " + localAddress + " city "
                                        + city + " state " + state + " postalCode "
                                        + postalCode + " country " + country;

                                Log.e("Address", " Address " + title);
                                param.put(Consts.ADDRESS, title);

//                            getLocationAPI(location.getLatitude(),location.getLongitude(),address);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            Log.e("Address", location.getLatitude()
                                    + " Address onLocationChanged: "
                                    + location.getLongitude());

                        }
                    });
        }

    }


    private void checkData() {if (!binding.etEmail.getText().toString().matches(emailPattern) && binding.etEmail.getText().toString().isEmpty()) {
        binding.etEmail.setError(getResources().getString(R.string.val_email));
        binding.etEmail.requestFocus();
    } else if (binding.etPassword.getText().toString().isEmpty()) {
        binding.etPassword.setError(getResources().getString(R.string.val_password));
        binding.etPassword.requestFocus();
    } else {
        login();
    }
    }

    private void login() {
        ProjectUtils.showProgressDialog(getActivity(),true,getResources().getString(R.string.please_wait));
        param.put(Consts.MOBILE_EMAIL, ProjectUtils.getEditTextValue(binding.etEmail));
        param.put(Consts.PASSWORD, ProjectUtils.getEditTextValue(binding.etPassword));
        param.put(Consts.LAT, lat);
        param.put(Consts.LANG, lng);
        param.put(Consts.FCM_ID, "fcm");
        new HttpsRequest(Consts.LOGIN,param,getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();

                if(flag){

                    try {
                        userDTO = new Gson().fromJson(response.getJSONObject("data").toString(), UserDTO.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if(userDTO.getRole().equalsIgnoreCase("1")){
                        modeDialogBox(getResources().getString(R.string.login_as_user));

                    }else {
                        sharedPrefrence.setParentUser(userDTO,Consts.USER_DTO);
                        sharedPrefrence.setLogin(true);

                        Intent in = new Intent(getActivity(), VendorHome.class);
                        startActivity(in);
                        getActivity().finish();

                    }
                }else {

                modeDialogBox(msg);
                }
            }
        });

    }

    private void modeDialogBox(String mode) {

        assert getActivity() != null;
        Dialog dialog = new Dialog(getActivity());
        if(dialog.getWindow() != null){
            dialog.getWindow().setBackgroundDrawable(new
                    ColorDrawable(Color.TRANSPARENT));
        }

        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.custom_dialog_mode_change,null);

        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setAttributes(layoutParams);
        dialog.show();


        TextView textMode = view.findViewById(R.id.textMode);
        AppCompatButton buttonOk = view.findViewById(R.id.buttonOk);

        textMode.setText(mode);

        buttonOk.setOnClickListener(v -> {

            dialog.dismiss();

        });

    }





}

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
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.logicals.compratodo.R;
import com.logicals.compratodo.databinding.FragmentCustomerSignupBinding;
import com.logicals.compratodo.https.HttpsRequest;
import com.logicals.compratodo.interfacess.Consts;
import com.logicals.compratodo.interfacess.Helper;
import com.logicals.compratodo.model.UserDTO;
import com.logicals.compratodo.preferences.SharedPrefrence;
import com.logicals.compratodo.utils.ProjectUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerSignupFragment extends Fragment {

    final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    FragmentCustomerSignupBinding binding;
    private static String TAG = CustomerSignupFragment.class.getSimpleName();

    FusedLocationProviderClient fusedLocationClient;
    UserDTO userDTO;
    HashMap<String, String> param = new HashMap<>();

    SharedPrefrence sharedPrefrence;
    String lat = "", lng = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_customer_signup, container, false);

        sharedPrefrence=SharedPrefrence.getInstance(getActivity());
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
            findLoc();
        } else {
            findLoc();
        }

        binding.tvTerms.setText(getResources().getString(R.string.i_agree)+ " "+getResources().getString(R.string.menu_terms));
        binding.ivSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkData();

            }
        });


        return binding.getRoot();
    }

    private void findLoc() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
            findLoc();
        } else {


            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), location -> {

                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {


                            lat = String.valueOf(location.getLatitude());
                            lng = String.valueOf(location.getLongitude());


                            Log.e("Address", location.getLatitude()
                                    + " Address onLocationChanged: "
                                    + location.getLongitude());


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

                            param.put(Consts.LAT, lat);
                            param.put(Consts.LANG, lng);

                        }
                    });
        }

    }

    private void checkData() {
        if (binding.etName.getText().toString().isEmpty()) {
            binding.etName.setError(getResources().getString(R.string.val_name));
        } else if (!binding.etEmail.getText().toString().matches(emailPattern) && binding.etEmail.getText().toString().isEmpty()) {
            binding.etEmail.setError(getResources().getString(R.string.val_email));
            binding.etEmail.requestFocus();
        } else if (binding.etPassword.getText().toString().isEmpty()) {
            binding.etPassword.setError(getResources().getString(R.string.val_password));
            binding.etPassword.requestFocus();
        } else if (binding.etCnfpassword.getText().toString().isEmpty()) {
            binding.etCnfpassword.setError(getResources().getString(R.string.val_cnfpassword));
            binding.etCnfpassword.requestFocus();
        } else if (binding.etPhone.getText().toString().isEmpty()) {
            binding.etPhone.setError(getResources().getString(R.string.val_phone));
            binding.etPhone.requestFocus();
        } else  if (isValidPassword( binding.etPassword.getText().toString().trim())) {


            if ( binding.etPassword.getText().toString().trim().equals( binding.etCnfpassword.getText().toString().trim())) {

                signUP();


            }else {

                Toast.makeText(getActivity(), R.string.didnt_match, Toast.LENGTH_SHORT).show();

            }


        } else {
            binding.etPassword.setError(getResources().getString(R.string.val_correct_pass));
            binding.etPassword.requestFocus();

        }

    }

    private void signUP() {
        ProjectUtils.showProgressDialog(getContext(),true,getResources().getString(R.string.please_wait));
        param.put(Consts.NAME, ProjectUtils.getEditTextValue(binding.etName));
        param.put(Consts.EMAIL, ProjectUtils.getEditTextValue(binding.etEmail));
        param.put(Consts.PASSWORD, ProjectUtils.getEditTextValue(binding.etCnfpassword));
        param.put(Consts.CNFPASSWORD, ProjectUtils.getEditTextValue(binding.etPassword));
        param.put(Consts.MOBILE, ProjectUtils.getEditTextValue(binding.etPhone));
        param.put(Consts.FCM_ID, "dcm");

        new HttpsRequest(Consts.USER_SIGNUP, param, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {

                    try {

                        userDTO = new Gson().fromJson(response.getJSONObject("data").toString(), UserDTO.class);
                        Intent in = new Intent(getActivity(), OtpverificationActivity.class);
                        in.putExtra(Consts.USER_ID, userDTO.getId());
                        in.putExtra(Consts.EMAIL, userDTO.getEmail());
                        startActivity(in);
                        getActivity().finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
//                    ProjectUtils.showToast(getActivity(), msg);

                    modeDialogBox(msg);
                }
            }
        });

    }



    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=_<^>*,?/:;!'.-])(?=\\S+$).{4,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

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

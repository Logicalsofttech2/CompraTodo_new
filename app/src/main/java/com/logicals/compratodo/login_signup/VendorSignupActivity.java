package com.logicals.compratodo.login_signup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.utils.Utils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.logicals.compratodo.R;
import com.logicals.compratodo.databinding.ActivityVendorSignup2Binding;
import com.logicals.compratodo.https.HttpsRequest;
import com.logicals.compratodo.interfacess.Consts;
import com.logicals.compratodo.interfacess.Helper;
import com.logicals.compratodo.model.UserDTO;
import com.logicals.compratodo.preferences.SharedPrefrence;
import com.logicals.compratodo.ui.loginsignup.OtpverificationActivity;
import com.logicals.compratodo.utils.ProjectUtils;
import com.logicals.compratodo.vendor.adapter.StateAdapter;
import com.logicals.compratodo.vendor.model.StateDTO;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VendorSignupActivity extends AppCompatActivity implements View.OnClickListener{
    private static String TAG = VendorSignupActivity.class.getSimpleName();
    ActivityVendorSignup2Binding binding;
    final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    SharedPrefrence sharedPrefrence;
    File file;
    private Bitmap bitmap;
    File imgFile;
    int img = 0,imgpiker = 0,RESULT_OK = -1;
    private Uri mCropImageUri;
    UserDTO userDTO;

    HashMap<String, String> param = new HashMap<>();
    HashMap<String, String> neghbourparam = new HashMap<>();

    HashMap<String, File> paramfile = new HashMap<>();
    FusedLocationProviderClient fusedLocationClient;
    String lat = "", lng = "" ,path1 = "",path2 = "";
    CommonSignupActivity tabActivity;
    Context mContext;


    ArrayList<StateDTO> stateDTOArrayList = new ArrayList<>();
    StateAdapter adapter;
    StateDTO stateArraylistDto;


    ArrayList<StateDTO> cityDTOArrayList = new ArrayList<>();
    StateAdapter adapter2;
    StateDTO cityArraylistDto;


    ArrayList<StateDTO> neghbourDTOArrayList = new ArrayList<>();
    StateAdapter adapter3;
    StateDTO neghbourArraylistDto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_vendor_signup2);
        mContext=VendorSignupActivity.this;

        get_States_Api();
        sharedPrefrence=SharedPrefrence.getInstance(mContext);
        binding.ivSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(mContext, VendorHome.class));
                checkData();

            }
        });

        binding.tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, VendorLoginActivity.class));
            }
        });


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
            findLoc();
        } else {
            findLoc();
        }


        binding.tvTerms.setText(getResources().getString(R.string.i_agree)+ " "+getResources().getString(R.string.menu_terms));
        binding.Farmer.setOnClickListener(this);
        binding.Wholesale.setOnClickListener(this);
        binding.Retail.setOnClickListener(this);
        binding.ivPermit.setOnClickListener(this);
        binding.ivHealthCertificate.setOnClickListener(this);
        binding.tvHeathcertificate.setOnClickListener(this);
        binding.tvNext.setOnClickListener(this);



    }

    private void get_States_Api() {
        ProjectUtils.showProgressDialog(mContext,false,getResources().getString(R.string.please_wait));

            new HttpsRequest(Consts.GET_STATES, mContext).stringGet(TAG, new Helper() {
                @Override
                public void backResponse(boolean flag, String msg, JSONObject response) {
                    if (flag) {
                        try {
                            ProjectUtils.pauseProgressDialog();

                            Type listType = new TypeToken<ArrayList<StateDTO>>() {}.getType();
                            stateDTOArrayList = new Gson().fromJson(response.getJSONArray("data").toString(), listType);
                            adapter = new StateAdapter(mContext, R.layout.food_type_list, stateDTOArrayList);
                            adapter.setDropDownViewResource(R.layout.food_type_list);
                            binding.spinnerStates.setAdapter(adapter);
                            binding.spinnerStates.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
                                    Log.e("TAG", "onItemSelected: " + position);
                                    stateArraylistDto = stateDTOArrayList.get(position);
                                    neghbourparam.put(Consts.STATE_ID,stateArraylistDto.getId());

                                    param.put(Consts.STATE_ID,stateArraylistDto.getId());
                                    Log.e(TAG, "state select: " + stateArraylistDto.getName()+" "+stateArraylistDto.getId());

                                    binding.ivspin.setVisibility(View.GONE);
                                    get_city_Api();

                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> parentView) {
                                }

                            });

                        } catch (Exception e) {
                        }


                    } else {
                        ProjectUtils.pauseProgressDialog();

                        ProjectUtils.showToast(mContext, msg);
                    }
                }
            });
        }

    private void get_city_Api() {
            new HttpsRequest(Consts.GET_CITY,neghbourparam, mContext).stringPost(TAG, new Helper() {
                @Override
                public void backResponse(boolean flag, String msg, JSONObject response) {
                    if (flag) {
                        try {
                            Type listType = new TypeToken<ArrayList<StateDTO>>() {}.getType();
                            cityDTOArrayList = new Gson().fromJson(response.getJSONArray("data").toString(), listType);
                            adapter2 = new StateAdapter(mContext, R.layout.food_type_list, cityDTOArrayList);
                            adapter2.setDropDownViewResource(R.layout.food_type_list);
                            binding.spinnerCity.setAdapter(adapter2);
                            binding.spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
                                    Log.e("TAG", "onItemSelected: " + position);
                                    cityArraylistDto = cityDTOArrayList.get(position);
                                    param.put(Consts.CITY,cityArraylistDto.getId());
                                    neghbourparam.put(Consts.CITY_ID,cityArraylistDto.getId());
                                    Log.e(TAG, "city select: " + cityArraylistDto.getName()+" "+cityArraylistDto.getId());

                                    //Log.e(TAG, "onItemSelected: " + stateArraylistDto.getName());
                                    binding.ivSpin2.setVisibility(View.GONE);
                                    get_neighbour_Api();




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

    private void get_neighbour_Api() {
            new HttpsRequest(Consts.GET_NEIGHBOURHOOD,neghbourparam, mContext).stringPost(TAG, new Helper() {
                @Override
                public void backResponse(boolean flag, String msg, JSONObject response) {
                    if (flag) {
                        try {
                            Type listType = new TypeToken<ArrayList<StateDTO>>() {}.getType();
                            neghbourDTOArrayList = new Gson().fromJson(response.getJSONArray("data").toString(), listType);
                            adapter3 = new StateAdapter(mContext, R.layout.food_type_list, neghbourDTOArrayList);
                            adapter3.setDropDownViewResource(R.layout.food_type_list);
                            binding.spinnerNeighbour.setAdapter(adapter3);
                            binding.spinnerNeighbour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
                                    Log.e("TAG", "onItemSelected: " + position);
                                    neghbourArraylistDto = neghbourDTOArrayList.get(position);
                                    param.put(Consts.NEGHBOUR_ID,neghbourArraylistDto.getId());
                                    Log.e(TAG, "neghbor select: " + neghbourArraylistDto.getName()+" "+neghbourArraylistDto.getId());
                                    binding.ivSpin3.setVisibility(View.GONE);

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



    private void findLoc() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
            findLoc();
        } else {


            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener((Activity) mContext, location -> {

                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {


                            lat = String.valueOf(location.getLatitude());
                            lng = String.valueOf(location.getLongitude());


                            Geocoder geocoder = new Geocoder(mContext);
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
                            param.put(Consts.LAT, lat);
                            param.put(Consts.LANG, lng);

                        }
                    });
        }

    }

    private void checkData() {
        if (binding.Retail.isChecked()) {
            param.put(Consts.ROLE, "2");
            if (binding.etName.getText().toString().isEmpty()) {
                binding.etName.setError(getResources().getString(R.string.val_name));
            }
           else if (binding.etLname.getText().toString().isEmpty()) {
                binding.etName.setError(getResources().getString(R.string.val_lname));
            }
            else if (!binding.etEmail.getText().toString().matches(emailPattern) && binding.etEmail.getText().toString().isEmpty()) {
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
            } else if (binding.etAccount.getText().toString().isEmpty()) {
                binding.etAccount.setError(getResources().getString(R.string.val_account));
                binding.etAccount.requestFocus();
            } else if (binding.etAddress.getText().toString().isEmpty()){
                binding.etAddress.setError(getResources().getString(R.string.val_address));
                binding.etAddress.requestFocus();
            }


            else if (isValidPassword( binding.etPassword.getText().toString().trim())) {
                if ( binding.etPassword.getText().toString().trim().equals( binding.etCnfpassword.getText().toString().trim())) {
                    signup();
                }else {
                    Toast.makeText(mContext, R.string.didnt_match, Toast.LENGTH_SHORT).show();
                }


            } else {
                binding.etPassword.setError(getResources().getString(R.string.val_correct_pass));
                binding.etPassword.requestFocus();

            }
        } else if (binding.Wholesale.isChecked()) {
            param.put(Consts.ROLE, "3");

            if (binding.etName.getText().toString().isEmpty()) {
                binding.etName.setError(getResources().getString(R.string.val_name));
            }   else if (binding.etLname.getText().toString().isEmpty()) {
                binding.etName.setError(getResources().getString(R.string.val_lname));
            }
            else if (!binding.etEmail.getText().toString().matches(emailPattern) && binding.etEmail.getText().toString().isEmpty()) {
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
            } else if (binding.etAccount.getText().toString().isEmpty()) {
                binding.etAccount.setError(getResources().getString(R.string.val_account));
                binding.etAccount.requestFocus();
            } else if (path2.equalsIgnoreCase("")) {
                binding.tvPermit.setError(getResources().getString(R.string.val_permit));
                binding.tvPermit.requestFocus();
            }else if (binding.etAddress.getText().toString().isEmpty()){
                binding.etAddress.setError(getResources().getString(R.string.val_address));
                binding.etAddress.requestFocus();
            } else if (isValidPassword( binding.etPassword.getText().toString().trim())) {


                if ( binding.etPassword.getText().toString().trim().equals( binding.etCnfpassword.getText().toString().trim())) {

                    signup();


                }else {

                    Toast.makeText(mContext, R.string.didnt_match, Toast.LENGTH_SHORT).show();

                }


            } else {
                binding.etPassword.setError(getResources().getString(R.string.val_correct_pass));
                binding.etPassword.requestFocus();

            }
        } else if (binding.Farmer.isChecked()) {
            param.put(Consts.ROLE, "4");

            if (binding.etName.getText().toString().isEmpty()) {
                binding.etName.setError(getResources().getString(R.string.val_name));
            }
            else if (binding.etLname.getText().toString().isEmpty()) {
                binding.etName.setError(getResources().getString(R.string.val_lname));
            }
            else if (!binding.etEmail.getText().toString().matches(emailPattern) && binding.etEmail.getText().toString().isEmpty()) {
                binding.etEmail.setError(getResources().getString(R.string.val_email));
                binding.etEmail.requestFocus();
            } else if (binding.etCnfpassword.getText().toString().isEmpty()) {
                binding.etCnfpassword.setError(getResources().getString(R.string.val_cnfpassword));
                binding.etCnfpassword.requestFocus();
            } else if (binding.etPassword.getText().toString().isEmpty()) {
                binding.etPassword.setError(getResources().getString(R.string.val_password));
                binding.etPassword.requestFocus();
            } else if (binding.etPhone.getText().toString().isEmpty()) {
                binding.etPhone.setError(getResources().getString(R.string.val_phone));
                binding.etPhone.requestFocus();
            } else if (binding.etAccount.getText().toString().isEmpty()) {
                binding.etAccount.setError(getResources().getString(R.string.val_account));
                binding.etAccount.requestFocus();
            } else if (path1.equalsIgnoreCase("")) {
                binding.tvHeathcertificate.setError(getResources().getString(R.string.val_heathcertificate));
                binding.tvHeathcertificate.requestFocus();
            }else if (binding.etAddress.getText().toString().isEmpty()){
                binding.etAddress.setError(getResources().getString(R.string.val_address));
                binding.etAddress.requestFocus();
            } else if (isValidPassword( binding.etPassword.getText().toString().trim())) {
                if ( binding.etPassword.getText().toString().trim().equals( binding.etCnfpassword.getText().toString().trim())) {
                    signup();
                }else {
                    Toast.makeText(mContext, R.string.didnt_match, Toast.LENGTH_SHORT).show();
                }
            } else {
                binding.etPassword.setError(getResources().getString(R.string.val_correct_pass));
                binding.etPassword.requestFocus();
            }
        } else {
            Toast.makeText(mContext, R.string.sellertype, Toast.LENGTH_SHORT).show();
        }

    }

    private void signup() {
        ProjectUtils.showProgressDialog(mContext, true, getResources().getString(R.string.please_wait));
        param.put(Consts.NAME, ProjectUtils.getEditTextValue(binding.etName));
        param.put(Consts.LNAME, ProjectUtils.getEditTextValue(binding.etLname));
        param.put(Consts.EMAIL, ProjectUtils.getEditTextValue(binding.etEmail));
        param.put(Consts.PASSWORD, ProjectUtils.getEditTextValue(binding.etCnfpassword));
        param.put(Consts.CNFPASSWORD, ProjectUtils.getEditTextValue(binding.etPassword));
        param.put(Consts.MOBILE, ProjectUtils.getEditTextValue(binding.etPhone));
        param.put(Consts.RUC, ProjectUtils.getEditTextValue(binding.etRUC));
        param.put(Consts.USERNAME, ProjectUtils.getEditTextValue(binding.etDNI));
        param.put(Consts.ACCOUNT_NO, ProjectUtils.getEditTextValue(binding.etAccount));
        param.put(Consts.STREET_ADDRESS, ProjectUtils.getEditTextValue(binding.etAddress));
        param.put(Consts.REFRENCE_ADDRESS, ProjectUtils.getEditTextValue(binding.etRefrenceaddress));

        param.put(Consts.FCM_ID, "fcm");
        new HttpsRequest(Consts.VENDOR_SIGNUP, param, paramfile, mContext).imagePost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {
                        userDTO = new Gson().fromJson(response.getJSONObject("data").toString(), UserDTO.class);
                        Intent in = new Intent(mContext, OtpverificationActivity.class);
                        in.putExtra(Consts.USER_ID, userDTO.getId());
                        in.putExtra(Consts.EMAIL, userDTO.getEmail());
                        startActivity(in);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
//                    ProjectUtils.showToast(mContext, msg);
                    modeDialogBox(msg);
                }
            }
        });









    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Retail:
                if (binding.Retail.isChecked()) {
                    binding.llPermit.setVisibility(View.GONE);
                }
                break;
            case R.id.Wholesale:
                if (binding.Wholesale.isChecked()) {
                    binding.llPermit.setVisibility(View.VISIBLE);
                }

                break;
            case R.id.Farmer:
                if (binding.Farmer.isChecked()) {
                    binding.llPermit.setVisibility(View.GONE);
                }

                break;
            case R.id.tvNext:

                if (binding.Retail.isChecked()) {
                    if (binding.etDNI.getText().toString().isEmpty()) {
                        binding.etDNI.setError(getResources().getString(R.string.val_dni));
                        binding.etDNI.requestFocus();
                    } else {
                        binding.llVerify.setVisibility(View.GONE);
                        binding.llSignup.setVisibility(View.VISIBLE);
                    }
                } else if (binding.Wholesale.isChecked()) {
                    if (binding.etRUC.getText().toString().isEmpty()) {
                        binding.etRUC.setError(getResources().getString(R.string.val_ruc));
                        binding.etRUC.requestFocus();
                    } else if (binding.etDNI.getText().toString().isEmpty()) {
                        binding.etDNI.setError(getResources().getString(R.string.val_dni));
                        binding.etDNI.requestFocus();
                    } else {
                        binding.llVerify.setVisibility(View.GONE);
                        binding.llSignup.setVisibility(View.VISIBLE);
                    }
                } else if (binding.Farmer.isChecked()) {
                    if (binding.etDNI.getText().toString().isEmpty()) {
                        binding.etDNI.setError(getResources().getString(R.string.val_dni));
                        binding.etDNI.requestFocus();
                    } else {
                        binding.llVerify.setVisibility(View.GONE);
                        binding.llSignup.setVisibility(View.VISIBLE);
                    }
                } else {

                    Toast.makeText(mContext, R.string.sellertype, Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.ivHealthCertificate:

                onSelectImageClick(v);
                imgpiker=0;
                break;
            case R.id.ivPermit:

                onSelectImageClick(v);
                imgpiker=1;
                break;


        }
    }


    private void modeDialogBox(String mode) {

        assert mContext != null;
        Dialog dialog = new Dialog(mContext);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new
                    ColorDrawable(Color.TRANSPARENT));
        }

        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.custom_dialog_mode_change, null);

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

    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=_<^>*,?/:;!'.-])(?=\\S+$).{4,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    public void onSelectImageClick(View view) {


        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, 100);

    }



    @Override
    @SuppressLint("NewApi")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 100){
            Uri imageUri;
            imageUri = data.getData();

            try {

                file = toFile(imageUri);
                Log.d("file", "File...:::: uti - " + file.getPath() + " file -" + file + " : " + file.exists());
                bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), imageUri);

                imgFile = bitmapToFile(bitmap);
                Log.e("FIle Img = ", "" + imgFile);
                Log.e("uri = ", "" + imageUri);
                Log.e("bitmap = ", "" + bitmap);

            } catch (Exception e) {

                Log.e("check excep", e.toString());
                e.printStackTrace();
            }
            if (imgpiker == 0) {
                binding.ivHealthCertificate.setImageBitmap(bitmap);
                img = 1;
                paramfile.put(Consts.HEALTH_CERTIFICATE, imgFile);
                path1=imgFile.getAbsolutePath();

            } else {

                binding.ivPermit.setImageBitmap(bitmap);
                img = 2;
                paramfile.put(Consts.PERMIT, imgFile);
                path2=imgFile.getAbsolutePath();

            }
        }else
            Toast.makeText(mContext, R.string.failed, Toast.LENGTH_LONG).show();
    }


    public File toFile(Uri uri) {
        if (uri == null) return null;
        Log.d(">>> uri path:", uri.getPath());
        Log.d(">>> uri string:", uri.toString());
        return new File(uri.getPath());
    }

    //convert bitmap to file
    private File bitmapToFile(Bitmap bitmap) {
        try {
            String name = System.currentTimeMillis() + ".png";
            File file = new File(mContext.getCacheDir(), name);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 60, bos);
            byte[] bArr = bos.toByteArray();
            bos.flush();
            bos.close();

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bArr);
            fos.flush();
            fos.close();

            return file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // required permissions granted, start crop image activity
//            startCropImageActivity(mCropImageUri);
        } else {
            Toast.makeText(mContext, R.string.img_permission, Toast.LENGTH_LONG).show();
        }
    }





/*

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        tabActivity =(CommonSignupActivity)context;
    }
*/











}
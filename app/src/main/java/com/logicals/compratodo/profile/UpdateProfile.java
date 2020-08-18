package com.logicals.compratodo.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.logicals.compratodo.R;
import com.logicals.compratodo.databinding.ActivityUpdateProfileBinding;
import com.logicals.compratodo.https.HttpsRequest;
import com.logicals.compratodo.interfacess.Consts;
import com.logicals.compratodo.interfacess.Helper;
import com.logicals.compratodo.model.UserDTO;
import com.logicals.compratodo.preferences.SharedPrefrence;
import com.logicals.compratodo.utils.ProjectUtils;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

public class UpdateProfile extends AppCompatActivity implements View.OnClickListener {
    private static String TAG = UpdateProfile.class.getSimpleName();
    ActivityUpdateProfileBinding binding;
    Context mContext;
    File file;
    private Uri mCropImageUri;
    int img = 0, check,imgcheck=0;
    SharedPrefrence sharedPrefrence;
    UserDTO userDTO;

    String lat = "", lng = "" ,path1 = "",path2 = "";
    HashMap<String, String> param = new HashMap<>();
    HashMap<String, File> paramfile = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_profile);
        mContext = UpdateProfile.this;
        sharedPrefrence = SharedPrefrence.getInstance(mContext);
        userDTO = sharedPrefrence.getParentUser(Consts.USER_DTO);

        binding.civProfilePic.setOnClickListener(this);
        binding.ivBack.setOnClickListener(this);
        binding.tvUpdate.setOnClickListener(this);
        binding.Farmer.setOnClickListener(this);
        binding.Wholesale.setOnClickListener(this);
        binding.Retail.setOnClickListener(this);
        binding.ivHealthCertificate.setOnClickListener(this);
        binding.ivPermit.setOnClickListener(this);

        if (getIntent().hasExtra(Consts.SCREEN_TAG)) {
            check = Integer.parseInt(getIntent().getStringExtra(Consts.SCREEN_TAG));
            if (check == 1) {
                setdata();
            } else {
                setdata1();
            }

        }


    }

    private void setdata() {
        binding.etEmail.setText(userDTO.getEmail());
        binding.etName.setText(userDTO.getName());
        binding.etPhone.setText(userDTO.getMobile());

        binding.llSellertype.setVisibility(View.GONE);
        binding.etAccount.setVisibility(View.GONE);
        binding.llHealthCertificate.setVisibility(View.GONE);
        binding.llPermit.setVisibility(View.GONE);

        Glide.with(mContext)
                .load(Consts.USER_IMAGE_URL + userDTO.getImage())
                .error(R.drawable.logo)
                .into(binding.civProfilePic);
        param.put(Consts.USER_ID, userDTO.getId());

    }
    private void setdata1() {
        binding.etEmail.setText(userDTO.getEmail());
        binding.etName.setText(userDTO.getName());
        binding.etPhone.setText(userDTO.getMobile());
        binding.etAccount.setText(userDTO.getAccount_no());

        binding.llSellertype.setVisibility(View.VISIBLE);
        binding.etAccount.setVisibility(View.VISIBLE);

        if(userDTO.getHealth_certificate().equalsIgnoreCase("")) {
            binding.llHealthCertificate.setVisibility(View.VISIBLE);

        }else {
            path1=userDTO.getHealth_certificate();
        }


        Glide.with(mContext)
                .load(Consts.USER_IMAGE_URL + userDTO.getImage())
                .error(R.drawable.logo)
                .into(binding.civProfilePic);
        param.put(Consts.USER_ID, userDTO.getId());

    }


    public void onSelectImageClick(View view) {
        CropImage.startPickImageActivity(this);
    }


    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);

            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                mCropImageUri = imageUri;

                try {

                    file = toFile(mCropImageUri);
                    Log.d("file", "File...:::: uti - " + file.getPath() + " file -" + file + " : " + file.exists());


                } catch (Exception e) {

                    Log.e("check excep", e.toString());
                    e.printStackTrace();
                }
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                // no permissions required or already grunted, can start crop image activity
                startCropImageActivity(imageUri);
            }
        }

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                try {

                    file = toFile(result.getUri());
                    Log.d("file", "File...:::: uti - " + file.getPath() + " file -" + file + " : " + file.exists());

                } catch (Exception e) {

                    Log.e("check excep", e.toString());
                    e.printStackTrace();
                }


                if (imgcheck == 1) {
                    binding.ivHealthCertificate.setImageURI(result.getUri());
                    img = 1;
                    paramfile.put(Consts.HEALTH_CERTIFICATE, file);
                    path1=file.getAbsolutePath();

                } else    if (imgcheck == 2) {

                    binding.ivPermit.setImageURI(result.getUri());
                    img = 2;
                    paramfile.put(Consts.PERMIT, file);
                    path2=file.getAbsolutePath();

                }else if (imgcheck == 0) {
                    binding.civProfilePic.setImageURI(result.getUri());
                    img = 1;
                    paramfile.put(Consts.IMAGE, file);
                }
                //  Toast.makeText(this, "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG).show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }


    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
    }

    public File toFile(Uri uri) {
        if (uri == null) return null;
        Log.d(">>> uri path:", uri.getPath());
        Log.d(">>> uri string:", uri.toString());
        return new File(uri.getPath());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.civProfilePic:

                imgcheck=0;
                onSelectImageClick(v);
                break;
            case R.id.tvUpdate:
                checkData();
                break;
            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.Retail:
                if (binding.Retail.isChecked()) {
                    binding.llPermit.setVisibility(View.GONE);
                }
                break;
            case R.id.Wholesale:
                if (binding.Wholesale.isChecked()) {
                    if(userDTO.getPermit().equalsIgnoreCase("")){
                        binding.llPermit.setVisibility(View.VISIBLE);
                    }else {
                    path2=userDTO.getPermit();
                }}

                break;
            case R.id.Farmer:
                if (binding.Farmer.isChecked()) {
                    binding.llPermit.setVisibility(View.GONE);

                    if(userDTO.getHealth_certificate().equalsIgnoreCase("")){
                        binding.llHealthCertificate.setVisibility(View.VISIBLE);
                    }else {
                        path1=userDTO.getHealth_certificate();
                    }

                }
                break;

            case R.id.ivHealthCertificate:
                onSelectImageClick(v);
                imgcheck=1;
                break;
            case R.id.ivPermit:
                onSelectImageClick(v);
                imgcheck=2;
                break;

        }
    }

    private void checkData() {


        if(check==2) {
            if (binding.Retail.isChecked()) {
                param.put(Consts.ROLE, "2");


                if (binding.etName.getText().toString().isEmpty()) {
                    binding.etName.setError(getResources().getString(R.string.val_name));
                } else if (binding.etAccount.getText().toString().isEmpty()) {
                    binding.etAccount.setError(getResources().getString(R.string.val_account));
                    binding.etAccount.requestFocus();
                } else {
                    param.put(Consts.ACCOUNT_NO, ProjectUtils.getEditTextValue(binding.etAccount));
                    updateData();
                }
            } else if (binding.Wholesale.isChecked()) {
                param.put(Consts.ROLE, "3");

                if (binding.etName.getText().toString().isEmpty()) {
                    binding.etName.setError(getResources().getString(R.string.val_name));
                } else if (binding.etAccount.getText().toString().isEmpty()) {
                    binding.etAccount.setError(getResources().getString(R.string.val_account));
                    binding.etAccount.requestFocus();
                } else if (path2.equalsIgnoreCase("")) {
                    binding.tvPermit.setError(getResources().getString(R.string.val_permit));
                    binding.tvPermit.requestFocus();
                } else {
                    param.put(Consts.ACCOUNT_NO, ProjectUtils.getEditTextValue(binding.etAccount));
                    updateData();
                }
            } else if (binding.Farmer.isChecked()) {
                param.put(Consts.ROLE, "4");

                if (binding.etName.getText().toString().isEmpty()) {
                    binding.etName.setError(getResources().getString(R.string.val_name));
                } else if (binding.etAccount.getText().toString().isEmpty()) {
                    binding.etAccount.setError(getResources().getString(R.string.val_account));
                    binding.etAccount.requestFocus();
                } else if (path1.equalsIgnoreCase("")) {
                    binding.tvHeathcertificate.setError(getResources().getString(R.string.val_heathcertificate));
                    binding.tvHeathcertificate.requestFocus();
                } else {
                    param.put(Consts.ACCOUNT_NO, ProjectUtils.getEditTextValue(binding.etAccount));
                    updateData();
                }
            }
        }else {  if (binding.etName.getText().toString().isEmpty()) {
            binding.etName.setError(getResources().getString(R.string.val_name));
            binding.etName.requestFocus();
        }/*else    if (img==0) {
            Toast.makeText(mContext, R.string.val_profilepic, Toast.LENGTH_SHORT).show();
        }*/ else {
            updateData();
        }}




    }

    private void updateData() {
        param.put(Consts.NAME, ProjectUtils.getEditTextValue(binding.etName));
        param.put(Consts.MOBILE, ProjectUtils.getEditTextValue(binding.etPhone));

        new HttpsRequest(Consts.USER_UPDATE_PROFILE, param, paramfile, mContext).imagePost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        userDTO = new Gson().fromJson(response.getJSONObject("data").toString(), UserDTO.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    sharedPrefrence.setParentUser(userDTO, Consts.USER_DTO);
                    onBackPressed();
                } else {
//                    ProjectUtils.showToast(mContext, msg);
                    modeDialogBox(msg);

                }

            }
        });
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

}
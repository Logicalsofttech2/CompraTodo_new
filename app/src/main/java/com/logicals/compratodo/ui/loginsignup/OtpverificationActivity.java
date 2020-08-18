package com.logicals.compratodo.ui.loginsignup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.logicals.compratodo.R;
import com.logicals.compratodo.customer.mainhomeactivity.UserHomeActivity;
import com.logicals.compratodo.databinding.ActivityOtpverificationBinding;
import com.logicals.compratodo.https.HttpsRequest;
import com.logicals.compratodo.interfacess.Consts;
import com.logicals.compratodo.interfacess.Helper;
import com.logicals.compratodo.model.UserDTO;
import com.logicals.compratodo.preferences.SharedPrefrence;
import com.logicals.compratodo.utils.GenericTextWatcher;
import com.logicals.compratodo.utils.ProjectUtils;
import com.logicals.compratodo.vendor.activity.CraeteCatlogActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class OtpverificationActivity extends AppCompatActivity {
    private static String TAG = OtpverificationActivity.class.getSimpleName();
    ActivityOtpverificationBinding binding;
    String userid = "", email = "", otp = "";
    Context mContext;
    HashMap<String, String> param = new HashMap<>();
    UserDTO userDTO;
    SharedPrefrence sharedPrefrence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_otpverification);
        mContext = OtpverificationActivity.this;
        sharedPrefrence = SharedPrefrence.getInstance(mContext);
        if (getIntent().hasExtra(Consts.USER_ID)) {
            userid = getIntent().getStringExtra(Consts.USER_ID);
            email = getIntent().getStringExtra(Consts.EMAIL);
        }



        binding.tvPleaseType.setText(getResources().getText(R.string.please_type) + " " + email);
        binding.e1.addTextChangedListener(new GenericTextWatcher(binding.e2, binding.e1));
        binding.e2.addTextChangedListener(new GenericTextWatcher(binding.e3, binding.e1));
        binding.e3.addTextChangedListener(new GenericTextWatcher(binding.e4, binding.e2));
        binding.e4.addTextChangedListener(new GenericTextWatcher(binding.e5, binding.e3));
        binding.e5.addTextChangedListener(new GenericTextWatcher(binding.e5, binding.e4));

        binding.etServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOtp();

            }
        });


    }

    private void getOtp() {
        if (ProjectUtils.isEditTextFilled(binding.e1.getText().toString())) {
            ProjectUtils.showToast(mContext, getResources().getString(R.string.val_otp));
        } else if (ProjectUtils.isEditTextFilled(binding.e2.getText().toString())) {

            ProjectUtils.showToast(mContext, getResources().getString(R.string.val_otp));
        } else if (ProjectUtils.isEditTextFilled(binding.e3.getText().toString())) {

            ProjectUtils.showToast(mContext, getResources().getString(R.string.val_otp));
        } else if (ProjectUtils.isEditTextFilled(binding.e4.getText().toString())) {

            ProjectUtils.showToast(mContext, getResources().getString(R.string.val_otp));
        } else if (ProjectUtils.isEditTextFilled(binding.e5.getText().toString())) {

            ProjectUtils.showToast(mContext, getResources().getString(R.string.val_otp));
        } else {
            otp = ProjectUtils.getEditTextValue(binding.e1)
                    + ProjectUtils.getEditTextValue(binding.e2)
                    + ProjectUtils.getEditTextValue(binding.e3)
                    + ProjectUtils.getEditTextValue(binding.e4)
                    + ProjectUtils.getEditTextValue(binding.e5);
            checkotp();
        }
    }

    private void checkotp() {
        param.put(Consts.USER_ID, userid);
        param.put(Consts.OTP, otp);

        new HttpsRequest(Consts.VERIFY_OTP, param, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {

                    try {
                        userDTO = new Gson().fromJson(response.getJSONObject("data").toString(), UserDTO.class);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    sharedPrefrence.setParentUser(userDTO, Consts.USER_DTO);
                    if (userDTO.getRole().equalsIgnoreCase("1")) {
                        Intent in = new Intent(mContext, UserHomeActivity.class);
                        in.putExtra(Consts.USER_ID, userDTO.getId());
                        in.putExtra(Consts.EMAIL, userDTO.getEmail());
                        sharedPrefrence.setLogin(true);
                        startActivity(in);
                        finish();
                    } else {
                        Intent in = new Intent(mContext, CraeteCatlogActivity.class);
                        in.putExtra(Consts.USER_ID, userDTO.getId());
                        in.putExtra(Consts.EMAIL, userDTO.getEmail());
                        sharedPrefrence.setLogin(true);
                        startActivity(in);
                        finish();

                    }
                } else {
                    ProjectUtils.showToast(mContext, msg);
                }
            }
        });
    }


}

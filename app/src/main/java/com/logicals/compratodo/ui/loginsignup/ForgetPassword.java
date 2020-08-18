package com.logicals.compratodo.ui.loginsignup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.logicals.compratodo.R;
import com.logicals.compratodo.databinding.ActivityForgetPasswordBinding;
import com.logicals.compratodo.https.HttpsRequest;
import com.logicals.compratodo.interfacess.Consts;
import com.logicals.compratodo.interfacess.Helper;
import com.logicals.compratodo.utils.ProjectUtils;
import com.logicals.compratodo.vendor.ui.profile.ChangePassword;

import org.json.JSONObject;

import java.util.HashMap;

public class ForgetPassword extends AppCompatActivity {
    private static String TAG = ForgetPassword.class.getSimpleName();

    Context mContext;
    HashMap<String,String >param=new HashMap<>();
    ActivityForgetPasswordBinding binding;
    final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_forget_password);
            mContext=ForgetPassword.this;


        binding.tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.etEmail.getText().toString().matches(emailPattern) && binding.etEmail.getText().toString().isEmpty()) {
                    binding.etEmail.setError(getResources().getString(R.string.val_email));
                    binding.etEmail.requestFocus();
                } else{
                    forget();
                }
            }
        });
    }


    private void forget() {
        ProjectUtils.showProgressDialog(mContext, true, getResources().getString(R.string.please_wait));
        param.put(Consts.EMAIL, ProjectUtils.getEditTextValue(binding.etEmail));

        new HttpsRequest(Consts.FORGOT_PASSWORD, param, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();

                if (flag) {

                    try {

                        Intent in = new Intent(mContext, ChangePassword.class);
                        in.putExtra(Consts.EMAIL, ProjectUtils.getEditTextValue(binding.etEmail));
                        startActivity(in);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {


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
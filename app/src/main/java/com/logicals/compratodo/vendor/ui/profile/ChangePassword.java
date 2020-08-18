package com.logicals.compratodo.vendor.ui.profile;

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
import android.widget.Toast;

import com.logicals.compratodo.R;
import com.logicals.compratodo.databinding.ActivityChangePasswordBinding;
import com.logicals.compratodo.https.HttpsRequest;
import com.logicals.compratodo.interfacess.Consts;
import com.logicals.compratodo.interfacess.Helper;
import com.logicals.compratodo.ui.loginsignup.ForgetPassword;
import com.logicals.compratodo.ui.loginsignup.TabActivity;
import com.logicals.compratodo.utils.ProjectUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChangePassword extends AppCompatActivity {
    ActivityChangePasswordBinding binding;
    private static String TAG = ForgetPassword.class.getSimpleName();
    Context mContext;
    HashMap<String,String > param=new HashMap<>();
    String email="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_change_password);
        mContext=ChangePassword.this;
        if(getIntent().hasExtra(Consts.EMAIL))
            email=getIntent().getStringExtra(Consts.EMAIL);
        binding.tvChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });


    }

    private void check() {
        if (binding.etPasswordOld.getText().toString().isEmpty()) {
            binding.etPasswordOld.setError(getResources().getString(R.string.val_password));
            binding.etPasswordOld.requestFocus();
        }else  if (binding.etPassword.getText().toString().isEmpty()) {
            binding.etPassword.setError(getResources().getString(R.string.val_password));
            binding.etPassword.requestFocus();
        } else if (binding.etCnfpassword.getText().toString().isEmpty()) {
            binding.etCnfpassword.setError(getResources().getString(R.string.val_cnfpassword));
            binding.etCnfpassword.requestFocus();
        } else if (isValidPassword( binding.etPassword.getText().toString().trim())) {
            if ( binding.etPassword.getText().toString().trim().equals( binding.etCnfpassword.getText().toString().trim())) {
                changepassword();
            }else {
                Toast.makeText(mContext, R.string.didnt_match, Toast.LENGTH_SHORT).show();
            }
        } else {
            binding.etPassword.setError(getResources().getString(R.string.val_correct_pass));
            binding.etPassword.requestFocus();
        }


    }

    private void changepassword() {
        ProjectUtils.showProgressDialog(mContext, true, getResources().getString(R.string.please_wait));
        param.put(Consts.EMAIL, email);
        param.put(Consts.OLD_PASSWORD, ProjectUtils.getEditTextValue(binding.etPasswordOld));
        param.put(Consts.NEW_PASSWORD, ProjectUtils.getEditTextValue(binding.etPassword));
        param.put(Consts.CONFIRM_PASSWORD, ProjectUtils.getEditTextValue(binding.etCnfpassword));

        new HttpsRequest(Consts.FORGOT_PASSWORD, param, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();

                if (flag) {

                    try {

                        Intent in = new Intent(mContext, TabActivity.class);
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


    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=_<^>*,?/:;!'.-])(?=\\S+$).{4,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

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
package com.logicals.compratodo.customer.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.logicals.compratodo.R;
import com.logicals.compratodo.customer.model.AddressListModel;
import com.logicals.compratodo.databinding.ActivityAddAddressBinding;
import com.logicals.compratodo.https.HttpsRequest;
import com.logicals.compratodo.interfacess.Consts;
import com.logicals.compratodo.interfacess.Helper;
import com.logicals.compratodo.model.UserDTO;
import com.logicals.compratodo.preferences.SharedPrefrence;
import com.logicals.compratodo.utils.ProjectUtils;

import org.json.JSONObject;

import java.util.HashMap;

public class AddAddressActivity extends AppCompatActivity {
  TextView iv_save;
  ActivityAddAddressBinding binding;
    String selection;
    SharedPrefrence sharedPrefrence;
    HashMap<String, String> param = new HashMap<>();
    Context mContext;
    UserDTO userDTO;
    String type;
    AddressListModel addressListModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_address);
        sharedPrefrence = SharedPrefrence.getInstance(AddAddressActivity.this);
        userDTO = sharedPrefrence.getParentUser(Consts.USER_DTO);



//////////////////////////objects get ////////////////////////
        //collect our intent

        if (getIntent()!=null){
            Intent intent = getIntent();
             type=intent.getStringExtra("Type");
             addressListModel = intent.getParcelableExtra("Data");
            binding.setAddress(addressListModel);
        }


        if (getIntent()!=null){
            type=getIntent().getStringExtra("Type");
        }


        mContext = AddAddressActivity.this;
        binding.ivSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.etState.getText().toString().equals("")) {
                    binding.etState.setError("Please Enter State");
                    return;
                } else if (binding.etCity.getText().toString().equals("")) {
                    binding.etCity.setError("Please Enter City");
                    return;
                } else if (binding.etPincode.getText().toString().equals("")) {
                    binding.etPincode.setError("Please Enter PinCode");
                    return;
                } else if (binding.etHouseNo.getText().toString().equals("")) {
                    binding.etHouseNo.setError("Please Enter House No");
                    return;
                } else if (binding.etArea.getText().toString().equals("")) {
                    binding.etArea.setError("Please Enter Area");
                    return;
                } else if (binding.etLandmark.getText().toString().equals("")) {
                    binding.etLandmark.setError("Please Enter Landmark");
                    return;
                } else if (binding.etMobile.getText().toString().equals("")) {
                    binding.etMobile.setError("Please Enter Mobile");
                    return;
                } else if (selection == null) {
                    try {
                        selection = ((RadioButton) findViewById(binding.groupradio.getCheckedRadioButtonId())).getText().toString();
                        if (sharedPrefrence.isLoggedIn()) {
                            param.put(Consts.USER_ID, userDTO.getId());
                            param.put(Consts.STATE, ProjectUtils.getEditTextValue(binding.etState));
                            param.put(Consts.CITY, ProjectUtils.getEditTextValue(binding.etCity));
                            param.put(Consts.PINCODE, ProjectUtils.getEditTextValue(binding.etPincode));
                            param.put(Consts.HOUSENO, ProjectUtils.getEditTextValue(binding.etHouseNo));
                            param.put(Consts.AREA, ProjectUtils.getEditTextValue(binding.etArea));
                            param.put(Consts.LANDMARK, ProjectUtils.getEditTextValue(binding.etLandmark));
                            param.put(Consts.MOBILE, ProjectUtils.getEditTextValue(binding.etMobile));
                            param.put(Consts.TYPE, selection);
                            if (type.equals("Add")){
                                addAddress(Consts.ADD_ADDRESS);
                            }else {
                                param.put(Consts.ADDRESS_ID,addressListModel.getId());
                                addAddress(Consts.UPDATEADRESS);
                            }


                            } else {
                            Toast.makeText(AddAddressActivity.this, "Please Login First...", Toast.LENGTH_SHORT).show();
                        }


                    } catch (Exception e) {
                        Toast.makeText(AddAddressActivity.this, "please select delivery type", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }

        });


    }


    private void addAddress(String apiname) {

        ProjectUtils.showProgressDialog(mContext,false,getResources().getString(R.string.please_wait));
        new HttpsRequest(apiname,param,mContext).imagePost("TAG", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if(flag){
                    Toast.makeText(mContext, ""+msg, Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }else {
                  //  modeDialogBox(msg);
                }
            }
        });
    }








}
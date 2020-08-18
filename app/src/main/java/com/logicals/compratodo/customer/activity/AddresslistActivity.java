package com.logicals.compratodo.customer.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.logicals.compratodo.R;
import com.logicals.compratodo.customer.adapter.AddressListAdapter;
import com.logicals.compratodo.customer.model.AddressListModel;
import com.logicals.compratodo.databinding.ActivityAddresslistBinding;
import com.logicals.compratodo.https.HttpsRequest;
import com.logicals.compratodo.interfacess.Consts;
import com.logicals.compratodo.interfacess.Helper;
import com.logicals.compratodo.model.UserDTO;
import com.logicals.compratodo.preferences.SharedPrefrence;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class AddresslistActivity extends AppCompatActivity {
    HashMap<String,String> param=new HashMap<>();
    SharedPrefrence sharedPrefrence;
    ActivityAddresslistBinding binding;
    UserDTO userDTO;
    Context mContext;
    AddressListAdapter addressListAdapter;
    ArrayList<AddressListModel>addressListModel= new ArrayList<>();
String check_api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_addresslist);
        sharedPrefrence= SharedPrefrence.getInstance(AddresslistActivity.this);
        userDTO=sharedPrefrence.getParentUser(Consts.USER_DTO);
        mContext=AddresslistActivity.this;

        if (sharedPrefrence.isLoggedIn()){
            getAddressList();
        }

        if (getIntent()!=null){
            check_api=getIntent().getStringExtra(Consts.BILLING);
        }



    }

    private void getAddressList() {
            param.put(Consts.USER_ID,userDTO.getId());
            new HttpsRequest(Consts.ADDRESSLIST,param,mContext).stringPost("TAG", new Helper() {
                @Override
                public void backResponse(boolean flag, String msg, JSONObject response) {
                    if(flag){
                        try {
                            Type listType = new TypeToken<ArrayList<AddressListModel>>() {}.getType();
                            addressListModel = new Gson().fromJson(response.getJSONArray("data").toString(), listType);
                            if (mContext!=null){
                                setList();
                            }
                        } catch (Exception e) {
                        }
                    }else {
                        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    private void setList() {
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(mContext, RecyclerView.VERTICAL,false);
        binding.RvAddressList.setLayoutManager(linearLayoutManager);
        addressListAdapter = new AddressListAdapter(mContext,addressListModel,check_api);
        binding.RvAddressList.setAdapter(addressListAdapter);
    }

}
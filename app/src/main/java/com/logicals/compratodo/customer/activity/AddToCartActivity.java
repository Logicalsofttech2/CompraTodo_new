package com.logicals.compratodo.customer.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.logicals.compratodo.R;
import com.logicals.compratodo.customer.GetValue;
import com.logicals.compratodo.customer.adapter.AdapterAddtoCart;
import com.logicals.compratodo.customer.mainhomeactivity.UserHomeActivity;
import com.logicals.compratodo.customer.model.AddCartDTO;
import com.logicals.compratodo.databinding.ActivityAddToCartBinding;
import com.logicals.compratodo.https.HttpsRequest;
import com.logicals.compratodo.interfacess.Consts;
import com.logicals.compratodo.interfacess.Helper;
import com.logicals.compratodo.model.ProductDTO;
import com.logicals.compratodo.model.UserDTO;
import com.logicals.compratodo.preferences.SharedPrefrence;
import com.logicals.compratodo.preferences.Shared_Pref;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;

public class AddToCartActivity extends AppCompatActivity implements GetValue {
    SharedPrefrence sharedPrefrence;
    TextView tv_order;
    ActivityAddToCartBinding binding;
    HashMap<String, String> param = new HashMap<>();
    UserDTO userDTO;
    Context mContext;
    AddCartDTO maddCartDTO;
    AdapterAddtoCart adapterAddtoCart;
    ArrayList<ProductDTO> addCartDTO = new ArrayList<>();
    ArrayList<ProductDTO> addCartList;
    ImageView iv_back;
    GetValue callback;
    String getProductid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_to_cart);
        sharedPrefrence = SharedPrefrence.getInstance(AddToCartActivity.this);
        userDTO = sharedPrefrence.getParentUser(Consts.USER_DTO);
        mContext = AddToCartActivity.this;


        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        binding.tvOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddToCartActivity.this, PlaceorderActivity.class)
                        .putExtra(Consts.BILLING, Consts.ADDTOCART));
            }
        });


        binding.strtshopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

          startActivity(new Intent(mContext, UserHomeActivity.class));


            }
        });


        if (sharedPrefrence.isLoggedIn()) {
            getCategory();
        }


    }


    private void getCategory() {
        param.put(Consts.USER_ID, userDTO.getId());
        new HttpsRequest(Consts.MYCARTLIST, param, mContext).stringPost("TAG", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        Type listType = new TypeToken<ArrayList<ProductDTO>>() {
                        }.getType();
                        addCartDTO = new Gson().fromJson(response.getJSONArray("data").toString(), listType);
                        setList();
                    } catch (Exception e) {
                    }
                } else {
                  binding.LinearDataNotFound.setVisibility(View.VISIBLE);
                  binding.tvOrder.setVisibility(View.GONE);
                  //Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void setList() {
        binding.tvOrder.setVisibility(View.VISIBLE);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        binding.RVAddCart.setLayoutManager(gridLayoutManager);
        adapterAddtoCart = new AdapterAddtoCart(mContext, addCartDTO, callback);
        binding.RVAddCart.setAdapter(adapterAddtoCart);
    }


    @Override
    public void GetValues(String values) {
        this.getProductid = values;

        Log.e("check product id ",getProductid);


        Shared_Pref.setProductId(mContext, getProductid);
    }

    @Override
    public void GetHideButton(boolean value) {
        if (value==true){
            binding.tvOrder.setVisibility(View.GONE);
            binding.LinearDataNotFound.setVisibility(View.VISIBLE);
        }




    }
}
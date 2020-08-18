package com.logicals.compratodo.customer.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.logicals.compratodo.R;
import com.logicals.compratodo.customer.adapter.AdapterNearBY;
import com.logicals.compratodo.customer.model.ProductDetailDTO;
import com.logicals.compratodo.databinding.ActivityRelatedProductsBinding;
import com.logicals.compratodo.https.HttpsRequest;
import com.logicals.compratodo.interfacess.Consts;
import com.logicals.compratodo.interfacess.Helper;
import com.logicals.compratodo.model.UserDTO;
import com.logicals.compratodo.preferences.SharedPrefrence;

import org.json.JSONObject;

import java.util.HashMap;

public class RelatedProductsActivity extends AppCompatActivity {
    ActivityRelatedProductsBinding binding;
    String productId;
    SharedPrefrence sharedPrefrence;

    HashMap<String, String> param = new HashMap<>();
    Context mContext;
    UserDTO userDTO;
    ProductDetailDTO productDetailDTO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_related_products);

        mContext=RelatedProductsActivity.this;
        sharedPrefrence = SharedPrefrence.getInstance(mContext);
        userDTO = sharedPrefrence.getParentUser(Consts.USER_DTO);

        if (getIntent() != null) {
            productId = getIntent().getStringExtra("product_id");

        }


        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        getAllProductsData();


    }


    private void getAllProductsData() {
        param.put(Consts.PRODUCT_ID, productId);
        param.put(Consts.USER_ID, userDTO.getId());
        new HttpsRequest(Consts.ProductDetails_, param, mContext).stringPost("TAG", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        productDetailDTO = new Gson().fromJson(response.getJSONObject("data").toString(), ProductDetailDTO.class);
                        setDATA();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                }


            }
        });


    }


    private void setDATA() {
        GridLayoutManager linearLayoutManager = new GridLayoutManager(mContext, 2);
        binding.rvAllRelatedProduct.setLayoutManager(linearLayoutManager);
        AdapterNearBY adapterNearBY = new AdapterNearBY(mContext, productDetailDTO.getRelated_product(),0);
        binding.rvAllRelatedProduct.setAdapter(adapterNearBY);
    }


}
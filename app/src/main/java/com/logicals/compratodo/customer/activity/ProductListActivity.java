package com.logicals.compratodo.customer.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.logicals.compratodo.R;
import com.logicals.compratodo.databinding.ActivityProductListBinding;
import com.logicals.compratodo.https.HttpsRequest;
import com.logicals.compratodo.interfacess.Consts;
import com.logicals.compratodo.interfacess.Helper;
import com.logicals.compratodo.model.ProductDTO;
import com.logicals.compratodo.model.SubCategoryDTO;
import com.logicals.compratodo.ui.adapter.AdapterProductList;
import org.json.JSONObject;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;


public class ProductListActivity extends AppCompatActivity {
    static String TAG= ProductListActivity.class.getSimpleName();
    CardView productdetails;
    LinearLayout tvSelectedItem;
    SubCategoryDTO subCategoryDTO;
    HashMap<String, String> param = new HashMap<>();
    Context mContext;
    ArrayList<ProductDTO> productDTOS = new ArrayList<>();
    ActivityProductListBinding binding;
    AdapterProductList adapterProductList;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_product_list);
        mContext= ProductListActivity.this;

        if(getIntent().hasExtra(Consts.DTO)){
            subCategoryDTO=(SubCategoryDTO)getIntent().getSerializableExtra(Consts.DTO);
            getProductListByCategory();
        }

        binding.productName.setText(subCategoryDTO.getName());


        binding.tvSelectedItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(mContext, R.style.BottomSheetDialogTheme);
                final  View view = getLayoutInflater().inflate(R.layout.bottom_layout, null);
                bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                view.findViewById(R.id.a_to_z_res).setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onClick(View v) {
                        getProductListByHighToLow();
                        bottomSheetDialog.dismiss();
                    }
                });

                view.findViewById(R.id.by_nearest).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getProductListByLowToHigh();
                        bottomSheetDialog.dismiss();
                    }
                });



                view.findViewById(R.id.most_popular).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        getProductListByNewestFirst();

                        bottomSheetDialog.dismiss();

                    }
                });


                bottomSheetDialog.setContentView(view);
                bottomSheetDialog.show();

            }
        });




    }

    private void getProductListByLowToHigh() {
        param.put(Consts.CATE_ID,subCategoryDTO.getId());
        new HttpsRequest(Consts.PRODUCTLOWTOHigh,param,mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if(flag){
                    try {
                        Type listType = new TypeToken<ArrayList<ProductDTO>>() {}.getType();
                        productDTOS = new Gson().fromJson(response.getJSONArray("data").toString(), listType);
                        binding.lytFilter.setVisibility(View.VISIBLE);
                        GridLayoutManager  gridLayoutManager= new GridLayoutManager(mContext, 2);
                        binding.recycleview.setLayoutManager(gridLayoutManager);
                        adapterProductList = new AdapterProductList(mContext,productDTOS);
                        binding.recycleview.setAdapter(adapterProductList);
                    } catch (Exception e) { }
                }else {
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();

                }
            }
        });
    }





    private void getProductListByNewestFirst() {
        param.put(Consts.CATE_ID,subCategoryDTO.getId());
        new HttpsRequest(Consts.PRODUCTNEWESt,param,mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if(flag){
                    try {
                        Type listType = new TypeToken<ArrayList<ProductDTO>>() {}.getType();
                        productDTOS = new Gson().fromJson(response.getJSONArray("data").toString(), listType);
                        binding.lytFilter.setVisibility(View.VISIBLE);
                        GridLayoutManager  gridLayoutManager= new GridLayoutManager(mContext, 2);
                        binding.recycleview.setLayoutManager(gridLayoutManager);
                        adapterProductList = new AdapterProductList(mContext,productDTOS);
                        binding.recycleview.setAdapter(adapterProductList);
                    } catch (Exception e) { }
                }else {
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();

                }
            }
        });
    }







    private void getProductListByCategory(){
        param.put(Consts.CATE_ID,subCategoryDTO.getId());
        new HttpsRequest(Consts.SUBPRODUCTLIST,param,mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if(flag){
                    try {
                        Type listType = new TypeToken<ArrayList<ProductDTO>>() {}.getType();
                        productDTOS = new Gson().fromJson(response.getJSONArray("data").toString(), listType);
                        binding.lytFilter.setVisibility(View.VISIBLE);
                        GridLayoutManager  gridLayoutManager= new GridLayoutManager(mContext, 2);
                        binding.recycleview.setLayoutManager(gridLayoutManager);
                        adapterProductList = new AdapterProductList(mContext,productDTOS);
                        binding.recycleview.setAdapter(adapterProductList);
                    } catch (Exception e) { }


                }else {
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();

                }
            }
        });
    }




    private void getProductListByHighToLow(){

        param.put(Consts.CATE_ID,subCategoryDTO.getId());

        new HttpsRequest(Consts.PRODUCTHIGHTOLOW,param,mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if(flag){
                    try {
                        Type listType = new TypeToken<ArrayList<ProductDTO>>() {}.getType();
                        productDTOS = new Gson().fromJson(response.getJSONArray("data").toString(), listType);
                        binding.lytFilter.setVisibility(View.VISIBLE);
                        GridLayoutManager  gridLayoutManager= new GridLayoutManager(mContext, 2);
                        binding.recycleview.setLayoutManager(gridLayoutManager);
                        adapterProductList = new AdapterProductList(mContext,productDTOS);
                        binding.recycleview.setAdapter(adapterProductList);
                    } catch (Exception e) { }


                }else {
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();

                }
            }
        });
    }




}
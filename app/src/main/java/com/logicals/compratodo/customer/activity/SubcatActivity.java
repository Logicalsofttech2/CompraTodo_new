package com.logicals.compratodo.customer.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.logicals.compratodo.R;
import com.logicals.compratodo.customer.UI.home.MainActivity2;
import com.logicals.compratodo.customer.mainhomeactivity.UserHomeActivity;
import com.logicals.compratodo.databinding.ActivitySubcatBinding;
import com.logicals.compratodo.https.HttpsRequest;
import com.logicals.compratodo.interfacess.Consts;
import com.logicals.compratodo.interfacess.Helper;
import com.logicals.compratodo.model.CategoryDTO;
import com.logicals.compratodo.model.HomeDTO;
import com.logicals.compratodo.model.MainCategoryDTO;
import com.logicals.compratodo.model.UserDTO;
import com.logicals.compratodo.preferences.SharedPrefrence;
import com.logicals.compratodo.ui.adapter.AdapterCategoryOther;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class SubcatActivity extends AppCompatActivity {
    HashMap<String,String> param=new HashMap<>();
    static String TAG= SubcatActivity.class.getSimpleName();
    ArrayList<CategoryDTO>mainCategoryDTOS= new ArrayList<>();
    CardView subcategorylist;
    LinearLayoutManager linearLayoutManager;
    MainCategoryDTO mainCategoryDTO;
    Context mContext;
    ActivitySubcatBinding binding;
    AdapterCategoryOther adapterCategoryOther;
    UserDTO userDTO;
    HomeDTO homeDTO;
    SharedPrefrence sharedPrefrence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_subcat);
        mContext= SubcatActivity.this;
        sharedPrefrence = SharedPrefrence.getInstance(mContext);
        userDTO = sharedPrefrence.getParentUser(Consts.USER_DTO);

        if(getIntent().hasExtra(Consts.DTO)){
            mainCategoryDTO=(MainCategoryDTO)getIntent().getSerializableExtra(Consts.DTO);
            getCategory();
        }



        binding.NotFoundData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in =new Intent(mContext, CategoriesListActivity.class);
                in.putExtra(Consts.DTO,homeDTO);
                startActivity(in);


            }
        });

        binding.SearchImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MainActivity2.class);
                startActivity(intent);

            }
        });








    }

    private void setData() {

        Glide.with(mContext).load(Consts.USER_IMAGE_URL+mainCategoryDTO.getImage()).into(binding.ivImage);
        binding.tvName.setText(mainCategoryDTO.getName());

    }
    private void getHomeData() {
        param.put(Consts.USER_ID, userDTO.getId());
        new HttpsRequest(Consts.USERHOME, param, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        homeDTO = new Gson().fromJson(response.getJSONObject("data").toString(), HomeDTO.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void getCategory() {
        param.put(Consts.ID,mainCategoryDTO.getId());
        new HttpsRequest(Consts.CATEGORY,param,mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if(flag){

                    try {
                        binding.LinearBannerimg.setVisibility(View.VISIBLE);
                        Type listType = new TypeToken<ArrayList<CategoryDTO>>() {}.getType();
                        mainCategoryDTOS = new Gson().fromJson(response.getJSONArray("data").toString(), listType);
                        setList();
                        setData();

                    } catch (Exception e) {
                    }


                }else {
                    binding.LinearBannerimg.setVisibility(View.GONE);
                    getHomeData();
                    binding.LinearDataNotFound.setVisibility(View.VISIBLE);

                  //  Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void setList() {
        linearLayoutManager= new LinearLayoutManager(mContext, RecyclerView.VERTICAL,false);
        binding.rvCategory.setLayoutManager(linearLayoutManager);
        adapterCategoryOther = new AdapterCategoryOther(mContext,mainCategoryDTOS);
        binding.rvCategory.setAdapter(adapterCategoryOther);
    }



}
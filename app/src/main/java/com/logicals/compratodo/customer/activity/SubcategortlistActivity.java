package com.logicals.compratodo.customer.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.logicals.compratodo.R;
import com.logicals.compratodo.databinding.ActivitySubcategortlistBinding;
import com.logicals.compratodo.https.HttpsRequest;
import com.logicals.compratodo.interfacess.Consts;
import com.logicals.compratodo.interfacess.Helper;
import com.logicals.compratodo.model.CategoryDTO;
import com.logicals.compratodo.model.HomeDTO;
import com.logicals.compratodo.model.SubCategoryDTO;
import com.logicals.compratodo.model.UserDTO;
import com.logicals.compratodo.preferences.SharedPrefrence;
import com.logicals.compratodo.ui.adapter.AdapterSubCategory;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class SubcategortlistActivity extends AppCompatActivity {

    CardView av_productlist;
    ActivitySubcategortlistBinding binding;
    static String TAG = SubcategortlistActivity.class.getSimpleName();
    HashMap<String, String> param = new HashMap<>();
    GridLayoutManager gridLayoutManager;
    Context mContext;
    CategoryDTO mainCategoryDTO;
    AdapterSubCategory adapterCategory;
    ArrayList<SubCategoryDTO> categoryDTOS = new ArrayList<>();
    UserDTO userDTO;
    HomeDTO homeDTO;
    SharedPrefrence sharedPrefrence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_subcategortlist);
        mContext = SubcategortlistActivity.this;
        sharedPrefrence = SharedPrefrence.getInstance(SubcategortlistActivity.this);
        userDTO = sharedPrefrence.getParentUser(Consts.USER_DTO);


        if (getIntent().hasExtra(Consts.DTO)) {
            mainCategoryDTO = (CategoryDTO) getIntent().getSerializableExtra(Consts.DTO);
            getSubCategory();
        }


        binding.LinearSeeCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in =new Intent(SubcategortlistActivity.this, CategoriesListActivity.class);
                in.putExtra(Consts.DTO,homeDTO);
                startActivity(in);
              //  startActivity(new Intent(SubcategortlistActivity.this, CategoriesListActivity.class));
            }
        });


        getHomeData();


    }


    private void getHomeData() {
        param.put(Consts.USER_ID, userDTO.getId());
        new HttpsRequest(Consts.USERHOME, param, SubcategortlistActivity.this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        homeDTO = new Gson().fromJson(response.getJSONObject("data").toString(), HomeDTO.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(SubcategortlistActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }




    private void getSubCategory() {
        param.put(Consts.ID, mainCategoryDTO.getId());
        new HttpsRequest(Consts.SUB_CATEGORY, param, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        Type listType = new TypeToken<ArrayList<SubCategoryDTO>>() {
                        }.getType();
                        categoryDTOS = new Gson().fromJson(response.getJSONArray("data").toString(), listType);
                        setData();

                    } catch (Exception e) {
                    }


                } else {
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void setData() {
        gridLayoutManager = new GridLayoutManager(mContext, 2, RecyclerView.VERTICAL, false);
        binding.rvSubCategory.setLayoutManager(gridLayoutManager);
        adapterCategory = new AdapterSubCategory(mContext, categoryDTOS);
        binding.rvSubCategory.setAdapter(adapterCategory);

    }

}
package com.logicals.compratodo.customer.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import com.logicals.compratodo.R;
import com.logicals.compratodo.databinding.ActivityCategoriesListBinding;
import com.logicals.compratodo.interfacess.Consts;
import com.logicals.compratodo.model.HomeDTO;
import com.logicals.compratodo.ui.adapter.AdapterAllCategory;

import java.util.HashMap;

public class CategoriesListActivity extends AppCompatActivity {

    HomeDTO homeDTO;
    ActivityCategoriesListBinding binding;
    static String TAG= SubcategortlistActivity.class.getSimpleName();
    HashMap<String, String> param = new HashMap<>();
    GridLayoutManager gridLayoutManager;
    Context mContext;
    AdapterAllCategory adapterCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_categories_list);
        mContext=CategoriesListActivity.this;
        if (getIntent().hasExtra(Consts.DTO)) {
            homeDTO=(HomeDTO) getIntent().getSerializableExtra(Consts.DTO);
            setData();
        }


    }

    private void setData() {

        gridLayoutManager= new GridLayoutManager(mContext,2, RecyclerView.VERTICAL,false);
        binding.rvAllCat.setLayoutManager(gridLayoutManager);
        adapterCategory = new AdapterAllCategory(mContext,homeDTO.getMaincate());
        binding.rvAllCat.setAdapter(adapterCategory);


    }


}
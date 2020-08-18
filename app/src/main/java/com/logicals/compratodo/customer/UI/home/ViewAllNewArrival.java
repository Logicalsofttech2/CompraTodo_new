package com.logicals.compratodo.customer.UI.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.google.gson.Gson;
import com.logicals.compratodo.R;

import com.logicals.compratodo.customer.adapter.AdapterNewArrivalGrid;
import com.logicals.compratodo.databinding.ActivityViewAllNewArrivalBinding;
import com.logicals.compratodo.https.HttpsRequest;
import com.logicals.compratodo.interfacess.Consts;
import com.logicals.compratodo.interfacess.Helper;
import com.logicals.compratodo.model.HomeDTO;
import org.json.JSONObject;

public class ViewAllNewArrival extends AppCompatActivity {
    Context mContext;
    AdapterNewArrivalGrid adapterNewArrival;
    LinearLayoutManager linearLayoutManager;
    ActivityViewAllNewArrivalBinding binding;
    static String TAG= ViewAllNearProduct.class.getSimpleName();
    HomeDTO homeDTO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_new_arrival);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_all_new_arrival);
        mContext = ViewAllNewArrival.this;

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        getHomeData();


    }

    private void getHomeData() {
        new HttpsRequest(Consts.USERHOME,mContext).stringGet(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if(flag){
                    try {
                        homeDTO= new Gson().fromJson(response.getJSONObject("data").toString(), HomeDTO.class); }
                    catch (Exception e){ e.printStackTrace(); }
                    setDATA();
                }else {
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setDATA() {

        GridLayoutManager gridLayoutManager=new GridLayoutManager(mContext, 2);
       // linearLayoutManager= new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL,false);
        binding.rvNewArrival.setLayoutManager(gridLayoutManager);
        adapterNewArrival = new AdapterNewArrivalGrid(mContext,homeDTO.getNew_arrival());
        binding.rvNewArrival.setAdapter(adapterNewArrival);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
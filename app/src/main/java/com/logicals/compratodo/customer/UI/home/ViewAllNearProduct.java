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
import com.logicals.compratodo.customer.adapter.AdapterNearBYGrid;
import com.logicals.compratodo.databinding.ActivityViewAllNearProductBinding;
import com.logicals.compratodo.https.HttpsRequest;
import com.logicals.compratodo.interfacess.Consts;
import com.logicals.compratodo.interfacess.Helper;
import com.logicals.compratodo.model.HomeDTO;
import com.logicals.compratodo.model.UserDTO;
import org.json.JSONObject;
import java.util.HashMap;

public class ViewAllNearProduct extends AppCompatActivity {
ActivityViewAllNearProductBinding binding;
Context mContext;
    UserDTO userDTO;
    HomeDTO homeDTO;
    static String TAG= ViewAllNearProduct.class.getSimpleName();
    HashMap<String,String> param=new HashMap<>();
    AdapterNearBYGrid adapterNearBY;
    LinearLayoutManager linearLayoutManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_all_near_product);
        mContext = ViewAllNearProduct.this;


        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

      getHomeData();


    }
    private void getHomeData() {
      //  param.put(Consts.USER_ID, userDTO.getId());

        new HttpsRequest(Consts.USERHOME, mContext).stringGet(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        homeDTO = new Gson().fromJson(response.getJSONObject("data").toString(), HomeDTO.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    setDATA("grid_layout");
                } else {
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                }


            }
        });


    }



        private void setDATA(String gridLayout) {

            GridLayoutManager gridLayoutManager=new GridLayoutManager(mContext, 2);
          //  linearLayoutManager= new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL,false);
            binding.rvNearByProduct.setLayoutManager(gridLayoutManager);
            adapterNearBY = new AdapterNearBYGrid(mContext,homeDTO.getNearbyproduct());
            binding.rvNearByProduct.setAdapter(adapterNearBY);

        }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
package com.logicals.compratodo.customer.UI.home;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.logicals.compratodo.R;
import com.logicals.compratodo.customer.adapter.AdapterNearBYGrid;
import com.logicals.compratodo.databinding.ActivitySearchableListBinding;
import com.logicals.compratodo.https.HttpsRequest;
import com.logicals.compratodo.interfacess.Consts;
import com.logicals.compratodo.interfacess.Helper;
import com.logicals.compratodo.model.ProductDTO;
import com.logicals.compratodo.model.UserDTO;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;


public class SearchableListActivity extends AppCompatActivity {

    ActivitySearchableListBinding binding;
    Context mContext;
    UserDTO userDTO;

    static String TAG= SearchableListActivity.class.getSimpleName();
    HashMap<String,String> param=new HashMap<>();
    AdapterNearBYGrid adapterNearBY;
    LinearLayoutManager linearLayoutManager;
    String product_name;

    ArrayList<ProductDTO>productDTO= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_searchable_list);
        mContext = SearchableListActivity.this;

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (getIntent()!=null)
        {
         product_name=getIntent().getStringExtra("product_name");
         binding.tvHeaderComment.setText(product_name);
         getHomeData(product_name);
        }


    }

    private void getHomeData(String product_name) {
        param.put(Consts.NAME, product_name);
        new HttpsRequest(Consts.SEARCHPRODUCT,param,mContext).stringPost("TAG", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if(flag){
                    try {
                        Type listType = new TypeToken<ArrayList<ProductDTO>>() {}.getType();
                        productDTO = new Gson().fromJson(response.getJSONArray("data").toString(), listType);
                        setDATA("grid_layout");
                    } catch (Exception e) {
                    }


                }else {
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }




















    private void setDATA(String gridLayout) {

        GridLayoutManager gridLayoutManager=new GridLayoutManager(mContext, 2);
        //  linearLayoutManager= new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL,false);
        binding.rvNearByProduct.setLayoutManager(gridLayoutManager);
        adapterNearBY = new AdapterNearBYGrid(mContext,productDTO);
        binding.rvNearByProduct.setAdapter(adapterNearBY);

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}


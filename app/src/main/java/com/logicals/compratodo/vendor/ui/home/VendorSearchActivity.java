package com.logicals.compratodo.vendor.ui.home;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.logicals.compratodo.R;
import com.logicals.compratodo.databinding.ActivityVendorSearchBinding;
import com.logicals.compratodo.https.HttpsRequest;
import com.logicals.compratodo.interfacess.Consts;
import com.logicals.compratodo.interfacess.Helper;
import com.logicals.compratodo.model.UserDTO;
import com.logicals.compratodo.preferences.SharedPrefrence;
import com.logicals.compratodo.vendor.adapter.AdapterMyOrderList;
import com.logicals.compratodo.vendor.model.MyProductListDTO;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class VendorSearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    ActivityVendorSearchBinding binding;
    HashMap<String, String> param = new HashMap<>();
    SharedPrefrence sharedPrefrence;
    UserDTO userDTO;
    ArrayList<MyProductListDTO>  myProductListDTO=new ArrayList<>();
    Context mContext;
    AdapterMyOrderList adapterMyOrderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_vendor_search);
        mContext=VendorSearchActivity.this;
        sharedPrefrence= SharedPrefrence.getInstance(mContext);
        userDTO = sharedPrefrence.getParentUser(Consts.USER_DTO);


        binding.searchView.setQueryHint("Search By Customer Name");
        binding.searchView.setOnQueryTextListener(this);
        binding.searchView.setIconified(false);


        getMYorderList();
    }
    private void getMYorderList() {

        param.put(Consts.USER_ID,userDTO.getId());
        new HttpsRequest(Consts.MYORDERlIST,param,mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if(flag){
                    try {
                        Type listType = new TypeToken<ArrayList<MyProductListDTO>>() {}.getType();
                        myProductListDTO = new Gson().fromJson(response.getJSONArray("data").toString(), listType);
                        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(mContext, RecyclerView.VERTICAL,false);
                        binding.recyclerview.setLayoutManager(linearLayoutManager);
                        adapterMyOrderList = new AdapterMyOrderList(mContext,myProductListDTO);
                      //  binding.recyclerview.setAdapter(adapterMyOrderList);
                    }
                    catch (Exception e) {

                    }
                }else {
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }









    @Override
    public boolean onQueryTextSubmit(String s) {
        return true;
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onQueryTextChange(String s) {
        ArrayList<MyProductListDTO> myProductListDTOArrayList = new ArrayList<>();
        for (MyProductListDTO apoint_user_model_data : myProductListDTO) {
            String name = apoint_user_model_data.getUsername().toLowerCase();
            if (name.contains(s))
                myProductListDTOArrayList.add(apoint_user_model_data);
        }
        try {
            adapterMyOrderList.setFilter(myProductListDTOArrayList);
            binding.recyclerview.setAdapter(adapterMyOrderList);
        } catch (Exception e) {
            // Toast.makeText(getActivity(), "No Data Available ", Toast.LENGTH_SHORT).show();
        }
        return true;
    }










}
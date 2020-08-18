package com.logicals.compratodo.customer.UI.completeorders;

import android.content.Context;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.logicals.compratodo.R;
import com.logicals.compratodo.customer.adapter.MYorderAdapter;
import com.logicals.compratodo.customer.model.MyOrderDTO;
import com.logicals.compratodo.databinding.FragmentCompleteOrderListBinding;
import com.logicals.compratodo.https.HttpsRequest;
import com.logicals.compratodo.interfacess.Consts;
import com.logicals.compratodo.interfacess.Helper;
import com.logicals.compratodo.model.UserDTO;
import com.logicals.compratodo.preferences.SharedPrefrence;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class CompleteOrderListFragment extends Fragment {

    FragmentCompleteOrderListBinding binding;
    TextView order_details;
    RecyclerView recyclerView;
    Context mContext;
    UserDTO userDTO;
    ArrayList<MyOrderDTO> myOrderDTOS = new ArrayList<>();
    HashMap<String, String> param = new HashMap<>();
    MYorderAdapter myorderAdapter;
    SharedPrefrence sharedPrefrence;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_complete_order_list, container, false);
        mContext = getActivity();
        sharedPrefrence= SharedPrefrence.getInstance(mContext);
        userDTO=sharedPrefrence.getParentUser(Consts.USER_DTO);
        get_Pending_List();
        return binding.getRoot();
    }

    private void get_Pending_List() {
        param.put(Consts.USER_ID, userDTO.getId());
        new HttpsRequest(Consts.COMPLETE_ORDER, param, mContext).stringPost("TAG", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        Type listType = new TypeToken<ArrayList<MyOrderDTO>>() {}.getType();
                        myOrderDTOS = new Gson().fromJson(response.getJSONArray("data").toString(), listType);
                        setList();
                    } catch (Exception e) {
                    }


                } else {
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setList() {
        //  binding.tvOrder.setVisibility(View.VISIBLE);
        LinearLayoutManager gridLayoutManager= new LinearLayoutManager(mContext, RecyclerView.VERTICAL,false);
        binding.recycleview.setLayoutManager(gridLayoutManager);
        myorderAdapter = new MYorderAdapter(mContext,myOrderDTOS,"reorder");
        binding.recycleview.setAdapter(myorderAdapter);
    }





}
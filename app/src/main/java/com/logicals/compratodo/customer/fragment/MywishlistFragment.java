package com.logicals.compratodo.customer.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.logicals.compratodo.customer.activity.ProductDetailsActivity;
import com.logicals.compratodo.R;
import com.logicals.compratodo.customer.adapter.AdapterProductReview;
import com.logicals.compratodo.customer.adapter.AdapterWishList;
import com.logicals.compratodo.customer.model.ProductDetailDTO;
import com.logicals.compratodo.databinding.FragmentMywishlistBinding;
import com.logicals.compratodo.https.HttpsRequest;
import com.logicals.compratodo.interfacess.Consts;
import com.logicals.compratodo.interfacess.Helper;
import com.logicals.compratodo.model.ProductDTO;
import com.logicals.compratodo.model.UserDTO;
import com.logicals.compratodo.preferences.SharedPrefrence;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;


public class MywishlistFragment extends Fragment {
    SharedPrefrence sharedPrefrence;
    UserDTO userDTO;
    CardView productdetail;
    AdapterWishList adapterWishList;
    FragmentMywishlistBinding binding;
    HashMap<String, String> param = new HashMap<>();
    ArrayList<ProductDTO>productDTO= new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_mywishlist, container, false);


        sharedPrefrence = SharedPrefrence.getInstance(getActivity());
        userDTO = sharedPrefrence.getParentUser(Consts.USER_DTO);

        getWishList();

        return binding.getRoot();
    }

    private void getWishList() {

        param.put(Consts.USER_ID, userDTO.getId());
        new HttpsRequest(Consts.FAVLIST, param, getActivity()).stringPost("TAG", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        Type listType = new TypeToken<ArrayList<ProductDTO>>() {}.getType();
                        productDTO = new Gson().fromJson(response.getJSONArray("data").toString(), listType);

                        setDATA();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                }


            }
        });

}

    private void setDATA() {
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        binding.RvWishList.setLayoutManager(linearLayoutManager2);
        adapterWishList = new AdapterWishList(getActivity(), productDTO);
        binding.RvWishList.setAdapter(adapterWishList);
    }


}
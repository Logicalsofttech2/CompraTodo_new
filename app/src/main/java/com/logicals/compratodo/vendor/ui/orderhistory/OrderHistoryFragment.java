package com.logicals.compratodo.vendor.ui.orderhistory;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.logicals.compratodo.R;
import com.logicals.compratodo.https.HttpsRequest;
import com.logicals.compratodo.interfacess.Consts;
import com.logicals.compratodo.interfacess.Helper;
import com.logicals.compratodo.model.UserDTO;
import com.logicals.compratodo.preferences.SharedPrefrence;
import com.logicals.compratodo.vendor.model.MyProductListDTO;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import static android.content.ContentValues.TAG;


public class OrderHistoryFragment extends Fragment {




    RecyclerView recyclerView;
    HashMap<String, String> param = new HashMap<>();
    SharedPrefrence sharedPrefrence;
    UserDTO userDTO;
    ArrayList<MyProductListDTO> myProductListDTO = new ArrayList<>();
    private ShimmerFrameLayout mShimmerViewContainer;
    AdapterOrderHistory adapterOrderHistory;

    public OrderHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notifications2, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        sharedPrefrence = SharedPrefrence.getInstance(getActivity());
        userDTO = sharedPrefrence.getParentUser(Consts.USER_DTO);
        Log.e("check userid", userDTO.getUser_id());
        mShimmerViewContainer = view.findViewById(R.id.shimmer_view_container);
        get_Order_History();
        return view;
    }

    private void get_Order_History() {
        param.put(Consts.USER_ID, userDTO.getId());
        new HttpsRequest(Consts.VENDOR_ORDER_HISTORY, param, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        Type listType = new TypeToken<ArrayList<MyProductListDTO>>() {}.getType();
                        myProductListDTO = new Gson().fromJson(response.getJSONArray("data").toString(), listType);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        adapterOrderHistory = new AdapterOrderHistory(getActivity(), myProductListDTO);
                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        recyclerView.setAdapter(adapterOrderHistory);

                    } catch (Exception e) {
                    }
                } else {
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        mShimmerViewContainer.stopShimmerAnimation();
        super.onPause();
    }
}

package com.logicals.compratodo.vendor.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;




import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.logicals.compratodo.R;
import com.logicals.compratodo.databinding.FragmentHomeBinding;
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

public class HomeFragment extends Fragment {



    AdapterMyOrderList adapterMyOrderList;
    HashMap<String, String> param = new HashMap<>();
    SharedPrefrence sharedPrefrence;
    UserDTO userDTO;
    ArrayList<MyProductListDTO>  myProductListDTO=new ArrayList<>();
    FragmentHomeBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home, container, false);
        //  catlog = root.findViewById (R.id.catlog);
        sharedPrefrence= SharedPrefrence.getInstance(getActivity());
        userDTO = sharedPrefrence.getParentUser(Consts.USER_DTO);
        Log.e("check userid",userDTO.getUser_id());
        binding.SearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),VendorSearchActivity.class));
            }
        });
        getMYorderList();
        return binding.getRoot();
    }

    private void getMYorderList() {
    param.put(Consts.USER_ID,userDTO.getId());
            new HttpsRequest(Consts.MYORDERlIST,param,getActivity()).stringPost(TAG, new Helper() {
                @Override
                public void backResponse(boolean flag, String msg, JSONObject response) {
                    if(flag){
                        try {
                            Type listType = new TypeToken<ArrayList<MyProductListDTO>>() {}.getType();
                            myProductListDTO = new Gson().fromJson(response.getJSONArray("data").toString(), listType);
                            LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,false);
                            binding.recycleview.setLayoutManager(linearLayoutManager);
                            adapterMyOrderList = new AdapterMyOrderList(getActivity(),myProductListDTO);
                            binding.recycleview.setAdapter(adapterMyOrderList);
                        }
                        catch (Exception e) { }
                    }else {
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }


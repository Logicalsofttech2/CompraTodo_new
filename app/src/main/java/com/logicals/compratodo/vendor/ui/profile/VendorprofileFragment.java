package com.logicals.compratodo.vendor.ui.profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.logicals.compratodo.customer.activity.CatlogActivity;
import com.logicals.compratodo.R;
import com.logicals.compratodo.databinding.FragmentVendorprofileBinding;
import com.logicals.compratodo.https.HttpsRequest;
import com.logicals.compratodo.interfacess.Consts;
import com.logicals.compratodo.interfacess.Helper;
import com.logicals.compratodo.login_signup.ChooseRoleActivity;
import com.logicals.compratodo.model.UserDTO;
import com.logicals.compratodo.preferences.SharedPrefrence;
import com.logicals.compratodo.utils.ProjectUtils;

import org.json.JSONObject;

import java.util.HashMap;

public class VendorprofileFragment extends Fragment {
    FragmentVendorprofileBinding binding;
    LinearLayout catlog;

    private static String TAG = VendorprofileFragment.class.getSimpleName();
    HashMap<String, String> param = new HashMap<>();
    SharedPrefrence sharedPrefrence;
    UserDTO userDTO;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate( inflater,R.layout.fragment_vendorprofile, container, false);

        sharedPrefrence= SharedPrefrence.getInstance(getActivity());
        userDTO=sharedPrefrence.getParentUser(Consts.USER_DTO);
        param.put(Consts.USER_ID,userDTO.getId());


        init();
        getProfileData();
        return binding.getRoot();


    }

    private void init() {
        binding.profiledetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Vendorprofiledetails.class)

                );

            }
        });





        binding.llChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), VendorChangePassword.class);
                in.putExtra(Consts.EMAIL,userDTO.getEmail());
                startActivity(in);
            }
        });


        binding.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), Vendorprofiledetails.class);
                startActivity(in);
            }
        });

        binding.catlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), CatlogActivity.class);
                startActivity(in);
            }
        });

        binding.llLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });
    }

    private void logOut() {
        new HttpsRequest(Consts.LOGOUT, param, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    
                    sharedPrefrence.setLogin(false);
                    Intent showLogin = new Intent(getActivity(), ChooseRoleActivity.class);
                    showLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    showLogin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    showLogin.putExtra("intent","login");
                    startActivity(showLogin);

                } else {
                    ProjectUtils.showToast(getActivity(),msg);
                }
            }
        });
    }

    private void getProfileData() {
        new HttpsRequest(Consts.GET_USER_PROFILE, param, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {

                    try {
                        userDTO = new Gson().fromJson(response.getJSONObject("data").toString(), UserDTO.class);
                        setdata();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    sharedPrefrence.setParentUser(userDTO,Consts.USER_DTO);
                } else {
                    ProjectUtils.showToast(getActivity(),msg);
                }
            }
        });

    }




    private void setdata() {
        binding.tvName.setText(userDTO.getName());
        if (getActivity()!=null){
            Glide.with(getActivity())
                    .load(Consts.USER_IMAGE_URL+userDTO.getImage())
                    .error(R.drawable.logo)
                    .into(binding.civProfilePic); }


    }


    @Override
    public void onResume() {
        super.onResume();
        getProfileData();
    }
}
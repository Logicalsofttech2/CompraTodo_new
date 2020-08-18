package com.logicals.compratodo.profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.logicals.compratodo.R;
import com.logicals.compratodo.databinding.FragmentProfileBinding;
import com.logicals.compratodo.https.HttpsRequest;
import com.logicals.compratodo.interfacess.Consts;
import com.logicals.compratodo.interfacess.Helper;
import com.logicals.compratodo.model.UserDTO;
import com.logicals.compratodo.preferences.SharedPrefrence;
import com.logicals.compratodo.vendor.ui.profile.ChangePassword;
import com.logicals.compratodo.utils.ProjectUtils;

import org.json.JSONObject;

import java.util.HashMap;

public class ProfileFragment extends Fragment {
    private static String TAG = ProfileFragment.class.getSimpleName();
    FragmentProfileBinding binding;
    HashMap<String, String> param = new HashMap<>();
    SharedPrefrence sharedPrefrence;
    UserDTO userDTO;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);

        sharedPrefrence=SharedPrefrence.getInstance(getActivity());
        userDTO=sharedPrefrence.getParentUser(Consts.USER_DTO);


        binding.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(),UpdateProfile.class);
                in.putExtra(Consts.SCREEN_TAG,"1");
                startActivity(in);
            }
        });
        binding.llChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), ChangePassword.class);
                in.putExtra(Consts.EMAIL,userDTO.getEmail());
                startActivity(in);
            }
        });
        param.put(Consts.USER_ID,userDTO.getId());
        getProfileData();

        return binding.getRoot();


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
        binding.tvEmail.setText(userDTO.getEmail());
        binding.tvName.setText(userDTO.getName());
        binding.tvPhoneNumber.setText(userDTO.getMobile());

        Glide.with(getActivity())
                .load(Consts.USER_IMAGE_URL+userDTO.getImage())
                .error(R.drawable.logo)
                .into(binding.civProfilePic);

    }

    @Override
    public void onResume() {
        super.onResume();
        getProfileData();
    }
}
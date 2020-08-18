package com.logicals.compratodo.customer.UI.about;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.logicals.compratodo.R;
import com.logicals.compratodo.databinding.FragmentAboutBinding;
import com.logicals.compratodo.https.HttpsRequest;
import com.logicals.compratodo.interfacess.Consts;
import com.logicals.compratodo.interfacess.Helper;
import com.logicals.compratodo.model.HomeDTO;

import org.json.JSONObject;

import static android.content.ContentValues.TAG;


public class AboutFragment extends Fragment {

FragmentAboutBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
     binding= DataBindingUtil.inflate(inflater,R.layout.fragment_about, container, false);


about();






    return  binding.getRoot();
    }



    public  void about(){
        new HttpsRequest(Consts.ABOUTUS, getActivity()).stringGet(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {

                        JSONObject jsonObject=response.getJSONObject("data");


                        String text=jsonObject.getString("about_us");


                        Log.e("settext",text);


                        binding.abouttext.setText(text);


                  //      homeDTO = new Gson().fromJson(response.getJSONObject("data").toString(), HomeDTO.class);



                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                }


            }
        });
    }





}
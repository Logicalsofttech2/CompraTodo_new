package com.logicals.compratodo.customer.UI.terms;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.logicals.compratodo.R;
import com.logicals.compratodo.databinding.FragmentTermsBinding;
import com.logicals.compratodo.https.HttpsRequest;
import com.logicals.compratodo.interfacess.Consts;
import com.logicals.compratodo.interfacess.Helper;

import org.json.JSONObject;

import static android.content.ContentValues.TAG;

public class TermsFragment extends Fragment {

    FragmentTermsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_terms, container, false);


        Terms();


        return binding.getRoot();
    }


    public void Terms() {
        new HttpsRequest(Consts.TERMS, getActivity()).stringGet(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {

                        JSONObject jsonObject = response.getJSONObject("data");


                        String text = jsonObject.getString("terms");


                        Log.e("settext", text);


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

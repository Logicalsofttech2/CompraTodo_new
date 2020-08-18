package com.logicals.compratodo.customer.UI.help;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.logicals.compratodo.R;
import com.logicals.compratodo.customer.mainhomeactivity.UserHomeActivity;
import com.logicals.compratodo.https.HttpsRequest;
import com.logicals.compratodo.interfacess.Consts;
import com.logicals.compratodo.interfacess.Helper;
import com.logicals.compratodo.model.UserDTO;
import com.logicals.compratodo.preferences.SharedPrefrence;
import com.logicals.compratodo.utils.ProjectUtils;

import org.json.JSONObject;

import java.util.HashMap;

public class HelpFragment extends Fragment {

EditText et_phoneno,et_description;
TextView iv_save;
HashMap<String, String> param = new HashMap<>();
View view;
SharedPrefrence sharedPreferences;
UserDTO userDTO;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_help, container, false);

        sharedPreferences = SharedPrefrence.getInstance(getActivity());
        userDTO = sharedPreferences.getParentUser(Consts.USER_DTO);

        iv_save=view.findViewById(R.id.iv_save);
        et_phoneno=view.findViewById(R.id.et_phoneno);
        et_description=view.findViewById(R.id.et_description);






        iv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             if (et_phoneno.getText().toString().equals("")){
                 et_phoneno.setError("please enter phone no.");
             }else if (et_description.getText().toString().equals("")){
                 et_description.setError("please enter your question");
             }else {

                 SubmitQuestion();

             }


            }
        });





        return view;
    }

    private void SubmitQuestion() {

        param.put(Consts.MOBILE,et_phoneno.getText().toString());
        param.put(Consts.MSG,et_description.getText().toString());
        param.put(Consts.USER_ID,userDTO.getId());


        new HttpsRequest(Consts.HELP, param, getActivity()).stringPost("TAG", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    Toast.makeText(getActivity(), "Your Query Has Been Sent", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), UserHomeActivity.class));
                } else {
                    ProjectUtils.showToast(getActivity(),msg);
                }
            }
        });




    }











}
package com.logicals.compratodo.customer.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.logicals.compratodo.R;
import com.logicals.compratodo.databinding.ActivityPaymentBinding;
import com.logicals.compratodo.databinding.ActivityREPaymentBinding;
import com.logicals.compratodo.https.HttpsRequest;
import com.logicals.compratodo.interfacess.Consts;
import com.logicals.compratodo.interfacess.Helper;
import com.logicals.compratodo.model.UserDTO;
import com.logicals.compratodo.preferences.SharedPrefrence;
import com.logicals.compratodo.preferences.Shared_Pref;

import org.json.JSONObject;

import java.util.HashMap;

public class REPaymentActivity extends AppCompatActivity {
    ActivityREPaymentBinding binding;
    Context mContext;
    String product_id;
    SharedPrefrence sharedPrefrence;
    UserDTO userDTO;
    String address,order_id;
    String final_amount;
    HashMap<String, String> param = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   setContentView(R.layout.activity_r_e_payment);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_r_e_payment);
        mContext = REPaymentActivity.this;
        product_id = Shared_Pref.getProductId(mContext);
        sharedPrefrence = SharedPrefrence.getInstance(mContext);
        userDTO = sharedPrefrence.getParentUser(Consts.USER_DTO);

        if (getIntent() != null) {
            final_amount = getIntent().getStringExtra(Consts.FINAL_AMOUNT);
            address = getIntent().getStringExtra(Consts.ADDRESS);
            binding.finalAmount.setText("USD " + final_amount);
            order_id = getIntent().getStringExtra(Consts.ORDER_ID);
        }


        binding.LinCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                place_order_api("Cash");

                //   Toast.makeText(mContext, ""+product_id+" "+userDTO.getId()+" "+address+" "+"paymentmode"+" "+final_amount+" "+"1", Toast.LENGTH_SHORT).show();


            }
        });


        binding.LinOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                place_order_api("Online");


            }
        });

    }

    private void place_order_api(String paymentmode) {
        param.put(Consts.ORDER_ID, order_id);
        param.put(Consts.USER_ID, userDTO.getId());
        param.put(Consts.ADDRESS_ID, address);
        param.put(Consts.Payment_Method, paymentmode);
       // param.put(Consts.TOTALAMOUNT, final_amount);
       // param.put(Consts.QUANTITY, "1");

        new HttpsRequest(Consts.RE_ORDER, param, mContext).stringPost("TAG", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        startActivity(new Intent(REPaymentActivity.this, ConfirmationActivity.class));
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}
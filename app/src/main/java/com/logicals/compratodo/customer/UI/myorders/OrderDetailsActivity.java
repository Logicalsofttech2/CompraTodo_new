package com.logicals.compratodo.customer.UI.myorders;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.logicals.compratodo.R;
import com.logicals.compratodo.customer.adapter.AdapterOrderDetail;
import com.logicals.compratodo.customer.model.OrderDetailDTo;
import com.logicals.compratodo.databinding.ActivityOrderDetailsBinding;
import com.logicals.compratodo.https.HttpsRequest;
import com.logicals.compratodo.interfacess.Consts;
import com.logicals.compratodo.interfacess.Helper;
import com.logicals.compratodo.model.UserDTO;
import com.logicals.compratodo.preferences.SharedPrefrence;

import org.json.JSONObject;

import java.util.HashMap;

public class OrderDetailsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ActivityOrderDetailsBinding binding;
    String order_id;
    SharedPrefrence sharedPrefrence;
    Context mContext;
    UserDTO userDTO;
    AdapterOrderDetail adapterOrderDetail;
    OrderDetailDTo orderDetailDTo;
    HashMap<String, String> param = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_details);
        sharedPrefrence = SharedPrefrence.getInstance(mContext);
        userDTO = sharedPrefrence.getParentUser(Consts.USER_DTO);

        if (getIntent() != null) {
            order_id = getIntent().getStringExtra("order_id");
        }


        getOrderDetails(order_id);
    }


    private void getOrderDetails(String order_id) {
        param.put(Consts.USER_ID, userDTO.getId());
        param.put(Consts.ORDER_ID, order_id);
        new HttpsRequest(Consts.ORDER_DETAIL, param, mContext).stringPost("TAG", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        orderDetailDTo = new Gson().fromJson(response.getJSONObject("data").toString(), OrderDetailDTo.class);
                        setData();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();

                }


            }
        });


    }

    private void setData() {





        if (orderDetailDTo.getOrder_status().equals("1")) {
           binding.preparingImg.setImageResource(R.drawable.ic_check_green);
        } else if (orderDetailDTo.getOrder_status().equals("2")) {
            binding.preparingImg.setImageResource(R.drawable.ic_check_green);
            binding.readyImg.setImageResource(R.drawable.ic_green_ready);
        } else if (orderDetailDTo.getOrder_status().equals("3")) {
            binding.preparingImg.setImageResource(R.drawable.ic_check_green);
            binding.readyImg.setImageResource(R.drawable.ic_green_ready);
            binding.bikeImg.setImageResource(R.drawable.ic_bike_ready);
        } else if (orderDetailDTo.getOrder_status().equals("4")) {
            binding.preparingImg.setImageResource(R.drawable.ic_check_green);
            binding.readyImg.setImageResource(R.drawable.ic_green_ready);
            binding.bikeImg.setImageResource(R.drawable.ic_bike_ready);
            binding.courierImg.setImageResource(R.drawable.ic_green_courier);
        }else if (orderDetailDTo.getOrder_status().equals("5")){
            binding.statusLinear.setVisibility(View.GONE);
        }


        binding.orderId.setText(orderDetailDTo.getOrder_id());
        binding.orderDate.setText(orderDetailDTo.getOrder_date());
        binding.deliveryFee.setText(orderDetailDTo.getDelivery_fee());
        binding.tvtotallamount.setText(orderDetailDTo.getTotal_amount());
        binding.paymentType.setText(orderDetailDTo.getPayment_method());


        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        binding.recyclerview.setLayoutManager(gridLayoutManager);
        adapterOrderDetail = new AdapterOrderDetail(mContext, orderDetailDTo.getPendingProductDtos());
        binding.recyclerview.setAdapter(adapterOrderDetail);
    }


}
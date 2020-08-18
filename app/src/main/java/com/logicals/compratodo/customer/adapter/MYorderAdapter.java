package com.logicals.compratodo.customer.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.logicals.compratodo.R;
import com.logicals.compratodo.customer.activity.PlaceorderActivity;
import com.logicals.compratodo.customer.model.MyOrderDTO;
import com.logicals.compratodo.databinding.AdapterMyOrderListBinding;
import com.logicals.compratodo.https.HttpsRequest;
import com.logicals.compratodo.interfacess.Consts;
import com.logicals.compratodo.interfacess.Helper;
import com.logicals.compratodo.model.UserDTO;
import com.logicals.compratodo.preferences.SharedPrefrence;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;


public class MYorderAdapter extends RecyclerView.Adapter<MYorderAdapter.MyViewHolder> {
    LayoutInflater inflater;
    AdapterMyOrderListBinding binding;
    Context mContext;
    ArrayList<MyOrderDTO> productDTOS = new ArrayList<>();
    int limit;
    HashMap<String, String> param = new HashMap<>();
    UserDTO userDTO;
    String status;
    SharedPrefrence sharedPrefrence;
    public MYorderAdapter(Context mContext, ArrayList<MyOrderDTO> productDTOS,String status) {
        this.mContext = mContext;
        this.productDTOS = productDTOS;
        this.status=status;
    }


    @NonNull
    @Override
    public MYorderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext()); }

        binding = DataBindingUtil.inflate(inflater, R.layout.adapter_my_order_list, parent, false);
        return new MYorderAdapter.MyViewHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MyOrderDTO productReviewDTO=productDTOS.get(position);

        sharedPrefrence= SharedPrefrence.getInstance(mContext);
        userDTO=sharedPrefrence.getParentUser(Consts.USER_DTO);


        binding.cancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                param.put(Consts.ORDER_ID, productDTOS.get(position).getOrder_id());
                param.put(Consts.USER_ID, userDTO.getId());
                Add_ToFav(position);
            }
        });



       if (status.equals("reorder")){
           binding.reorder.setVisibility(View.VISIBLE);
           binding.cancelOrder.setVisibility(View.GONE); }




       binding.reorder.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {


               String morder_id=productDTOS.get(position).getOrder_id();
               String order_price=productDTOS.get(position).getCheckout_price();



             //  get_reorder_api();


               mContext.startActivity(new Intent(mContext, PlaceorderActivity.class)
               .putExtra(Consts.BILLING,Consts.REBELLING)
               .putExtra(Consts.ORDER_ID,morder_id)
               );









           }
       });








        binding.setMyorderlist(productReviewDTO);
    }

    private void get_reorder_api() {
        new HttpsRequest(Consts.RE_ORDER, param, mContext).stringPost("TAG", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {


                    }
                    catch (Exception e) { e.printStackTrace(); }
                } else {
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    private void Add_ToFav(int position) {
        //param.put(Consts.USER_ID, userDTO.getId());
        new HttpsRequest(Consts.CANCEL_ORDER, param, mContext).stringPost("TAG", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                    removeAt(position); }
                    catch (Exception e) {
                    e.printStackTrace();



                    }
                } else {
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                }


            }
        });


    }



    public void removeAt(int position) {
        productDTOS.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, productDTOS.size());
    }










    @Override
    public int getItemCount() {

        return productDTOS.size();


    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterMyOrderListBinding binding;
        public MyViewHolder(@NonNull AdapterMyOrderListBinding view) {
            super(view.getRoot());
            this.binding = view;
        }
    }



}


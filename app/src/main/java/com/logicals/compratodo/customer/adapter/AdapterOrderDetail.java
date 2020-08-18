package com.logicals.compratodo.customer.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.logicals.compratodo.R;
import com.logicals.compratodo.customer.model.PendingProductDto;
import com.logicals.compratodo.databinding.AdapterOrderDetailBinding;
import java.util.ArrayList;


    public class AdapterOrderDetail extends RecyclerView.Adapter<AdapterOrderDetail.MyViewHolder> {
        LayoutInflater inflater;
        Context mContext;
        ArrayList<PendingProductDto> productDTOS = new ArrayList<>();
        AdapterOrderDetailBinding binding;
        View view;

        public AdapterOrderDetail(Context mContext, ArrayList<PendingProductDto> productDTOS) {
            this.mContext = mContext;
            this.productDTOS = productDTOS;
        }




        @NonNull
        @Override
        public AdapterOrderDetail.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (inflater == null) {
                inflater = LayoutInflater.from(parent.getContext()); }
            binding= DataBindingUtil.inflate(inflater,R.layout.adapter_order_detail,parent,false);

         //    view = LayoutInflater.from(mContext).inflate(R.layout.adapter_order_detail, parent, false);
         //   return new AdapterOrderDetail.MyViewHolder(view);



            return new AdapterOrderDetail.MyViewHolder(binding);

        }


        @Override
        public void onBindViewHolder(@NonNull AdapterOrderDetail.MyViewHolder holder, int position) {
            PendingProductDto orderDetailDTo = productDTOS.get(position);

            Log.e("check pending ",orderDetailDTo.getOrder_id());

            binding.productName.setText("jdj");

         binding.setOrderdetails(orderDetailDTo);





        }



        @Override
        public int getItemCount() {
            return productDTOS.size();
        }








        public class MyViewHolder extends RecyclerView.ViewHolder {
            AdapterOrderDetailBinding binding;
            public MyViewHolder(@NonNull AdapterOrderDetailBinding view) {
                super(view.getRoot());
                this.binding = view;
            }
        }

    }



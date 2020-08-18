package com.logicals.compratodo.customer.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.logicals.compratodo.R;
import com.logicals.compratodo.customer.activity.ProductDetailsActivity;
import com.logicals.compratodo.customer.fragment.Home2Fragment;
import com.logicals.compratodo.databinding.AdapterNewArrivalBinding;
import com.logicals.compratodo.interfacess.Consts;
import com.logicals.compratodo.model.ProductDTO;

import java.util.ArrayList;

public class AdapterNewArrival extends RecyclerView.Adapter<AdapterNewArrival.MyViewHolder> {
    LayoutInflater inflater;
    AdapterNewArrivalBinding binding;
    Context mContext;
    ArrayList<ProductDTO> productDTOS = new ArrayList<>();
    Home2Fragment fragment;

    public AdapterNewArrival(Context mContext, ArrayList<ProductDTO> productDTOS) {
        this.mContext = mContext;
        this.productDTOS = productDTOS;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        binding = DataBindingUtil.inflate(inflater, R.layout.adapter_new_arrival, parent, false);

        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final ProductDTO model = productDTOS.get(position);
        binding.tvName.setText(productDTOS.get(position).getProduct_name());
        binding.tvOfferPrice.setText("$"+productDTOS.get(position).getMin_price());
        binding.tvMrpPrice.setText("$"+productDTOS.get(position).getMax_price());
        binding.tvPercetage.setText(offered(Float.valueOf(productDTOS.get(position).getMin_price()),Float.valueOf(productDTOS.get(position).getMax_price()))+" % OFF");
        binding.tvMrpPrice.setPaintFlags(  binding.tvMrpPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        if (!productDTOS.get(position).getImage1().equalsIgnoreCase("")) {

            Glide.with(mContext)
                    .load(Consts.USER_IMAGE_URL+productDTOS.get(position).getImage2())
                    .into(binding.ivImage);
        } else if (!productDTOS.get(position).getImage2().equalsIgnoreCase("")) {

            Glide.with(mContext).load(Consts.USER_IMAGE_URL+productDTOS.get(position).getImage2()).into(binding.ivImage);
        } else
            Glide.with(mContext).load(Consts.USER_IMAGE_URL+productDTOS.get(position).getImage3()).into(binding.ivImage);




        binding.rlCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(mContext, ProductDetailsActivity.class);
                in.putExtra(Consts.DTO,productDTOS.get(position));
                in.putExtra(Consts.PRODUCT_ID,productDTOS.get(position).getId());
                mContext.startActivity(in);
            }
        });





    }


    @Override
    public int getItemCount() {
        return productDTOS.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterNewArrivalBinding binding;

        public MyViewHolder(@NonNull AdapterNewArrivalBinding view) {
            super(view.getRoot());
            this.binding = view;
        }
    }



    private int offered(Float miniprice,Float maxiprice){
        int percentage,exactValue;
        percentage = (int)((maxiprice - miniprice) );
        exactValue= (int) (( percentage*100)/maxiprice);
        return exactValue;

    }
}

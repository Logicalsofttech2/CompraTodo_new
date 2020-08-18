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
import com.logicals.compratodo.customer.activity.ProductDetailsActivity;
import com.logicals.compratodo.R;
import com.logicals.compratodo.databinding.AdapterNearbyproductBinding;
import com.logicals.compratodo.interfacess.Consts;
import com.logicals.compratodo.model.ProductDTO;

import java.util.ArrayList;

public class AdapterNearBY extends RecyclerView.Adapter<AdapterNearBY.MyViewHolder> {
    LayoutInflater inflater;
    AdapterNearbyproductBinding binding;
    Context mContext;
    ArrayList<ProductDTO> productDTOS = new ArrayList<>();
    int limit;

    
    public AdapterNearBY(Context mContext, ArrayList<ProductDTO> productDTOS,int limit) {
        this.mContext = mContext;
        this.productDTOS = productDTOS;
        this.limit=limit;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }

        binding = DataBindingUtil.inflate(inflater, R.layout.adapter_nearbyproduct, parent, false);
        return new MyViewHolder(binding);





    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        binding.tvName.setText(productDTOS.get(position).getProduct_name());
        binding.tvOfferPrice.setText("$" + productDTOS.get(position).getMin_price());
        binding.tvMrpPrice.setText("$" + productDTOS.get(position).getMax_price());
        binding.tvPercetage.setText(offered(Float.valueOf(productDTOS.get(position).getMin_price()), Float.valueOf(productDTOS.get(position).getMax_price())) + " % OFF");
        binding.tvMrpPrice.setPaintFlags(binding.tvMrpPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        if (!productDTOS.get(position).getImage1().equalsIgnoreCase("")) {

            Glide.with(mContext).load(Consts.USER_IMAGE_URL + productDTOS.get(position).getImage2()).into(binding.ivImage);
        } else if (!productDTOS.get(position).getImage2().equalsIgnoreCase("")) {

            Glide.with(mContext).load(Consts.USER_IMAGE_URL + productDTOS.get(position).getImage2()).into(binding.ivImage);
        } else
            Glide.with(mContext).load(Consts.USER_IMAGE_URL + productDTOS.get(position).getImage3()).into(binding.ivImage);



            binding.rlProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Toast.makeText(mContext, ""+productDTOS.get(position).getId(), Toast.LENGTH_SHORT).show();


                    Intent in =new Intent(mContext, ProductDetailsActivity.class);
                    in.putExtra(Consts.DTO,productDTOS.get(position));
                    in.putExtra(Consts.PRODUCT_ID,productDTOS.get(position).getId());
                    mContext.startActivity(in);



                }
            });


    }

    @Override
    public int getItemCount() {
        /*if (productDTOS.size()==1){
            return productDTOS.size();
        }else if (productDTOS.size()>=1){
            return 10;
        }else if (limit==0){
            return productDTOS.size();
        }else {
            return productDTOS.size();
        }*/

        return productDTOS.size();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterNearbyproductBinding binding;

        public MyViewHolder(@NonNull AdapterNearbyproductBinding view) {
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

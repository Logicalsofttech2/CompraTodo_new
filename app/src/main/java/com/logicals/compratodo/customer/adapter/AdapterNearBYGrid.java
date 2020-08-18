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

import com.logicals.compratodo.customer.fragment.Home2Fragment;
import com.logicals.compratodo.databinding.AdapterNearbyproductGridBinding;
import com.logicals.compratodo.interfacess.Consts;
import com.logicals.compratodo.model.ProductDTO;

import java.util.ArrayList;




    public class AdapterNearBYGrid extends RecyclerView.Adapter<AdapterNearBYGrid.MyViewHolder> {
        LayoutInflater inflater;
        AdapterNearbyproductGridBinding binding;
        Context mContext;
        ArrayList<ProductDTO> productDTOS = new ArrayList<>();
        Home2Fragment fragment;


        public AdapterNearBYGrid(Context mContext, ArrayList<ProductDTO> productDTOS) {
            this.mContext = mContext;
            this.productDTOS = productDTOS;

        }



        @NonNull
        @Override
        public AdapterNearBYGrid.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (inflater == null) {
                inflater = LayoutInflater.from(parent.getContext());
            }

            binding = DataBindingUtil.inflate(inflater, R.layout.adapter_nearbyproduct_grid, parent, false);
            return new AdapterNearBYGrid.MyViewHolder(binding);





        }

        @Override
        public void onBindViewHolder(@NonNull AdapterNearBYGrid.MyViewHolder holder, int position) {




            String ProductName=productDTOS.get(position).getProduct_name();
            if (Character.isUpperCase(ProductName.charAt(0))) {
                binding.tvName.setText(productDTOS.get(position).getProduct_name());
            } else {
                ProductName = productDTOS.get(position).getProduct_name().substring(0, 1).toUpperCase() + productDTOS.get(position).getProduct_name().substring(1).toLowerCase();
                binding.tvName.setText(ProductName);
            }







            binding.tvOfferPrice.setText("$"+productDTOS.get(position).getMin_price());
            binding.tvMrpPrice.setText("$"+productDTOS.get(position).getMax_price());
            binding.tvPercetage.setText(offered(Float.valueOf(productDTOS.get(position).getMin_price()),Float.valueOf(productDTOS.get(position).getMax_price()))+" % OFF");
            binding.tvMrpPrice.setPaintFlags(  binding.tvMrpPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            if (!productDTOS.get(position).getImage1().equalsIgnoreCase("")) {

                Glide.with(mContext)
                        .load(Consts.USER_IMAGE_URL+productDTOS.get(position).getImage2())

                        .into(binding.ivImage);
            } else if (!productDTOS.get(position).getImage2().equalsIgnoreCase("")) {

                Glide.with(mContext)
                        .load(Consts.USER_IMAGE_URL+productDTOS.get(position).getImage2())

                        .into(binding.ivImage);
            } else
                Glide.with(mContext)
                        .load(Consts.USER_IMAGE_URL+productDTOS.get(position).getImage3())

                        .into(binding.ivImage);



            binding.rlProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in =new Intent(mContext, ProductDetailsActivity.class);
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
            AdapterNearbyproductGridBinding binding;

            public MyViewHolder(@NonNull AdapterNearbyproductGridBinding view) {
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


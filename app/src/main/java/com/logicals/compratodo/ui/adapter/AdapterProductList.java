package com.logicals.compratodo.ui.adapter;

import android.app.Activity;
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
import com.logicals.compratodo.databinding.AdapterProductListBinding;
import com.logicals.compratodo.interfacess.Consts;
import com.logicals.compratodo.model.ProductDTO;
import java.util.ArrayList;




    public class AdapterProductList extends RecyclerView.Adapter<AdapterProductList.MyViewHolder> {
        LayoutInflater inflater;
        Context mContext;
        ArrayList<ProductDTO> productDTOS;
        AdapterProductListBinding binding;
        Activity activity;
        ProductDTO productDTO;

        public AdapterProductList(Context mContext, ArrayList<ProductDTO> productDTOS) {
            this.mContext = mContext;
            this.productDTOS = productDTOS;
            this.activity=activity;
        }

        @NonNull
        @Override
        public AdapterProductList.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (inflater == null) {
                inflater = LayoutInflater.from(parent.getContext());
            }
            binding = DataBindingUtil.inflate(inflater, R.layout.adapter_product_list, parent, false);

            return new AdapterProductList.MyViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull AdapterProductList.MyViewHolder holder, int position) {

            final ProductDTO model = productDTOS.get(position);



            String Product_name = productDTOS.get(position).getProduct_name().substring(0, 1).toUpperCase() + productDTOS.get(position).getProduct_name().substring(1).toLowerCase();
            binding.tvName.setText(Product_name);
            String Product_about = productDTOS.get(position).getAbout().substring(0, 1).toUpperCase() + productDTOS.get(position).getAbout().substring(1).toLowerCase();
            binding.tvDesc.setText(Product_about);



         //   binding.tvName.setText(productDTOS.get(position).getProduct_name());
            binding.tvOfferPrice.setText("$"+productDTOS.get(position).getMin_price());
            binding.tvMrpPrice.setText("$"+productDTOS.get(position).getMax_price());
            binding.tvPercetage.setText(offered(Float.valueOf(productDTOS.get(position).getMin_price()),Float.valueOf(productDTOS.get(position).getMax_price()))+" % OFF");
            binding.tvMrpPrice.setPaintFlags(  binding.tvMrpPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            if (!productDTOS.get(position).getImage1().equalsIgnoreCase("")) {

                Glide.with(mContext).load(Consts.USER_IMAGE_URL+productDTOS.get(position).getImage2()).into(binding.ivImage);
            } else if (!productDTOS.get(position).getImage2().equalsIgnoreCase("")) {

                Glide.with(mContext).load(Consts.USER_IMAGE_URL+productDTOS.get(position).getImage2()).into(binding.ivImage);

            } else
                Glide.with(mContext).load(Consts.USER_IMAGE_URL+productDTOS.get(position).getImage3()).into(binding.ivImage);







            binding.product.setOnClickListener(new View.OnClickListener() {
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
            AdapterProductListBinding binding;

            public MyViewHolder(@NonNull AdapterProductListBinding view) {
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


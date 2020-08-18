package com.logicals.compratodo.vendor.product;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.logicals.compratodo.R;
import com.logicals.compratodo.databinding.ProductAdapterBinding;
import com.logicals.compratodo.interfacess.Consts;
import com.logicals.compratodo.model.ProductDTO;
import com.logicals.compratodo.vendor.interfaces.CardIds;
import com.logicals.compratodo.vendor.ui.uploadproduct.UploadProductActivity;

import java.util.ArrayList;

public class AdapterProduct extends RecyclerView.Adapter<AdapterProduct.MyViewHolder> {
    LayoutInflater inflater;
    ProductAdapterBinding binding;
    Context mContext;
    ArrayList<ProductDTO> productDTOS = new ArrayList<>();
    ProductFragment fragment;
    CardIds listener;
ArrayList<String> add_card_ids=new ArrayList<>();


    public AdapterProduct(Context mContext, ArrayList<ProductDTO> productDTOS, ProductFragment fragment,CardIds listener) {
        this.mContext = mContext;
        this.productDTOS = productDTOS;
        this.fragment = fragment;
        this.listener=listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }

        binding = DataBindingUtil.inflate(inflater, R.layout.product_adapter, parent, false);

        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        try {
            listener = (CardIds) mContext;
        } catch (ClassCastException e) {
            throw new ClassCastException(mContext + " must implement OnHeadlineSelectedListener");
        }




        final ProductDTO model = productDTOS.get(position);
        binding.tvName.setText(productDTOS.get(position).getProduct_name());
        binding.tvAbout.setText(productDTOS.get(position).getAbout());
        binding.tvMrp.setText(productDTOS.get(position).getMin_price()+" "+"USD");
        binding.tvOfferprice.setText("MRP:-"+" "+productDTOS.get(position).getMax_price()+" "+"USD");

        if (!productDTOS.get(position).getImage1().equalsIgnoreCase("")) {

            Glide.with(mContext)
                    .load(Consts.USER_IMAGE_URL+productDTOS.get(position).getImage1())
                    .error(R.drawable.watches)
                    .into(binding.ivImage);
        } else if (!productDTOS.get(position).getImage2().equalsIgnoreCase("")) {

            Glide.with(mContext)
                    .load(Consts.USER_IMAGE_URL+productDTOS.get(position).getImage2())
                    .error(R.drawable.watches)
                    .into(binding.ivImage);
        } else
            Glide.with(mContext)
                    .load(Consts.USER_IMAGE_URL+productDTOS.get(position).getImage3())
                    .error(R.drawable.watches)
                    .into(binding.ivImage);


        binding.cbDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



        binding.tvEditProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mContext, UploadProductActivity.class);
                in.putExtra(Consts.DTO,productDTOS.get(position));
                mContext.startActivity(in);
            }
        });

      /*
        if(binding.cbDelete.isChecked()){
            binding.cbDelete.setBackgroundColor(R.color.colorPrimary);
            binding.rlChangeBackground.setBackgroundColor(R.color.transparentblack);
        }else {
            binding.cbDelete.setBackgroundColor(R.color.white);
            binding.rlChangeBackground.setBackgroundColor(R.color.transparentfull);
        }
*/





        binding.cbDelete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked())
                {
                  add_card_ids.add(productDTOS.get(position).getId());
                  listener.getCardids(add_card_ids);


                    /*
                    buttonView.setBackgroundColor(R.color.colorPrimary);
                    binding.rlChangeBackground.setBackgroundColor(R.color.transparentblack);*/



                    model.setSelected(!model.isSelected());
                }
                if(!buttonView.isChecked())
                {



                    add_card_ids.remove(productDTOS.get(position).getId());
                    listener.getCardids(add_card_ids);
                    /*
                    buttonView.setBackgroundColor(R.color.white);
                    binding.rlChangeBackground.setBackgroundColor(R.color.transparentfull);*/
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return productDTOS.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ProductAdapterBinding binding;

        public MyViewHolder(@NonNull ProductAdapterBinding view) {
            super(view.getRoot());
            this.binding = view;
        }
    }
}

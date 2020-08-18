package com.logicals.compratodo.customer.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.logicals.compratodo.R;
import com.logicals.compratodo.customer.activity.SubcatActivity;
import com.logicals.compratodo.databinding.AdapterCategoryBinding;
import com.logicals.compratodo.interfacess.Consts;
import com.logicals.compratodo.model.MainCategoryDTO;

import java.util.ArrayList;

public class AdapterCategory extends RecyclerView.Adapter<AdapterCategory.MyViewHolder> {
    LayoutInflater inflater;
    AdapterCategoryBinding binding;
    Context mContext;
    ArrayList<MainCategoryDTO> productDTOS = new ArrayList<>();


    public AdapterCategory(Context mContext, ArrayList<MainCategoryDTO> productDTOS) {
        this.mContext = mContext;
        this.productDTOS = productDTOS;
    }





    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        binding = DataBindingUtil.inflate(inflater, R.layout.adapter_category, parent, false);

        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        binding.tvCateName.setText(productDTOS.get(position).getName());


            Glide.with(mContext)
                    .load(Consts.USER_IMAGE_URL+productDTOS.get(position).getImage())

                    .into(binding.civCateImage);

            binding.rlCat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in=new Intent(mContext, SubcatActivity.class);
                    in.putExtra(Consts.DTO,productDTOS.get(position));
                    mContext.startActivity(in);


                }
            });





    }

    @Override
    public int getItemCount()
    {   if(productDTOS.size()>6){
        return 6;
    }else
        return productDTOS.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterCategoryBinding binding;

        public MyViewHolder(@NonNull AdapterCategoryBinding view) {
            super(view.getRoot());
            this.binding = view;
        }
    }
}

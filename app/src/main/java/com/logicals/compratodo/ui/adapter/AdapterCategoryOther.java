package com.logicals.compratodo.ui.adapter;

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
import com.logicals.compratodo.customer.activity.SubcategortlistActivity;
import com.logicals.compratodo.databinding.AdapterCategoryOtherBinding;
import com.logicals.compratodo.interfacess.Consts;
import com.logicals.compratodo.model.CategoryDTO;

import java.util.ArrayList;

public class AdapterCategoryOther extends RecyclerView.Adapter<AdapterCategoryOther.MyViewHolder> {
    LayoutInflater inflater;
    Context mContext;
    ArrayList<CategoryDTO> productDTOS = new ArrayList<>();
    AdapterCategoryOtherBinding binding;


    public AdapterCategoryOther(Context mContext, ArrayList<CategoryDTO> productDTOS) {
        this.mContext = mContext;
        this.productDTOS = productDTOS;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        binding = DataBindingUtil.inflate(inflater, R.layout.adapter_category_other, parent, false);

        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        binding.tvName.setText(productDTOS.get(position).getName());

        if (!productDTOS.get(position).getImage().equalsIgnoreCase("")) {

            Glide.with(mContext)
                    .load(Consts.USER_IMAGE_URL+productDTOS.get(position).getImage())
                    .error(R.drawable.watches)
                    .into(binding.ivImage);
        }

        binding.rlCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent intent = new Intent(mContext, SubcategortlistActivity.class);
                intent.putExtra(Consts.DTO,productDTOS.get(position));
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return productDTOS.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterCategoryOtherBinding binding;

        public MyViewHolder(@NonNull AdapterCategoryOtherBinding view) {
            super(view.getRoot());
            this.binding = view;
        }
    }
}

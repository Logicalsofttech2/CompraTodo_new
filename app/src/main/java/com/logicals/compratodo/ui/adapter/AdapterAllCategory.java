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
import com.logicals.compratodo.customer.activity.SubcatActivity;
import com.logicals.compratodo.databinding.AdapterAllCategoryBinding;
import com.logicals.compratodo.interfacess.Consts;
import com.logicals.compratodo.model.MainCategoryDTO;

import java.util.ArrayList;

public class AdapterAllCategory extends RecyclerView.Adapter<AdapterAllCategory.MyViewHolder> {
    LayoutInflater inflater;
    Context mContext;
    ArrayList<MainCategoryDTO> productDTOS = new ArrayList<>();
    AdapterAllCategoryBinding binding;


    public AdapterAllCategory(Context mContext, ArrayList<MainCategoryDTO> productDTOS) {
        this.mContext = mContext;
        this.productDTOS = productDTOS;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        binding = DataBindingUtil.inflate(inflater, R.layout.adapter_all_category, parent, false);

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

                Intent intent = new Intent(mContext, SubcatActivity.class);

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
        AdapterAllCategoryBinding binding;

        public MyViewHolder(@NonNull AdapterAllCategoryBinding view) {
            super(view.getRoot());
            this.binding = view;
        }
    }
}

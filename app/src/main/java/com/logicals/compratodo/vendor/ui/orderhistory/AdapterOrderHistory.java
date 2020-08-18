package com.logicals.compratodo.vendor.ui.orderhistory;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.logicals.compratodo.R;
import com.logicals.compratodo.databinding.VendorOrderHistoryBinding;
import com.logicals.compratodo.interfacess.Consts;
import com.logicals.compratodo.model.UserDTO;
import com.logicals.compratodo.preferences.SharedPrefrence;
import com.logicals.compratodo.vendor.model.MyProductListDTO;

import java.util.ArrayList;
import java.util.HashMap;


public class AdapterOrderHistory extends RecyclerView.Adapter<AdapterOrderHistory.MyViewHolder> {
    LayoutInflater inflater;
    Context mContext;
    ArrayList<MyProductListDTO> productDTOS;
    VendorOrderHistoryBinding binding;
    UserDTO userDTO;
    SharedPrefrence sharedPrefrence;
    HashMap<String, String> param = new HashMap<>();

    public AdapterOrderHistory(Context mContext, ArrayList<MyProductListDTO> productDTOS) {
        this.mContext = mContext;
        this.productDTOS = productDTOS;
    }

    @NonNull
    @Override
    public AdapterOrderHistory.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        binding = DataBindingUtil.inflate(inflater, R.layout.vendor_order_history, parent, false);
        return new AdapterOrderHistory.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterOrderHistory.MyViewHolder holder, int position) {
        final MyProductListDTO model = productDTOS.get(position);
        sharedPrefrence = SharedPrefrence.getInstance(mContext);
        userDTO = sharedPrefrence.getParentUser(Consts.USER_DTO);
        binding.setMyorderlist(model);
        Glide.with(mContext).load(Consts.USER_IMAGE_URL + productDTOS.get(position).getProduct_image()).error(R.drawable.frock).into(binding.productImage);
        binding.Accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext, R.style.BottomSheetDialogTheme);
                LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View view = li.inflate(R.layout.bottom_layout_status, null);
                bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                view.findViewById(R.id.a_to_z_res).setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                    }
                });

                view.findViewById(R.id.by_nearest).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                    }
                });


                view.findViewById(R.id.most_popular).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetDialog.setContentView(view);
                bottomSheetDialog.show();
            }
        });

    }


    @Override
    public int getItemCount() {
        return productDTOS.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        VendorOrderHistoryBinding binding;

        public MyViewHolder(@NonNull VendorOrderHistoryBinding view) {
            super(view.getRoot());
            this.binding = view;
        }
    }


    private int offered(Float miniprice, Float maxiprice) {
        int percentage, exactValue;
        percentage = (int) ((maxiprice - miniprice));
        exactValue = (int) ((percentage * 100) / maxiprice);
        return exactValue;
    }

    public void removeAt(int position) {
        productDTOS.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, productDTOS.size());
    }


}


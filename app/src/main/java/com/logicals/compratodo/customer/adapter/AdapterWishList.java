package com.logicals.compratodo.customer.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.logicals.compratodo.R;
import com.logicals.compratodo.customer.activity.ProductDetailsActivity;
import com.logicals.compratodo.databinding.AdapterWishListBinding;
import com.logicals.compratodo.https.HttpsRequest;
import com.logicals.compratodo.interfacess.Consts;
import com.logicals.compratodo.interfacess.Helper;
import com.logicals.compratodo.model.ProductDTO;
import com.logicals.compratodo.model.UserDTO;
import com.logicals.compratodo.preferences.SharedPrefrence;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterWishList extends RecyclerView.Adapter<AdapterWishList.MyViewHolder> {
    LayoutInflater inflater;
    Context mContext;
    ArrayList<ProductDTO> productDTOS;
    AdapterWishListBinding binding;
    Activity activity;
    HashMap<String, String> param = new HashMap<>();
    SharedPrefrence sharedPrefrence;
    UserDTO userDTO;

    public AdapterWishList(Context mContext, ArrayList<ProductDTO> productDTOS) {
        this.mContext = mContext;
        this.productDTOS = productDTOS;
        this.activity=activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        binding = DataBindingUtil.inflate(inflater, R.layout.adapter_wish_list, parent, false);

        return new AdapterWishList.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterWishList.MyViewHolder holder, int position) {

        final ProductDTO productDTO = productDTOS.get(position);

        sharedPrefrence = SharedPrefrence.getInstance(mContext);

        userDTO = sharedPrefrence.getParentUser(Consts.USER_DTO);

        binding.setProduct(productDTO);

        binding.edProductOff.setText(offered(Float.valueOf(productDTO.getMin_price()), Float.valueOf(productDTO.getMax_price())) + " % OFF");

        binding.ImgFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                param.put(Consts.PRODUCT_ID, productDTO.getId());
                Add_ToFav(position);
            }
        });



        binding.rlProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in =new Intent(mContext, ProductDetailsActivity.class);
                in.putExtra(Consts.DTO,productDTOS.get(position));
                in.putExtra(Consts.PRODUCT_ID,productDTOS.get(position).getProduct_id());
                mContext.startActivity(in);
            }
        });





    }

    @Override
    public int getItemCount() {
        return productDTOS.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterWishListBinding binding;

        public MyViewHolder(@NonNull AdapterWishListBinding view) {
            super(view.getRoot());
            this.binding = view;
        }
    }




    private void Add_ToFav(int position) {
        param.put(Consts.USER_ID, userDTO.getId());
        new HttpsRequest(Consts.ADDTOFAV, param, mContext).stringPost("TAG", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        String status=response.getString("status");

                        if (status.equals("1")){
                          //  binding.heart.setImageResource(R.drawable.ic_baseline_bookmark);
                        } else {
                            removeAt(position);



                            //binding.heart.setImageResource(R.drawable.ic_baseline_bookmark_border);
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                }


            }
        });


    }


    public  int offered(Float miniprice,Float maxiprice) {
        int percentage,exactValue;
        percentage = (int)((maxiprice - miniprice) );
        exactValue= (int) (( percentage*100)/maxiprice);
        return exactValue;
    }
    public void removeAt(int position) {
        productDTOS.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, productDTOS.size());
    }


}


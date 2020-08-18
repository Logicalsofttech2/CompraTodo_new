package com.logicals.compratodo.customer.adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.androidnetworking.utils.Utils;
import com.bumptech.glide.Glide;
import com.logicals.compratodo.R;
import com.logicals.compratodo.customer.GetValue;
import com.logicals.compratodo.customer.activity.ProductDetailsActivity;
import com.logicals.compratodo.databinding.AdapterAddToCartBinding;
import com.logicals.compratodo.https.HttpsRequest;
import com.logicals.compratodo.interfacess.Consts;
import com.logicals.compratodo.interfacess.Helper;
import com.logicals.compratodo.model.ProductDTO;
import com.logicals.compratodo.model.UserDTO;
import com.logicals.compratodo.preferences.SharedPrefrence;
import com.logicals.compratodo.utils.ProjectUtils;
import com.logicals.compratodo.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class AdapterAddtoCart extends RecyclerView.Adapter<AdapterAddtoCart.MyViewHolder> {
    LayoutInflater inflater;
    Context mContext;
    ArrayList<ProductDTO> productDTOS = new ArrayList<>();
    HashMap<String,String> param=new HashMap<>();
    AdapterAddToCartBinding binding;

    UserDTO  userDTO;
    Dialog dialog;
    String mStrproduct_id="",card_id;
    SharedPrefrence sharedPrefrence;
    GetValue callback;
    int quantity;
    public AdapterAddtoCart(Context mContext, ArrayList<ProductDTO> productDTOS,GetValue callback) {
        this.mContext = mContext;
        this.productDTOS = productDTOS;
        this.callback=callback;
    }


    @NonNull
    @Override
    public AdapterAddtoCart.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        binding = DataBindingUtil.inflate(inflater, R.layout.adapter_add_to_cart, parent, false);

        return new AdapterAddtoCart.MyViewHolder(binding);



    }

    @Override
    public void onBindViewHolder(@NonNull AdapterAddtoCart.MyViewHolder holder, int position) {



        try {
            this.callback = ((GetValue) mContext);
        } catch (ClassCastException e) {
            throw new ClassCastException("Fragment must implement AdapterCallback.");
        }







        quantity = Integer.parseInt(productDTOS.get(position).getQuantity());
        binding.tvCount.setText(productDTOS.get(position).getQuantity());
        binding.productName.setText(productDTOS.get(position).getProduct_name());
        binding.productPrice.setText("$"+productDTOS.get(position).getTotal_amount());
        sharedPrefrence= SharedPrefrence.getInstance(mContext);
        userDTO=sharedPrefrence.getParentUser(Consts.USER_DTO);
        mStrproduct_id=mStrproduct_id+","+productDTOS.get(position).getProduct_id();
        mStrproduct_id = mStrproduct_id.startsWith(",") ? mStrproduct_id.substring(1) : mStrproduct_id;


        try {
            callback.GetValues(mStrproduct_id);
        } catch (ClassCastException exception) {
            // do something
        }

        Log.e("check id",mStrproduct_id.toString());

        binding.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                param.put(Consts.USER_ID, userDTO.getId());
                param.put(Consts.CARDID,productDTOS.get(position).getId());
                Delete_dialoge(position);
            }
        });



        if (!productDTOS.get(position).getImage1().equalsIgnoreCase("")) {

            Glide.with(mContext)
                    .load(Consts.USER_IMAGE_URL+productDTOS.get(position).getImage1())
                    .error(R.drawable.watches)
                    .into(binding.productImg);
        }else if (!productDTOS.get(position).getImage2().equalsIgnoreCase("")) {

            Glide.with(mContext)
                    .load(Consts.USER_IMAGE_URL+productDTOS.get(position).getImage2())
                    .error(R.drawable.watches)
                    .into(binding.productImg);
        }else if (!productDTOS.get(position).getImage3().equalsIgnoreCase("")) {

            Glide.with(mContext)
                    .load(Consts.USER_IMAGE_URL+productDTOS.get(position).getImage3())
                    .error(R.drawable.watches)
                    .into(binding.productImg);
        }




        if (quantity == 1) {
            binding.tvCount.setText(String.valueOf(quantity));
        }



        binding.tvIncr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int  qty = Integer.valueOf(holder.binding.tvCount.getText().toString());
                Log.e("chck qty", String.valueOf(qty));
                qty = qty + 1;
                Log.e("chck qty2", String.valueOf(qty));
                String mfinal=productDTOS.get(position).getMin_price();
                int mfinalAmount= Integer.parseInt(mfinal);
                int value=mfinalAmount * qty;
                UpdateItemInCart(Consts.INCREASE,value,productDTOS.get(position).getId(),position,String.valueOf(qty), holder.binding.tvCount,holder.binding.productPrice);
            }
        });



        binding.tvDecr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int   qty = Integer.valueOf(binding.tvCount.getText().toString());
                if (qty > 1) {
                    qty = qty - 1;

                    String mfinal=productDTOS.get(position).getMin_price();
                    int mfinalAmount= Integer.parseInt(mfinal);
                    int  value=mfinalAmount * qty;
                    //   binding.tvCount.setText(String.valueOf(qty));
                    //  binding.productPrice.setText("$"+String.valueOf(value));
                   /* if (Utils.isConnected(mCtx)){
                        UpdateItemInCart2(value,heroList.get(position).getId(),position,String.valueOf(qty), holder.tv_count,holder.restorent_price);
                    }*/






                    UpdateItemInCart(Consts.INCREASE,value,productDTOS.get(position).getId(),position,String.valueOf(qty), binding.tvCount,binding.productPrice);

                }
            }
        });












        binding.rlCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProductDetailsActivity.class);
                intent.putExtra(Consts.DTO,productDTOS.get(position));
                intent.putExtra(Consts.PRODUCT_ID,productDTOS.get(position).getProduct_id());
                mContext.startActivity(intent);
            }
        });











    }

    private void Delete_dialoge(int position) {
        assert mContext != null;
        dialog = new Dialog(mContext);
        if(dialog.getWindow() != null){
            dialog.getWindow().setBackgroundDrawable(new
                    ColorDrawable(Color.TRANSPARENT));
        }

        View view = LayoutInflater.from(mContext).inflate(R.layout.dialoge_delete_card,null);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setAttributes(layoutParams);
        dialog.show();

        TextView textMode = view.findViewById(R.id.textMode);
        AppCompatButton buttonOk = view.findViewById(R.id.buttonOk);
        AppCompatButton buttonCancel = view.findViewById(R.id.cancel);





        buttonCancel.setOnClickListener(v -> {

            dialog.dismiss();

        });



        buttonOk.setOnClickListener(v -> {
            dialog.dismiss();
            delete_api(position);

        });

    }

    private void delete_api(int position) {
        ProjectUtils.showProgressDialog(mContext, true, mContext.getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.CARDDELETED, param, mContext).stringPost("TAG", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    ProjectUtils.pauseProgressDialog();
                    removeAt(position);
                    if (productDTOS.size()==0){
                        callback.GetHideButton(true);
                    }
                    Toast.makeText(mContext, ""+msg, Toast.LENGTH_SHORT).show();

                } else {
                    ProjectUtils.pauseProgressDialog();
                    ProjectUtils.showToast(mContext,msg);
                }
            }
        });

    }
    public void removeAt(int position) {
        productDTOS.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, productDTOS.size());
    }


    @Override
    public int getItemCount() {
        return productDTOS.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterAddToCartBinding binding;

        public MyViewHolder(@NonNull AdapterAddToCartBinding view) {
            super(view.getRoot());
            this.binding = view;
        }
    }





    private void UpdateItemInCart(String url,final int final_amount, final String item_id, final int addposition, final String qty_nmbr, final TextView tv_count_number, final TextView amount) {
        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Consts.BASE_URL+url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("cart_update", response.toString());
                        try {

                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("result");
                            if (result.equalsIgnoreCase("true")) {
                                progressDialog.dismiss();
                                JSONArray heroArray = obj.getJSONArray("data");
                                for (int i = 0; i < heroArray.length(); i++) {

                                    JSONObject heroObject = heroArray.getJSONObject(i);
                                    String qty=  heroObject.getString("quantity");
                                    String total_amount=  heroObject.getString("total_amount");
                                    card_id= heroArray.getJSONObject(i).getString("id");
                                    Log.e("check i value", String.valueOf(i)+" "+addposition);
/*
                                    try {
                                        callback.GetValues(mStrproduct_id);
                                    } catch (ClassCastException exception) {
                                        // do something
                                    }*/
                                    mStrproduct_id="";


                                    if (i==addposition){
                                        productDTOS.get(addposition).setQuantity(qty);
                                        tv_count_number.setText( productDTOS.get(addposition).getQuantity());
                                        productDTOS.get(addposition).setTotal_amount(total_amount);
                                        amount.setText("$"+productDTOS.get(addposition).getTotal_amount());
                                        Log.e("update_qty", tv_count_number.getText().toString()+" "+qty);

                                    }

                                }

                            } else {
                            }



                         notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("cart_id", item_id);
                params.put("quantity", qty_nmbr);
                params.put("user_id", userDTO.getId());
                params.put("total_amount", String.valueOf(final_amount));
                return params;
            }
        };

        VolleySingleton.getInstance(mContext).addToRequestQueue(stringRequest);
    }










}


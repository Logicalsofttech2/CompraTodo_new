package com.logicals.compratodo.vendor.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.logicals.compratodo.R;
import com.logicals.compratodo.customer.activity.PlaceorderActivity;
import com.logicals.compratodo.databinding.VendorAdapterMyOrderBinding;
import com.logicals.compratodo.https.HttpsRequest;
import com.logicals.compratodo.interfacess.Consts;
import com.logicals.compratodo.interfacess.Helper;
import com.logicals.compratodo.model.ProductDTO;
import com.logicals.compratodo.model.UserDTO;
import com.logicals.compratodo.preferences.SharedPrefrence;
import com.logicals.compratodo.utils.ProjectUtils;
import com.logicals.compratodo.vendor.model.MyProductListDTO;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AdapterMyOrderList extends RecyclerView.Adapter<AdapterMyOrderList.MyViewHolder> {
    LayoutInflater inflater;
    Context mContext;
    ArrayList<MyProductListDTO> productDTOS;
    VendorAdapterMyOrderBinding binding;
    UserDTO userDTO;
    SharedPrefrence sharedPrefrence;
    HashMap<String, String> param = new HashMap<>();
    public AdapterMyOrderList(Context mContext, ArrayList<MyProductListDTO> productDTOS) {
        this.mContext = mContext;
        this.productDTOS = productDTOS;
    }

    public void setFilter(List<MyProductListDTO> apointUserModelArrayList1) {
        productDTOS = new ArrayList<>();
        productDTOS.addAll(apointUserModelArrayList1);
        notifyDataSetChanged();
    }









    @NonNull
    @Override
    public AdapterMyOrderList.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        binding = DataBindingUtil.inflate(inflater, R.layout.vendor_adapter_my_order, parent, false);
        return new AdapterMyOrderList.MyViewHolder(binding);




    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMyOrderList.MyViewHolder holder, int position) {

        final MyProductListDTO model = productDTOS.get(position);

        sharedPrefrence = SharedPrefrence.getInstance(mContext);
        userDTO = sharedPrefrence.getParentUser(Consts.USER_DTO);
        binding.setMyorderlist(model);
        Glide.with(mContext).load(Consts.USER_IMAGE_URL+productDTOS.get(position).getProduct_image()).error(R.drawable.frock).into(binding.productImage);

          binding.Accept.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  param.put(Consts.USER_ID,userDTO.getId());
                  param.put(Consts.ORDER_ID,productDTOS.get(position).getOrder_id());
                  Accept_Order_(position,Consts.AcceptORder);
              }
          });








    }



    @Override
    public int getItemCount() {
        return productDTOS.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        VendorAdapterMyOrderBinding binding;

        public MyViewHolder(@NonNull VendorAdapterMyOrderBinding view) {
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






    private void Accept_Order_(final int position,String apiname) {
        ProjectUtils.showProgressDialog(mContext,false,mContext.getResources().getString(R.string.please_wait));
        new HttpsRequest(apiname,param,mContext).imagePost("TAG", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if(flag){
                    Toast.makeText(mContext, "Request Accepted Successfully!!", Toast.LENGTH_SHORT).show();
                    //  dialog.dismiss();
                    removeAt(position);
                }else {
                    Toast.makeText(mContext, ""+msg, Toast.LENGTH_SHORT).show();
                    //Utils.alert_dialoge(mCtx,response.body().getMsg());
                }
            }
        });
    }
}


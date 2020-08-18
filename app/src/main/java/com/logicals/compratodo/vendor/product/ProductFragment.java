package com.logicals.compratodo.vendor.product;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.logicals.compratodo.R;
import com.logicals.compratodo.databinding.FragmentSaveBinding;
import com.logicals.compratodo.https.HttpsRequest;
import com.logicals.compratodo.interfacess.Consts;
import com.logicals.compratodo.interfacess.Helper;
import com.logicals.compratodo.model.ProductDTO;
import com.logicals.compratodo.model.UserDTO;
import com.logicals.compratodo.preferences.SharedPrefrence;
import com.logicals.compratodo.vendor.interfaces.CardIds;
import com.logicals.compratodo.vendor.ui.uploadproduct.UploadProductActivity;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import static com.logicals.compratodo.vendor.activity.VendorHome.getProductId;

public class ProductFragment extends Fragment  {
    FragmentSaveBinding binding;
    HashMap<String,String>param=new HashMap<>();
    static String TAG= ProductFragment.class.getSimpleName();
    SharedPrefrence sharedPrefrence;
    UserDTO userDTO;
    ArrayList<ProductDTO>productDTOS=new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    AdapterProduct adapterProduct;
    ArrayList<String> strings=new ArrayList<>();
    String getcardId;
    CardIds listenr;
    ArrayList<String> GetValuesProductsId=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_save, container, false);
        sharedPrefrence = SharedPrefrence.getInstance(getActivity());
        userDTO = sharedPrefrence.getParentUser(Consts.USER_DTO);


        Log.e("check user dto",getProductId.toString());


            binding.uploadproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            startActivity(new Intent(getActivity(), UploadProductActivity.class)); }});

            binding.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modeDialogBox("Do You Want to Delete This product?");
               // deleteProduct();
            }
        });

            getProductList();



        return binding.getRoot();}



    private void getProductList() {

        param.put(Consts.USER_ID,userDTO.getId());

        new HttpsRequest(Consts.PRODUCTLIST,param,getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
            if(flag){
                try {
                    Type listType = new TypeToken<ArrayList<ProductDTO>>() {}.getType();
                    productDTOS = new Gson().fromJson(response.getJSONArray("data").toString(), listType);
                    setData();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else {
//                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            }



            }
        });
    }

    private void setData() {
        linearLayoutManager= new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
        binding.rvProduct.setLayoutManager(linearLayoutManager);
        adapterProduct = new AdapterProduct(getActivity(),productDTOS, ProductFragment.this,listenr);
        binding.rvProduct.setAdapter(adapterProduct);
    }




    private void deleteProduct(){
        String delete_id= String.valueOf(getProductId);
        delete_id = delete_id.replaceAll("[\\[\\](){}]","");
        if (delete_id.equals("")){
            Toast.makeText(getActivity(), "Please Check For Delete Products", Toast.LENGTH_SHORT).show();
        }else {
            param.put(Consts.USER_ID,userDTO.getId());
            param.put(Consts.PRODUCT_ID, String.valueOf(delete_id));
                new HttpsRequest(Consts.PRODUCTDELETE,param,getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if(flag){
                    getProductList();
                }else {
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show(); } }
        });
        }
    }

    private void modeDialogBox(String mode) {
        assert getActivity() != null;
        Dialog dialog = new Dialog(getActivity());
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.custom_dialog_mode_change, null);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setAttributes(layoutParams);
        dialog.show();

        TextView textMode = view.findViewById(R.id.textMode);
        AppCompatButton buttonOk = view.findViewById(R.id.buttonOk);

        textMode.setText(mode);

        buttonOk.setOnClickListener(v -> {
            dialog.dismiss();
            deleteProduct();
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        getProductList();
    }




}
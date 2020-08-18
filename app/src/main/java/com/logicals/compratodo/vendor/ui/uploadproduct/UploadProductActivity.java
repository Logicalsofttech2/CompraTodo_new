package com.logicals.compratodo.vendor.ui.uploadproduct;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.logicals.compratodo.R;
import com.logicals.compratodo.databinding.ActivityUploadProductBinding;
import com.logicals.compratodo.https.HttpsRequest;
import com.logicals.compratodo.interfacess.Consts;
import com.logicals.compratodo.interfacess.Helper;
import com.logicals.compratodo.model.CategoryDTO;
import com.logicals.compratodo.model.MeasureDTO;
import com.logicals.compratodo.model.ProductDTO;
import com.logicals.compratodo.model.SubCategoryDTO;
import com.logicals.compratodo.model.UserDTO;
import com.logicals.compratodo.preferences.SharedPrefrence;
import com.logicals.compratodo.ui.adapter.CategoryDropDown;
import com.logicals.compratodo.ui.adapter.MeasureDropDown;
import com.logicals.compratodo.ui.adapter.SubCategoryDropDown;
import com.logicals.compratodo.utils.ProjectUtils;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class UploadProductActivity extends AppCompatActivity implements  View.OnClickListener{
    ActivityUploadProductBinding binding;
    Context mContext;
    static String TAG=UploadProductActivity.class.getSimpleName();
    ArrayList<SubCategoryDTO>categoryDTOS= new ArrayList<>();
    ArrayList<CategoryDTO>mainCategoryDTOS= new ArrayList<>();
    ArrayList<MeasureDTO>measureDTOS= new ArrayList<>();
    CategoryDropDown adapter;
    SubCategoryDropDown adapter1;
    MeasureDropDown adapter2;
    HashMap<String, String> param = new HashMap<>();
    HashMap<String, String> param2 = new HashMap<>();
    HashMap<String, String> param3 = new HashMap<>();
    HashMap<String, String> parampro = new HashMap<>();

    ProductDTO productDTO;
    File file;
    private Uri mCropImageUri;
    int img = 0, check,imgcheck=0;
    HashMap<String, File> paramfile = new HashMap<>();
    String path1 = "",path2 = "",path3 = "";
    SharedPrefrence sharedPrefrence;
    UserDTO userDTO;
    int i =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_upload_product);
        mContext=UploadProductActivity.this;
        sharedPrefrence = SharedPrefrence.getInstance(mContext);
        userDTO = sharedPrefrence.getParentUser(Consts.USER_DTO);
        getCategory();

        Log.e("check main category id",userDTO.getMain_cate_id());
        binding.spinner3.setPrompt(getResources().getString(R.string.select_category));
        binding.spinner4.setPrompt(getResources().getString(R.string.select_category));

        calculateValue();



        if(getIntent().hasExtra(Consts.DTO)){
            productDTO=(ProductDTO) getIntent().getSerializableExtra(Consts.DTO);
            binding.tvHeaderComment.setText(getResources().getString(R.string.update_product));
            binding.btnUploadProduct.setText(getResources().getString(R.string.update));
            i=1;
            setData();
        }





    }

    private void setData() {
        binding.etProductName.setText(productDTO.getProduct_name());
        binding.etMiniQuantity.setText(productDTO.getAvailable_stock());
        binding.etCalculatedPrice.setText(productDTO.getTotal()+" "+getResources().getString(R.string.usd));
        binding.etSellingPrice.setText(productDTO.getMax_price());
        binding.etDiscription.setText(productDTO.getAbout());
        binding.etProductPricePerMeasure.setText(productDTO.getMin_price());
        binding.etQuantityInStock.setText(productDTO.getAvailable_stock());

        Glide.with(mContext)
                .load(Consts.USER_IMAGE_URL+productDTO.getImage1())
                .error(R.drawable.ic_frame)
                .into(binding.img1);

        Glide.with(mContext)
                .load(Consts.USER_IMAGE_URL+productDTO.getImage2())
                .error(R.drawable.ic_frame)
                .into(binding.img2);

        Glide.with(mContext)
                .load(Consts.USER_IMAGE_URL+productDTO.getImage3())
                .error(R.drawable.ic_frame)
                .into(binding.img3);


    }

    private void calculateValue() {


        if(binding.etMiniQuantity.toString().isEmpty()){

        }else {

            binding.etMiniQuantity.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
/*
                Double v1 = Double.parseDouble(!binding.etMiniQuantity.getText().toString().isEmpty() ?
                        binding.etMiniQuantity.getText().toString() : "0");
                Double v2 = Double.parseDouble(!binding.etSellingPrice.getText().toString().isEmpty() ?
                        binding.etSellingPrice.getText().toString() : "0");
                Double value = v1 * v2;
                binding.etCalculatedPrice.setText(String.valueOf(value));*/
                }

                @Override
                public void afterTextChanged(Editable s) {


                    Double v1 = Double.parseDouble(!binding.etMiniQuantity.getText().toString().isEmpty()?
                            binding.etMiniQuantity.getText().toString() : "0");
                    Double v2 = Double.parseDouble(!binding.etProductPricePerMeasure.getText().toString().isEmpty()?
                            binding.etProductPricePerMeasure.getText().toString() : "0");
                    Double value = v1 * v2;

                    binding.etCalculatedPrice.setText(String.valueOf(value)+" "+getResources().getString(R.string.usd));


                }
            });

        }

        binding.etProductPricePerMeasure.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Double v1 = Double.parseDouble(!binding.etMiniQuantity.getText().toString().isEmpty() ? binding.etMiniQuantity.getText().toString() : "0");
                Double v2 = Double.parseDouble(!binding.etProductPricePerMeasure.getText().toString().isEmpty() ?
                        binding.etProductPricePerMeasure.getText().toString() : "0");
                Double value = v1 * v2;
                binding.etCalculatedPrice.setText(String.valueOf(value)+" "+getResources().getString(R.string.usd));

            }
        });

        binding.img1.setOnClickListener(this);
        binding.img2.setOnClickListener(this);
        binding.img3.setOnClickListener(this);
        binding.ivBack.setOnClickListener(this);
        binding.btnUploadProduct.setOnClickListener(this);



    }

    private void getCategory() {
        param.put(Consts.ID,userDTO.getMain_cate_id());
        new HttpsRequest(Consts.CATEGORY,param,mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
            if(flag){
                try {
                    Type listType = new TypeToken<ArrayList<CategoryDTO>>() {}.getType();
                    mainCategoryDTOS = new Gson().fromJson(response.getJSONArray("data").toString(), listType);
                    adapter = new CategoryDropDown(mContext, R.layout.food_type_list, mainCategoryDTOS);
                    adapter.setDropDownViewResource(R.layout.food_type_list);
                    binding.spinner2.setPrompt(getResources().getString(R.string.select_category));
                    binding.spinner2.setAdapter(adapter);
                    binding.spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
                            Log.e("TAG", "onItemSelected: " + position);
                            CategoryDTO mainCategoryDTO = mainCategoryDTOS.get(position);
                            parampro.put(Consts.CATEGORYID,mainCategoryDTO.getId());
                            getSubCategory(mainCategoryDTO.getId());
                            Log.e(TAG, "onItemSelected: " + mainCategoryDTO.getName());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                        }
                    });

                } catch (Exception e) {
                }
            }else {
                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
            }
            }
        });


    }


    private void getSubCategory(String id){
        param2.put(Consts.ID,id);
        new HttpsRequest(Consts.SUB_CATEGORY,param2,mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if(flag){
                    try {
                        Type listType = new TypeToken<ArrayList<SubCategoryDTO>>() {}.getType();
                        categoryDTOS = new Gson().fromJson(response.getJSONArray("data").toString(), listType);
                        adapter1 = new SubCategoryDropDown(mContext, R.layout.food_type_list, categoryDTOS);
                        adapter1.setDropDownViewResource(R.layout.food_type_list);
                        binding.spinner3.setAdapter(adapter1);
                        binding.spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
                                Log.e("TAG", "onItemSelected: " + position);
                                SubCategoryDTO categoryDTO = categoryDTOS.get(position);
                                parampro.put(Consts.SUBCATEGORYID,categoryDTO.getId());

                                getMeasure(categoryDTO.getId());
                                Log.e(TAG, "onItemSelected: " + categoryDTO.getName());
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parentView) {
                            }
                        });

                    } catch (Exception e) {
                    }


                }else {
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();

                }
            }
        });
    }


    private void getMeasure(String id){
        param3.put(Consts.ID,id);

        new HttpsRequest(Consts.MEASURE,param3,mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if(flag){

                    try {

                        Type listType =
                                new TypeToken<ArrayList<MeasureDTO>>() {
                                }.getType();
                        measureDTOS = new Gson().fromJson(response.getJSONArray("data").toString(), listType);

                        adapter2 = new MeasureDropDown(mContext, R.layout.food_type_list, measureDTOS);
                        adapter2.setDropDownViewResource(R.layout.food_type_list);
                        binding.spinner4.setAdapter(adapter2);

                        binding.spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {

                                Log.e("TAG", "onItemSelected: " + position);

                                MeasureDTO measureDTO = measureDTOS.get(position);


                                parampro.put(Consts.UNITOFMEASURE,measureDTO.getId());

//                                getSubCategory(mainCategoryDTO.getId());

                                Log.e(TAG, "onItemSelected: " + measureDTO.getName());


                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parentView) {
                            }

                        });

                    } catch (Exception e) {
                    }


                }else {
                    parampro.put(Consts.UNITOFMEASURE,"");
                    binding.RelativeProduct.setVisibility(View.GONE);
                    binding.unitMeasure.setVisibility(View.GONE);

                   // Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void onSelectImageClick(View view) {
        CropImage.startPickImageActivity(this);
    }


    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);

            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                mCropImageUri = imageUri;
                try {
                    file = toFile(mCropImageUri);
                    Log.d("file", "File...:::: uti - " + file.getPath() + " file -" + file + " : " + file.exists());
                } catch (Exception e) {
                    Log.e("check excep", e.toString());
                    e.printStackTrace();
                }
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                // no permissions required or already grunted, can start crop image activity
                startCropImageActivity(imageUri);
            }
        }

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                try {
                    file = toFile(result.getUri());
                    Log.d("file", "File...:::: uti - " + file.getPath() + " file -" + file + " : " + file.exists());
                } catch (Exception e) {
                    Log.e("check excep", e.toString());
                    e.printStackTrace();
                }
                if (imgcheck == 1) {
                    binding.img1.setImageURI(result.getUri());
                    img = 1;
                    paramfile.put(Consts.IMAGE1, file);
                    path1=file.getAbsolutePath();
                } else if (imgcheck == 2) {
                    binding.img2.setImageURI(result.getUri());
                    img = 2;
                    paramfile.put(Consts.IMAGE2, file);
                    path2=file.getAbsolutePath();

                }else if (imgcheck == 0) {
                    binding.img3.setImageURI(result.getUri());
                    img = 1;
                    path3=file.getAbsolutePath();
                    paramfile.put(Consts.IMAGE3, file);
                }
                //  Toast.makeText(this, "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG).show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }


        }
    }


    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
    }

    public File toFile(Uri uri) {
        if (uri == null) return null;
        Log.d(">>> uri path:", uri.getPath());
        Log.d(">>> uri string:", uri.toString());
        return new File(uri.getPath());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img1:
                imgcheck=1;
                onSelectImageClick(v);
                break;
            case R.id.img2:
                imgcheck=2;
                onSelectImageClick(v);
                break;
            case R.id.img3:
                imgcheck=0;
                onSelectImageClick(v);
                break;
            case R.id.btnUploadProduct:
                checkData();
                break;
            case R.id.ivBack:
                onBackPressed();
                break;
        }


    }





    private void checkData(){
        if(i==0){
            if (binding.etProductName.getText().toString().isEmpty()) {
                binding.etProductName.setError(getResources().getString(R.string.addproductname));
            } else if (binding.etProductPricePerMeasure.getText().toString().isEmpty()) {
                binding.etProductPricePerMeasure.setError(getResources().getString(R.string.addpricepermoq));
                binding.etProductPricePerMeasure.requestFocus();
            } else if (binding.etSellingPrice.getText().toString().isEmpty()) {
                binding.etSellingPrice.setError(getResources().getString(R.string.addpurchesprice));
                binding.etSellingPrice.requestFocus();
            } else if (binding.etMiniQuantity.getText().toString().isEmpty()) {
                binding.etMiniQuantity.setError(getResources().getString(R.string.addminimumorder));
                binding.etMiniQuantity.requestFocus();
            } else if (binding.etQuantityInStock.getText().toString().isEmpty()) {
                binding.etQuantityInStock.setError(getResources().getString(R.string.addquantitystok));
                binding.etQuantityInStock.requestFocus();
            }else if (binding.etDiscription.getText().toString().isEmpty()) {
                binding.etDiscription.setError(getResources().getString(R.string.adddescription));
                binding.etDiscription.requestFocus();
            }else if (path1.equalsIgnoreCase("")) {
                Toast.makeText(mContext, R.string.addimg1, Toast.LENGTH_SHORT).show();
            }/*else if (path2.equalsIgnoreCase("")) {
                Toast.makeText(mContext, R.string.addimg2, Toast.LENGTH_SHORT).show();
            }else if (path3.equalsIgnoreCase("")) {
                Toast.makeText(mContext, R.string.addimg3, Toast.LENGTH_SHORT).show();
            }*/ else {
                parampro.put(Consts.PRODUCTNAME, ProjectUtils.getEditTextValue(binding.etProductName));
                parampro.put(Consts.PRICEPERMOQ, ProjectUtils.getEditTextValue(binding.etProductPricePerMeasure));
                parampro.put(Consts.PURCHESPRICE, ProjectUtils.getEditTextValue(binding.etSellingPrice));
                parampro.put(Consts.MINIMUMQUANTITY, ProjectUtils.getEditTextValue(binding.etMiniQuantity));
                parampro.put(Consts.DESCRIPTION, ProjectUtils.getEditTextValue(binding.etDiscription));
                parampro.put(Consts.QUANTITYSTOCK, ProjectUtils.getEditTextValue(binding.etQuantityInStock));
                parampro.put(Consts.TOTALPRICE, binding.etCalculatedPrice.getText().toString().trim());
                parampro.put(Consts.ACCOUNT_NO, ProjectUtils.getEditTextValue(binding.etProductName));
                parampro.put(Consts.USER_ID, userDTO.getId());
                addProduct(Consts.ADDITEAM);
            }
        }else {

            if (binding.etProductName.getText().toString().isEmpty()) {
                binding.etProductName.setError(getResources().getString(R.string.addproductname));
            } else if (binding.etProductPricePerMeasure.getText().toString().isEmpty()) {
                binding.etProductPricePerMeasure.setError(getResources().getString(R.string.addpricepermoq));
                binding.etProductPricePerMeasure.requestFocus();
            } else if (binding.etSellingPrice.getText().toString().isEmpty()) {
                binding.etSellingPrice.setError(getResources().getString(R.string.addpurchesprice));
                binding.etSellingPrice.requestFocus();
            } else if (binding.etMiniQuantity.getText().toString().isEmpty()) {
                binding.etMiniQuantity.setError(getResources().getString(R.string.addminimumorder));
                binding.etMiniQuantity.requestFocus();
            } else if (binding.etQuantityInStock.getText().toString().isEmpty()) {
                binding.etQuantityInStock.setError(getResources().getString(R.string.addquantitystok));
                binding.etQuantityInStock.requestFocus();
            }else if (binding.etDiscription.getText().toString().isEmpty()) {
                binding.etDiscription.setError(getResources().getString(R.string.adddescription));
                binding.etDiscription.requestFocus();
            }/*else if (path1.equalsIgnoreCase("")) {
                Toast.makeText(mContext, R.string.addimg1, Toast.LENGTH_SHORT).show();
            }else if (path2.equalsIgnoreCase("")) {
                Toast.makeText(mContext, R.string.addimg2, Toast.LENGTH_SHORT).show();
            }else if (path3.equalsIgnoreCase("")) {
                Toast.makeText(mContext, R.string.addimg3, Toast.LENGTH_SHORT).show();
            }*/ else {
                parampro.put(Consts.PRODUCTNAME, ProjectUtils.getEditTextValue(binding.etProductName));
                parampro.put(Consts.PRICEPERMOQ, ProjectUtils.getEditTextValue(binding.etProductPricePerMeasure));
                parampro.put(Consts.PURCHESPRICE, ProjectUtils.getEditTextValue(binding.etSellingPrice));
                parampro.put(Consts.MINIMUMQUANTITY, ProjectUtils.getEditTextValue(binding.etMiniQuantity));
                parampro.put(Consts.DESCRIPTION, ProjectUtils.getEditTextValue(binding.etDiscription));
                parampro.put(Consts.QUANTITYSTOCK, ProjectUtils.getEditTextValue(binding.etQuantityInStock));
                parampro.put(Consts.TOTALPRICE, binding.etCalculatedPrice.getText().toString().trim());
                parampro.put(Consts.ACCOUNT_NO, ProjectUtils.getEditTextValue(binding.etProductName));
                parampro.put(Consts.USER_ID, userDTO.getId());
                parampro.put(Consts.PRODUCT_ID, productDTO.getId());
                addProduct(Consts.UPDATEPRODUCT);
            }
        }

    }

    private void addProduct(String apiname) {
        ProjectUtils.showProgressDialog(mContext,false,getResources().getString(R.string.please_wait));
        new HttpsRequest(apiname,parampro,paramfile,mContext).imagePost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if(flag){
                    onBackPressed();

                }else {
                    modeDialogBox(msg);
                }


            }
        });



    }


    private void modeDialogBox(String mode) {

        assert mContext != null;
        Dialog dialog = new Dialog(mContext);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new
                    ColorDrawable(Color.TRANSPARENT));
        }

        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.custom_dialog_mode_change, null);

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

        });

    }
}
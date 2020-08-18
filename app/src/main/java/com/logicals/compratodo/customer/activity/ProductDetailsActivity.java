package com.logicals.compratodo.customer.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.gson.Gson;
import com.logicals.compratodo.R;
import com.logicals.compratodo.customer.adapter.AdapterProductReview;
import com.logicals.compratodo.customer.adapter.AdapterNearBY;
import com.logicals.compratodo.customer.model.ProductDetailDTO;
import com.logicals.compratodo.databinding.ActivityProductDetailsBinding;
import com.logicals.compratodo.https.HttpsRequest;
import com.logicals.compratodo.interfacess.Consts;
import com.logicals.compratodo.interfacess.Helper;
import com.logicals.compratodo.login_signup.CustomerLoginActivity;
import com.logicals.compratodo.model.ProductDTO;
import com.logicals.compratodo.model.UserDTO;
import com.logicals.compratodo.preferences.SharedPrefrence;
import com.logicals.compratodo.preferences.Shared_Pref;
import com.logicals.compratodo.utils.ProjectUtils;


import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener, View.OnClickListener {
    private SliderLayout mDemoSlider;
    LinearLayout zoomimage;
    TextView tv_cart;
    ProductDTO productDTO;
    ActivityProductDetailsBinding binding;
    HashMap<String, String> param = new HashMap<>();
    SharedPrefrence sharedPrefrence;
    UserDTO userDTO;
    HashMap<String, String> file_maps;
    Context mContext;
    ProductDetailDTO productDetailDTO;
    String product_id;


    RadioGroup radioGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_details);
        mContext = ProductDetailsActivity.this;
        binding.MainLinear.setVisibility(View.GONE);
        sharedPrefrence = SharedPrefrence.getInstance(ProductDetailsActivity.this);
        userDTO = sharedPrefrence.getParentUser(Consts.USER_DTO);

        if (getIntent().hasExtra(Consts.DTO)) {
            product_id=getIntent().getStringExtra(Consts.PRODUCT_ID);
            productDTO = (ProductDTO) getIntent().getSerializableExtra(Consts.DTO);
        }


        get_Product_Details(product_id);
        String mProductName = productDTO.getProduct_name().substring(0, 1).toUpperCase() + productDTO.getProduct_name().substring(1);
        binding.tvProductName.setText(mProductName);
        binding.productNameBold.setText(mProductName);
        String mProductAbout = productDTO.getAbout().substring(0, 1).toUpperCase() + productDTO.getAbout().substring(1);
        binding.tvProductDiscription.setText(mProductAbout);
        binding.edMrp.setText("$" + productDTO.getMax_price());
        binding.sellingPrice.setText("$" + productDTO.getMin_price());
        binding.edProductOff.setText(offered(Float.valueOf(productDTO.getMin_price()), Float.valueOf(productDTO.getMax_price())) + " % OFF");
        binding.edMrp.setPaintFlags(binding.edMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        binding.tvOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharedPrefrence.isLoggedIn()) {
                    if (productDetailDTO.getAddtocartstatus().equals("1")){
                        binding.tvOrder.setText("Go To Cart");
                        startActivity(new Intent(ProductDetailsActivity.this,AddToCartActivity.class));
                    }else {
                        Add_to_Cart();
                  }
                } else {
                    Login_Dialoge();
                }
            }
        });
        binding.tvCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPrefrence.isLoggedIn()) {
                    Shared_Pref.setProductId(mContext,product_id);
                    Intent intent = new Intent(ProductDetailsActivity.this, PlaceorderActivity.class);
                    intent.putExtra(Consts.BILLING,Consts.PRODUCTBILLING);
                    startActivity(intent);

                } else {
                    Login_Dialoge();
                }
            }
        });
        binding.TvViewAllRelatedProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext,RelatedProductsActivity.class).putExtra("product_id",productDTO.getId()));
            }
        });
        binding.RatingReviewALl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext,ViewAllRatingActivity.class).putExtra("product_id",productDTO.getId()));
            }
        });
        binding.heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharedPrefrence.isLoggedIn()){
                    Add_ToFav();
                }else {
                    Login_Dialoge();
                }

            }
        });




        binding.mLinReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* if (sharedPrefrence.isLoggedIn()){
                    Report_Dialoge();
                }else {
                    Login_Dialoge();
                }*/

                Report_Dialoge();


            }
        });









///////////////////////////start slider////////////////////////////////////////////////////////////


        file_maps = new HashMap<String, String>();
        file_maps.put(productDTO.getImage1(), Consts.USER_IMAGE_URL + productDTO.getImage1());
        file_maps.put(productDTO.getImage2(), Consts.USER_IMAGE_URL + productDTO.getImage2());
        file_maps.put(productDTO.getImage3(), Consts.USER_IMAGE_URL + productDTO.getImage3());
        for (String name : file_maps.keySet()) {
            DefaultSliderView defaultSliderView = new DefaultSliderView(ProductDetailsActivity.this);
                     defaultSliderView
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this::onSliderClick);
            binding.mDemoSlider.addSlider(defaultSliderView);
            binding.mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
            binding.mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            binding.mDemoSlider.setCustomAnimation(new DescriptionAnimation());
            //  binding.mDemoSlider.setDuration(3000);
            binding.mDemoSlider.stopAutoCycle();
        }

    }

    private void Report_Dialoge() {
        assert mContext != null;
        Dialog dialog = new Dialog(mContext);
       /* if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }*/
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialoge_report, null);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setAttributes(layoutParams);
        dialog.show();
        TextView textMode = view.findViewById(R.id.textMode);
        AppCompatButton buttonOk = view.findViewById(R.id.buttonOk);
        AppCompatButton buttonCancel = view.findViewById(R.id.cancel);


         radioGroup=view.findViewById(R.id.RadioGroup);



        buttonCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });


        buttonOk.setOnClickListener(v -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            RadioButton   genderradioButton = (RadioButton)view.findViewById(selectedId);
            if(selectedId==-1){
                Toast.makeText(mContext,"Please Select Options To Report", Toast.LENGTH_SHORT).show();
            }
            else{
                get_Report_Api(genderradioButton.getText().toString());
                dialog.dismiss();

            }

        });





    }

    private void get_Report_Api(String report_text) {
        param.put(Consts.PRODUCT_ID, productDTO.getId());
        param.put(Consts.USER_ID, userDTO.getId());
        param.put(Consts.REASON, report_text);
        new HttpsRequest(Consts.PRODUCT_REPORT, param, mContext).stringPost("TAG", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        Toast.makeText(ProductDetailsActivity.this, "Your Report Sucessfully Submitted", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                    e.printStackTrace();
                    }
                } else {
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void Add_ToFav() {
        param.put(Consts.PRODUCT_ID, productDTO.getId());
        param.put(Consts.USER_ID, userDTO.getId());
        new HttpsRequest(Consts.ADDTOFAV, param, mContext).stringPost("TAG", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                     String status=response.getString("status");
                     if (status.equals("1")){
                         binding.heart.setImageResource(R.drawable.ic_baseline_bookmark);
                     } else {
                         binding.heart.setImageResource(R.drawable.ic_baseline_bookmark_border);
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

    private void get_Product_Details(String product_id) {
        binding.MainLinear.setVisibility(View.VISIBLE);
        param.put(Consts.PRODUCT_ID, product_id);
        param.put(Consts.USER_ID, userDTO.getId());
        new HttpsRequest("Productdetail", param, mContext).stringPost("TAG", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        Log.e("check response",response.toString());
                        productDetailDTO = new Gson().fromJson(response.getJSONObject("data").toString(), ProductDetailDTO.class);
                        setDATA();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    private void setDATA() {
       // binding.MainLinear.setVisibility(View.VISIBLE);
        binding.setProductdetails(productDetailDTO);

        binding.ratingBar.setRating(Float.parseFloat(productDetailDTO.getAverage_rating()));
        binding.rBRate.setRating(Float.parseFloat(productDetailDTO.getAverage_rating()));


        if (productDetailDTO.getFev_status().equals("1")) {
            binding.heart.setImageResource(R.drawable.ic_baseline_bookmark);
        } else {
            binding.heart.setImageResource(R.drawable.ic_baseline_bookmark_border);
        }


        if (productDetailDTO.getAddtocartstatus().equals("1")){
            binding.tvOrder.setText("Go To Cart");

        }else {
            binding.tvOrder.setText("Add To Cart");
        }


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
        binding.relatedRv.setLayoutManager(linearLayoutManager);
        AdapterNearBY adapterNearBY = new AdapterNearBY(mContext, productDetailDTO.getRelated_product(),10);
        binding.relatedRv.setAdapter(adapterNearBY);


        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        binding.RvRatingReview.setLayoutManager(linearLayoutManager2);
        AdapterProductReview adapterProductReview = new AdapterProductReview(mContext, productDetailDTO.getProduct_review(),3);
        binding.RvRatingReview.setAdapter(adapterProductReview);


    }


    private void Add_to_Cart() {
        ProjectUtils.showProgressDialog(mContext, true, getResources().getString(R.string.please_wait));



        param.put(Consts.USER_ID, userDTO.getId());
        param.put(Consts.PRODUCT_ID, productDTO.getId());
        new HttpsRequest(Consts.ADDTOCART, param, ProductDetailsActivity.this).stringPost("TAG", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    Toast.makeText(ProductDetailsActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                    binding.tvOrder.setText("Go to Cart");
                    get_Product_Details(product_id);
                 //   startActivity(new Intent(ProductDetailsActivity.this,AddToCartActivity.class));
                } else {
                    ProjectUtils.showToast(ProductDetailsActivity.this, msg);
                }
            }
        });


    }


    @Override
    public void onSliderClick(BaseSliderView slider) {

        Intent intent = new Intent(ProductDetailsActivity.this, ZoomImageActivity.class);
        intent.putExtra("image1", Consts.USER_IMAGE_URL + productDetailDTO.getImage1());
        intent.putExtra("image2", Consts.USER_IMAGE_URL + productDetailDTO.getImage2());
        intent.putExtra("image3", Consts.USER_IMAGE_URL + productDetailDTO.getImage3());
        startActivity(intent);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }


    private int offered(Float miniprice, Float maxiprice) {
        int percentage, exactValue;
        percentage = (int) ((maxiprice - miniprice));
        exactValue = (int) ((percentage * 100) / maxiprice);
        return exactValue;

    }


    @Override
    public void onClick(View v) {}


    private void Login_Dialoge() {
        assert mContext != null;
        Dialog dialog = new Dialog(mContext);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new
                    ColorDrawable(Color.TRANSPARENT));
        }

        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.dialoge_login, null);

        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
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
            startActivity(new Intent(mContext, CustomerLoginActivity.class));

        });

    }


}






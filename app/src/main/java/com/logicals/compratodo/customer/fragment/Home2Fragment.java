package com.logicals.compratodo.customer.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.gson.Gson;
import com.logicals.compratodo.customer.activity.CategoriesListActivity;
import com.logicals.compratodo.R;
import com.logicals.compratodo.customer.adapter.AdapterCategory;
import com.logicals.compratodo.customer.adapter.AdapterNearBY;
import com.logicals.compratodo.customer.adapter.AdapterNewArrival;
import com.logicals.compratodo.customer.UI.home.ViewAllNearProduct;
import com.logicals.compratodo.customer.UI.home.ViewAllNewArrival;
import com.logicals.compratodo.https.HttpsRequest;
import com.logicals.compratodo.interfacess.Consts;
import com.logicals.compratodo.interfacess.Helper;
import com.logicals.compratodo.login_signup.ChooseRoleActivity;
import com.logicals.compratodo.login_signup.VendorLoginActivity;
import com.logicals.compratodo.model.HomeDTO;
import com.logicals.compratodo.model.SliderDTO;
import com.logicals.compratodo.model.UserDTO;
import com.logicals.compratodo.preferences.SharedPrefrence;

import org.json.JSONObject;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Home2Fragment extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{

    static String TAG= Home2Fragment.class.getSimpleName();
    SharedPrefrence sharedPrefrence;
    private SliderLayout mDemoSlider;
    CardView productdetails;
    CircleImageView subcat;
    TextView viewmore,Signup_page,Login_page;
    TextView catogeries,tvViewAllNewArrival;
    RecyclerView  rvNearByProduct,rvCategory,rvNewArrival;
    UserDTO userDTO;
    HashMap<String,String>param=new HashMap<>();
    HomeDTO homeDTO;
    LinearLayoutManager linearLayoutManager;
    GridLayoutManager gridLayoutManager;
    AdapterNearBY adapterNearBY;
    AdapterCategory adapterCategory;
    AdapterNewArrival adapterNewArrival;
    LinearLayout Login_linear,Login_linear2;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home2, container, false);
        sharedPrefrence = SharedPrefrence.getInstance(getActivity());
        userDTO = sharedPrefrence.getParentUser(Consts.USER_DTO);
        catogeries = root.findViewById(R.id.catogeries);
        final TextView textView = root.findViewById(R.id.text_home);
        viewmore=root.findViewById(R.id.viewmore);
        rvNearByProduct=root.findViewById(R.id.rvNearByProduct);
        rvCategory=root.findViewById(R.id.rvCategory);
        rvNewArrival=root.findViewById(R.id.rvNewArrival);
        mDemoSlider = (SliderLayout)root.findViewById(R.id.slider);
        tvViewAllNewArrival=root.findViewById(R.id.tvViewAllNewArrival);
        Login_linear=root.findViewById(R.id.Login_linear);
        Signup_page=root.findViewById(R.id.Signup_page);
        Login_page=root.findViewById(R.id.Login_page);
        Login_linear2=root.findViewById(R.id.Login_linear2);


        if (sharedPrefrence.isLoggedIn()){
            Login_linear.setVisibility(View.GONE);
        }




        Signup_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ChooseRoleActivity.class)
                        .putExtra("intent","home")
                );


            }
        });


        Login_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ChooseRoleActivity.class)
                .putExtra("intent","login")

                );
            }
        });


        Login_linear2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sharedPrefrence.isLoggedIn()){

                    Logout_Dialoge();

                }else {
                    startActivity(new Intent(getActivity(), VendorLoginActivity.class));
                }


            }
        });


        getHomeData();

        viewmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ViewAllNearProduct.class));
            }
        });

        tvViewAllNewArrival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ViewAllNewArrival.class));
            }
        });




        return root;
    }

    private void Logout_Dialoge() {
            assert getActivity() != null;
            Dialog dialog = new Dialog(getActivity());
            if(dialog.getWindow() != null){
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }

            View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialoge_delete_card,null);
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
             textMode.setText("Please Logout First, For Start Selling!!");


            buttonCancel.setOnClickListener(v -> {
                dialog.dismiss();

            });



            buttonOk.setOnClickListener(v -> {
                dialog.dismiss();
                sharedPrefrence.logout2();

            });
        }


    private void getHomeData() {
        param.put(Consts.USER_ID,userDTO.getId());
        new HttpsRequest(Consts.USERHOME,param,getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if(flag){
                    try {
                    homeDTO= new Gson().fromJson(response.getJSONObject("data").toString(),HomeDTO.class); }
                    catch (Exception e){ e.printStackTrace(); }

                    setDATA();
                }else {
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setDATA() {
        for(SliderDTO sliderDTO: homeDTO.getSlider()){
            DefaultSliderView defaultSliderView = new DefaultSliderView(getActivity());
            defaultSliderView.image( Consts.USER_IMAGE_URL+sliderDTO.getImage()).setScaleType(BaseSliderView.ScaleType.Fit);
            mDemoSlider.addSlider(defaultSliderView);
        }


        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(10000);
        mDemoSlider.addOnPageChangeListener(this);


        linearLayoutManager= new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL,false);
        rvNearByProduct.setLayoutManager(linearLayoutManager);
        adapterNearBY = new AdapterNearBY(getActivity(),homeDTO.getNearbyproduct(),0);
        rvNearByProduct.setAdapter(adapterNearBY);


        gridLayoutManager= new GridLayoutManager(getActivity(),3,RecyclerView.VERTICAL,false);
        rvCategory.setLayoutManager(gridLayoutManager);
        adapterCategory = new AdapterCategory(getActivity(),homeDTO.getMaincate());
        rvCategory.setAdapter(adapterCategory);

        linearLayoutManager= new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL,false);
        rvNewArrival.setLayoutManager(linearLayoutManager);
        adapterNewArrival = new AdapterNewArrival(getActivity(),homeDTO.getNearbyproduct());
        rvNewArrival.setAdapter(adapterNewArrival);

        catogeries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              Intent in =new Intent(getActivity(), CategoriesListActivity.class);
              in.putExtra(Consts.DTO,homeDTO);
              startActivity(in);

            }
        });


    }

    @Override
    public void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }


    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(getActivity(),slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {}

    @Override
    public void onResume() {
        super.onResume();
        getHomeData();
    }
}



package com.logicals.compratodo.customer.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.logicals.compratodo.R;

import java.util.HashMap;

public class ZoomImageActivity extends AppCompatActivity  {
    private SliderLayout mDemoSlider;
    HashMap<String, String> file_maps;
    String img1,img2,img3;
    ImageView iv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_image);
        mDemoSlider = (SliderLayout) findViewById(R.id.slider);
        iv_back=findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });

        if (getIntent() != null) {
             img1 = getIntent().getStringExtra("image1");

             img2 = getIntent().getStringExtra("image2");

             img3 = getIntent().getStringExtra("image3");


            Log.e("check image", "" + img1 + " " + img2 + " " + img3);

        }


        //////////////start slider////////////////////





        file_maps = new HashMap<String, String>();
        file_maps.put("img1", img1);
        file_maps.put("img2", img2);
        file_maps.put("img3", img3);
        for (String name : file_maps.keySet()) {
            DefaultSliderView defaultSliderView = new DefaultSliderView(ZoomImageActivity.this);
            defaultSliderView
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);
            mDemoSlider.addSlider(defaultSliderView);
            mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
            mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            mDemoSlider.setCustomAnimation(new DescriptionAnimation());
            //  binding.mDemoSlider.setDuration(3000);
            mDemoSlider.stopAutoCycle();


        }




    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
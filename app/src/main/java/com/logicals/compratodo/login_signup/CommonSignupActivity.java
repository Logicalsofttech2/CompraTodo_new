package com.logicals.compratodo.login_signup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.logicals.compratodo.R;
import com.logicals.compratodo.ui.loginsignup.TabsAdapter1;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommonSignupActivity extends AppCompatActivity {

    TextView login;
    TextView register;
    CircleImageView shop_image;
    TabLayout tabLayout;
    ViewPager viewPager;
    TextView upload_product,shop_name,about_shop;
    String userid,get_user_id;
    Switch msg_switch;
    public static String  strShop_id;
    int status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_signup);
        register= findViewById(R.id.register);

        tabLayout=findViewById(R.id.tabLayout);
        viewPager=findViewById(R.id.viewPager);
        tabLayout.addTab(tabLayout.newTab().setText("User"));
        tabLayout.addTab(tabLayout.newTab().setText("Seller"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final TabsAdapter1 adapter = new TabsAdapter1(getSupportFragmentManager(), this, tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });





    }


}

package com.logicals.compratodo.ui.loginsignup;

import android.os.Bundle;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.logicals.compratodo.R;
import com.logicals.compratodo.ui.loginsignup.TabsAdapter2;

import de.hdodenhof.circleimageview.CircleImageView;

public class TabActivity extends AppCompatActivity {
    TextView login;
    TextView register;
    CircleImageView shop_image;
    TabLayout tabLayout;
    ViewPager viewPager;
    TextView upload_product,shop_name,about_shop;
    String userid,get_user_id;

    Switch msg_switch;
    public  static String  strShop_id;
    int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        tabLayout.addTab(tabLayout.newTab().setText("Buyer"));
        tabLayout.addTab(tabLayout.newTab().setText("Seller"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final TabsAdapter2 adapter = new TabsAdapter2(getSupportFragmentManager(), this, tabLayout.getTabCount(), get_user_id);
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

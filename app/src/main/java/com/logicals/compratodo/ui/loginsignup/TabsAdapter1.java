package com.logicals.compratodo.ui.loginsignup;
import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabsAdapter1 extends FragmentPagerAdapter {
    int totalTabs;
    private Context context;
    String user_id;


    public TabsAdapter1(FragmentManager fm, Context context, int totalTabs) {
        super(fm);
        this.context = context;
        this.totalTabs = totalTabs;

    }



    @Override
    public Fragment getItem(int position){
        Fragment fragment=null;



        switch (position){
            case 0:
                fragment=new CustomerSignupFragment();

                break;
            case 1:
                fragment=new VendorSignupFragment();

                break;

            default:
                fragment=null;
                break;
        }
        return  fragment;
    }







    @Override
    public int getCount() {
        return totalTabs;
    }
}

package com.logicals.compratodo.ui.loginsignup;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabsAdapter2 extends FragmentPagerAdapter {
    int totalTabs;
    private Context context;
    String user_id;

    public TabsAdapter2(FragmentManager fm, Context context, int totalTabs, String user_id) {
        super(fm);
        this.context = context;
        this.totalTabs = totalTabs;
        this.user_id=user_id;
    }



    @Override
    public Fragment getItem(int position){
        Fragment fragment=null;

        Bundle bundle = new Bundle();
        bundle.putString("user_id", user_id);

        switch (position){
            case 0:
                fragment=new CustomerLoginFragment();
                fragment.setArguments(bundle);
                break;
            case 1:
                fragment=new VendorLoginFragment();
                fragment.setArguments(bundle);
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

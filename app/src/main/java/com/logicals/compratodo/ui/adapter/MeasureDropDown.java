package com.logicals.compratodo.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.logicals.compratodo.R;
import com.logicals.compratodo.model.CategoryDTO;
import com.logicals.compratodo.model.MeasureDTO;

import java.util.ArrayList;

public class MeasureDropDown extends ArrayAdapter {

    private Context mContext;
    private ArrayList<MeasureDTO> foodTypeLists;
    private LayoutInflater inflater;

    public MeasureDropDown(Context mContext, int resource, ArrayList<MeasureDTO>foodTypeLists) {
        super(mContext, resource, foodTypeLists);
        this.mContext = mContext;
        this.foodTypeLists = foodTypeLists;
        inflater = LayoutInflater.from(mContext);
    }

    @SuppressLint("SetTextI18n")
    public View getCustomView(int position, View convertView, ViewGroup parent) {

        View layout = inflater.inflate(R.layout.food_type_list, null);

        TextView foodItemName = layout.findViewById(R.id.food_item);

//        CountryCodeModel countryCodeModel = foodTypeLists.get(position);

        foodItemName.setText(foodTypeLists.get(position).getName());

        return layout;

    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        return getCustomView(position, convertView, parent);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        return getCustomView(position, convertView, parent);
    }

}



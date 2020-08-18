package com.logicals.compratodo.model;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.logicals.compratodo.R;

import java.io.Serializable;

import static com.logicals.compratodo.interfacess.Consts.USER_IMAGE_URL;

public class ProductDTO implements Serializable {

          String   id="";
          String   vendor_id="";
          String   product_name="";
          String   max_price="";
          String   min_price="";
          String   available_stock="";
          String   status="";
          String   about="";
          String   created_date="";
          String   cat_id="";
          String   sub_cat_id="";
          String   unit="";
          String   minimumquantity="";
          String   total="";
          String   image1="";
          String   image2="";
          String   image3="";
          String   quantity="";
          String   final_amount="";
          String   offer;
          String   product_id;
          String total_amount;






/*
    public static int offer(Float miniprice,Float maxiprice) {
       int percentage,exactValue;
       percentage = (int)((maxiprice - miniprice) );
       exactValue= (int) (( percentage*100)/maxiprice);
       return exactValue;
    }*/

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    @BindingAdapter({"image"})
    public static void loadPosterImage(ImageView imageView, String imgUrl){
        String imagePath = USER_IMAGE_URL + imgUrl;
        Glide.with(imageView.getContext())
                .load(imagePath)
                .placeholder(R.drawable.ic_person)
                .error(R.drawable.ic_person)
                .into(imageView);

    }


    /*public int Offer(Float miniprice,Float maxiprice){
        int percentage,exactValue;
        percentage = (int)((maxiprice - miniprice) );
        exactValue= (int) (( percentage*100)/maxiprice);
        return exactValue;
    }*/

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public void getOffer(String offer) {
        this.offer = offer;
    }

    public String getFinal_amount() {
        return final_amount;
    }

    public void setFinal_amount(String final_amount) {
        this.final_amount = final_amount;
    }

    public String getQuantity() {
        return quantity;
    }

    public String setQuantity(String quantity) {
        this.quantity = quantity;
        return quantity;
    }

    private boolean isSelected = false;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(String vendor_id) {
        this.vendor_id = vendor_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }


    public String getMax_price() {
        return max_price;
    }

    public void setMax_price(String max_price) {
        this.max_price = max_price;
    }

    public String getMin_price() {
        return min_price;
    }

    public void setMin_price(String min_price) {
        this.min_price = min_price;
    }

    public String getAvailable_stock() {
        return available_stock;
    }

    public void setAvailable_stock(String available_stock) {
        this.available_stock = available_stock;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getSub_cat_id() {
        return sub_cat_id;
    }

    public void setSub_cat_id(String sub_cat_id) {
        this.sub_cat_id = sub_cat_id;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getMinimumquantity() {
        return minimumquantity;
    }

    public void setMinimumquantity(String minimumquantity) {
        this.minimumquantity = minimumquantity;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }
}

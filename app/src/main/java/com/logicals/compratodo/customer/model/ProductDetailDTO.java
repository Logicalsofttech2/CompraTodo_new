package com.logicals.compratodo.customer.model;

import android.widget.RatingBar;

import androidx.databinding.BindingAdapter;

import com.google.gson.annotations.SerializedName;
import com.logicals.compratodo.model.ProductDTO;

import java.util.ArrayList;

public class ProductDetailDTO {

    // String rating;

String  average_rating,userno,fev_status,addtocartstatus,fivestar,fourstar,threestar,towstar,onestar;


     String image1,image2,image3;

    public String getAverage_rating() {
        return average_rating;
    }

    public void setAverage_rating(String average_rating) {
        this.average_rating = average_rating;
    }

    public String getFivestar() {
        return fivestar;
    }

    public void setFivestar(String fivestar) {
        this.fivestar = fivestar;
    }

    public String getFourstar() {
        return fourstar;
    }

    public void setFourstar(String fourstar) {
        this.fourstar = fourstar;
    }

    public String getThreestar() {
        return threestar;
    }

    public void setThreestar(String threestar) {
        this.threestar = threestar;
    }

    public String getTwostar() {
        return towstar;
    }

    public void setTwostar(String twostar) {
        this.towstar = twostar;
    }

    public String getOnestar() {
        return onestar;
    }

    public void setOnestar(String onestar) {
        this.onestar = onestar;
    }

    public String getAddtocartstatus() {
        return addtocartstatus;
    }

    public void setAddtocartstatus(String addtocartstatus) {
        this.addtocartstatus = addtocartstatus;
    }

    public String getUserno() {
        return userno;
    }

    public void setUserno(String userno) {
        this.userno = userno;
    }

    public String getFev_status() {
        return fev_status;
    }

    public void setFev_status(String fev_status) {
        this.fev_status = fev_status;
    }

    /*@BindingAdapter("android:rating")
    public static void setRating(RatingBar view, String rating) {
        if (view!=null)
        {
            float rate= Float.parseFloat(rating);
            view.setRating(rate);
        }

    }*/





    ArrayList<ProductDTO> related_product = new ArrayList<>();

    ArrayList<ProductReviewDTO> product_review = new ArrayList<>();


    public ArrayList<ProductDTO> getProduct() {
        return related_product;
    }

    public void setProduct(ArrayList<ProductDTO> slider) {
        this.related_product = slider;
    }

    public ArrayList<ProductDTO> getRelated_product() {
        return related_product;
    }

    public void setRelated_product(ArrayList<ProductDTO> related_product) {
        this.related_product = related_product;
    }

    public ArrayList<ProductReviewDTO> getProduct_review() {
        return product_review;
    }

    public void setProduct_review(ArrayList<ProductReviewDTO> product_review) {
        this.product_review = product_review;
    }

 /*   public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }*/

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

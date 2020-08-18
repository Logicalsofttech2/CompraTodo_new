package com.logicals.compratodo.model;

import java.io.Serializable;
import java.util.ArrayList;

public class HomeDTO implements Serializable {

   String user_id,cart_count;



    ArrayList<SliderDTO>slider= new ArrayList<>();
    ArrayList<MainCategoryDTO>maincate= new ArrayList<>();
    ArrayList<ProductDTO>new_arrival= new ArrayList<>();
    ArrayList<ProductDTO>nearbyproduct= new ArrayList<>();


    public ArrayList<SliderDTO> getSlider() {
        return slider;
    }

    public void setSlider(ArrayList<SliderDTO> slider) {
        this.slider = slider;
    }

    public ArrayList<MainCategoryDTO> getMaincate() {
        return maincate;
    }

    public void setMaincate(ArrayList<MainCategoryDTO> maincate) {
        this.maincate = maincate;
    }

    public ArrayList<ProductDTO> getNew_arrival() {
        return new_arrival;
    }

    public void setNew_arrival(ArrayList<ProductDTO> new_arrival) {
        this.new_arrival = new_arrival;
    }

    public ArrayList<ProductDTO> getNearbyproduct() {
        return nearbyproduct;
    }

    public void setNearbyproduct(ArrayList<ProductDTO> nearbyproduct) {
        this.nearbyproduct = nearbyproduct;
    }


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }


    public String getCart_count() {
        return cart_count;
    }

    public void setCart_count(String cart_count) {
        this.cart_count = cart_count;
    }
}

package com.logicals.compratodo.vendor.model;

import android.os.Parcel;
import android.os.Parcelable;

public class MyProductListDTO implements Parcelable {

              String order_id= "";
              String user_id= "";
              String vendor_id= "";
              String product_id= "";
              String address_id= "";
              String quanity= "";
              String price= "";
              String sub_total= "";
              String total_amount= "";
              String payment_method= "";
              String order_status= "";
              String payment_status= "";
              String trxn_id= "";
              String create_date= "";
              String product_image= "";
              String product_name= "";
              String username= "",dateshow="",order_time="";

    protected MyProductListDTO(Parcel in) {
        order_id = in.readString();
        user_id = in.readString();
        vendor_id = in.readString();
        product_id = in.readString();
        address_id = in.readString();
        quanity = in.readString();
        price = in.readString();
        sub_total = in.readString();
        total_amount = in.readString();
        payment_method = in.readString();
        order_status = in.readString();
        payment_status = in.readString();
        trxn_id = in.readString();
        create_date = in.readString();
        product_image = in.readString();
        product_name = in.readString();
        username = in.readString();
    }


    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public String getDateshow() {
        return dateshow;
    }

    public void setDateshow(String dateshow) {
        this.dateshow = dateshow;
    }

    public static final Creator<MyProductListDTO> CREATOR = new Creator<MyProductListDTO>() {
        @Override
        public MyProductListDTO createFromParcel(Parcel in) {
            return new MyProductListDTO(in);
        }

        @Override
        public MyProductListDTO[] newArray(int size) {
            return new MyProductListDTO[size];
        }
    };

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(String vendor_id) {
        this.vendor_id = vendor_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }

    public String getQuanity() {
        return quanity;
    }

    public void setQuanity(String quanity) {
        this.quanity = quanity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSub_total() {
        return sub_total;
    }

    public void setSub_total(String sub_total) {
        this.sub_total = sub_total;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getTrxn_id() {
        return trxn_id;
    }

    public void setTrxn_id(String trxn_id) {
        this.trxn_id = trxn_id;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(order_id);
        dest.writeString(user_id);
        dest.writeString(vendor_id);
        dest.writeString(product_id);
        dest.writeString(address_id);
        dest.writeString(quanity);
        dest.writeString(price);
        dest.writeString(sub_total);
        dest.writeString(total_amount);
        dest.writeString(payment_method);
        dest.writeString(order_status);
        dest.writeString(payment_status);
        dest.writeString(trxn_id);
        dest.writeString(create_date);
        dest.writeString(product_image);
        dest.writeString(product_name);
        dest.writeString(username);
    }
}

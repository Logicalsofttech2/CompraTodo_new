package com.logicals.compratodo.customer.model;


import java.util.ArrayList;

public class OrderDetailDTo {
    String delivery_fee,order_date,order_status,total_amount,order_id,payment_method;

    ArrayList<PendingProductDto> product= new ArrayList<>();

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getDelivery_fee() {
        return delivery_fee;
    }

    public void setDelivery_fee(String delivery_fee) {
        this.delivery_fee = delivery_fee;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public ArrayList<PendingProductDto> getPendingProductDtos() {
        return product;
    }

    public void setPendingProductDtos(ArrayList<PendingProductDto> pendingProductDtos) {
        this.product = pendingProductDtos;
    }
}

package com.logicals.compratodo.customer.model;

import android.content.Intent;
import android.view.View;
import com.logicals.compratodo.customer.UI.myorders.OrderDetailsActivity;

public class MyOrderDTO {
    String order_iteam, order_status, order_id, checkout_price, order_date;
    public String getOrder_status() {
        if (order_status.equals("0")) {
            order_status = "Order Recieved";
        } else if (order_status.equals("1")) {
            order_status = "Preparing";
        } else if (order_status.equals("2")) {
            order_status = "Ready";
        } else if (order_status.equals("3")) {
            order_status = "On the Way";
        } else if (order_status.equals("4")) {
            order_status = "Delivered";
        }else if (order_status.equals("5")) {
            order_status = "Cancel";
        }

        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public void onClickItem(View view){
        view.getContext().startActivity(new Intent(view.getContext(), OrderDetailsActivity.class).putExtra("order_id",order_id));
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_iteam() {
        return order_iteam;
    }

    public void setOrder_iteam(String order_iteam) {
        this.order_iteam = order_iteam;
    }

    public String getCheckout_price() {
        return checkout_price;
    }

    public void setCheckout_price(String checkout_price) {
        this.checkout_price = checkout_price;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }
}

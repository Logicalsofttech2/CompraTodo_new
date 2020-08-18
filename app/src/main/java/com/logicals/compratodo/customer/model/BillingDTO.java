package com.logicals.compratodo.customer.model;

public class BillingDTO {
    String  order_iteam,total_pay,sub_total,delivery_charge,total_discount,final_amount;


    public String getOrder_iteam() {
        return order_iteam;
    }

    public void setOrder_iteam(String order_iteam) {
        this.order_iteam = order_iteam;
    }

    public String getTotal_pay() {
        return total_pay;
    }

    public void setTotal_pay(String total_pay) {
        this.total_pay = total_pay;
    }

    public String getSub_total() {
        return sub_total;
    }

    public void setSub_total(String sub_total) {
        this.sub_total = sub_total;
    }

    public String getDelivery_charge() {
        return delivery_charge;
    }

    public void setDelivery_charge(String delivery_charge) {
        this.delivery_charge = delivery_charge;
    }

    public String getTotal_discount() {
        return total_discount;
    }

    public void setTotal_discount(String total_discount) {
        this.total_discount = total_discount;
    }

    public String getFinal_amount() {
        return final_amount;
    }

    public void setFinal_amount(String final_amount) {
        this.final_amount = final_amount;
    }
}

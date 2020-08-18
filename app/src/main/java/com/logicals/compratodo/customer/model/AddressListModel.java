package com.logicals.compratodo.customer.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddressListModel implements Parcelable {
    private boolean isSelectedA;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("pin_code")
    @Expose
    private String pinCode;
    @SerializedName("area")
    @Expose
    private String area;
    @SerializedName("house_no")
    @Expose
    private String houseNo;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("landmark")
    @Expose
    private String landmark;

    protected AddressListModel(Parcel in) {
        isSelectedA = in.readByte() != 0;
        id = in.readString();
        userId = in.readString();
        state = in.readString();
        city = in.readString();
        pinCode = in.readString();
        area = in.readString();
        houseNo = in.readString();
        type = in.readString();
        mobile = in.readString();
        landmark = in.readString();
    }

    public static final Creator<AddressListModel> CREATOR = new Creator<AddressListModel>() {
        @Override
        public AddressListModel createFromParcel(Parcel in) {
            return new AddressListModel(in);
        }

        @Override
        public AddressListModel[] newArray(int size) {
            return new AddressListModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    public boolean isSelectedA() {
        return isSelectedA;
    }

    public void setSelectedA(boolean selectedA) {
        isSelectedA = selectedA;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isSelectedA ? 1 : 0));
        dest.writeString(id);
        dest.writeString(userId);
        dest.writeString(state);
        dest.writeString(city);
        dest.writeString(pinCode);
        dest.writeString(area);
        dest.writeString(houseNo);
        dest.writeString(type);
        dest.writeString(mobile);
        dest.writeString(landmark);
    }
}

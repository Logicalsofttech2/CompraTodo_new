package com.logicals.compratodo.model;

import java.io.Serializable;

public class SubCategoryDTO implements Serializable {
    
    
    
                String     id="";
                String     main_cate_id="";
                String     cat_id="";
                String     name="";
                String     image="";
                String     status="";
                String     created_date="";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMain_cate_id() {
        return main_cate_id;
    }

    public void setMain_cate_id(String main_cate_id) {
        this.main_cate_id = main_cate_id;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }
}

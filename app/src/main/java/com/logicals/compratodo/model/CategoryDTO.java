package com.logicals.compratodo.model;

import java.io.Serializable;

public class CategoryDTO implements Serializable {
    
                 String  id="";
                 String  main_cate="";
                 String  name="";
                 String  image="";
                 String  status="";


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMain_cate() {
        return main_cate;
    }

    public void setMain_cate(String main_cate) {
        this.main_cate = main_cate;
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
}

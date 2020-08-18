package com.logicals.compratodo.model;

import java.io.Serializable;

public class MeasureDTO implements Serializable {
       String   id="";
       String   cat_id="";
       String   name="";


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}

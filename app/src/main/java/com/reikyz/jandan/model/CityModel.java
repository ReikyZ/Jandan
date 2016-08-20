package com.reikyz.jandan.model;

import java.io.Serializable;

public class CityModel implements Serializable {
    String display_name;
    Integer id;
    String china_loc;
    String type;


    public CityModel() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public CityModel(String display_name) {
        this.display_name = display_name;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public Integer getId() {
        return id;
    }

    public String getChina_loc() {
        return china_loc;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setChina_loc(String china_loc) {
        this.china_loc = china_loc;
    }

    @Override
    public String toString() {
        return "CityModel{" +
                "display_name='" + display_name + '\'' +
                ", id=" + id +
                ", china_loc='" + china_loc + '\'' +
                '}';
    }
}

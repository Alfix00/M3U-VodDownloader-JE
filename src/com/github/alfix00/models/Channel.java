package com.github.alfix00.models;

import java.io.Serial;
import java.io.Serializable;

public class Channel implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String name;
    private String category;
    private String logo;
    private String url;
    private int index;


    public Channel(String name, String url){
        this.name = name;
        this.url = url;
    }

    public Channel(String name, String url, String logo){
        this.name = name;
        this.url = url;
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
       return logo;
    }

    public void setLogo(String info) {
        this.logo = info;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}

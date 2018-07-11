package com.example.asus.fp_ams_mnmg;

/**
 * Created by Asus on 14-04-2018.
 */

public class ImageUploader {
    public String name;
    public String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ImageUploader(String name,String url){
        this.name = name;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

package com.aiwue.model;

/**
 * 位置信息模型
 * Created by Yibao on 2017年4月19日14:00:47
 * Copyright (c) 2017 aiwue.com All rights reserved
 */
public class LocationInfo {
    private String location; // 地点
    private String  city;// 城市
    private Float longitude;// 经度
    private Float latitude; // 纬度

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }
}

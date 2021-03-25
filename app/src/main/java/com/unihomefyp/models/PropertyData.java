package com.unihomefyp.models;

import android.net.Uri;

public class PropertyData {

    private String name;
    private String county;
    private String address;
    private String price;
    private String imageUrl;
    private String wifi;
    private String wifiBill;
    private String bins;
    private String electricity;
    private String heating;
    private String washing;
    private String dryer;
    private String parking;
    private String dish;
    private String tv;
    private String kitchen;
    private String beds;
    private String availableBeds;
    private String bathrooms;
    private String period;
    private String key;
    private String active;
    private String uid;
    private Long ratingCount;
    private Double ratingValue;
    String imagename;
    Uri image;


    public PropertyData(){

    }




    public PropertyData(String name, String county, String address, String price, String imageUrl, String wifi, String wifiBill, String bins, String electricity, String heating, String washing, String dryer, String parking, String dish, String tv, String kitchen, String beds, String availableBeds, String bathrooms, String period, String active, String uid) {
        this.name = name;
        this.county = county;
        this.address = address;
        this.price = price;
        this.imageUrl = imageUrl;
        this.wifi = wifi;
        this.bins = bins;
        this.electricity = electricity;
        this.heating = heating;
        this.washing = washing;
        this.dryer = dryer;
        this.parking = parking;
        this.dish = dish;
        this.tv = tv;
        this.kitchen = kitchen;
        this.beds = beds;
        this.availableBeds = availableBeds;
        this.bathrooms = bathrooms;
        this.period = period;
        this.active = active;
        this.uid = uid;
        this.wifiBill = wifiBill;
        this.imagename = imagename;
        this.image = image;

    }

    public String getUid() {
        return uid;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getWifi() {
        return wifi;
    }

    public void setWifi(String wifi) {
        this.wifi = wifi;
    }

    public String getBins() {
        return bins;
    }

    public void setBins(String bins) {
        this.bins = bins;
    }

    public String getElectricity() {
        return electricity;
    }

    public void setElectricity(String electricity) {
        this.electricity = electricity;
    }

    public String getHeating() {
        return heating;
    }

    public void setHeating(String heating) {
        this.heating = heating;
    }

    public String getWashing() {
        return washing;
    }

    public void setWashing(String washing) {
        this.washing = washing;
    }

    public String getDryer() {
        return dryer;
    }

    public void setDryer(String dryer) {
        this.dryer = dryer;
    }

    public String getParking() {
        return parking;
    }

    public void setParking(String parking) {
        this.parking = parking;
    }

    public String getDish() {
        return dish;
    }

    public void setDish(String dish) {
        this.dish = dish;
    }

    public String getTv() {
        return tv;
    }

    public void setTv(String tv) {
        this.tv = tv;
    }

    public String getKitchen() {
        return kitchen;
    }

    public void setKitchen(String kitchen) {
        this.kitchen = kitchen;
    }

    public String getBeds() {
        return beds;
    }

    public void setBeds(String beds) {
        this.beds = availableBeds;
    }

    public String getAvailableBeds() {
        return availableBeds;
    }

    public void setAvailableBeds(String availableBeds) {
        this.availableBeds = availableBeds;
    }

    public String getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(String bathrooms) {
        this.bathrooms = bathrooms;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public Long getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(Long ratingCount) {
        this.ratingCount = ratingCount;
    }

    public Double getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(Double ratingValue) {
        this.ratingValue = ratingValue;
    }

    public String getWifiBill() {
        return wifiBill;
    }

    public void setWifiBill(String wifiBill) {
        this.wifiBill = wifiBill;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

    public String getImagename() {
        return imagename;
    }

    public void setImagename(String imagename) {
        this.imagename = imagename;
    }

}
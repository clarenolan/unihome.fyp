package com.unihomefyp.models;

import java.util.Map;

public class ReviewModel {
    private float ratingValue2;
    private String propertyID, review,name,uid;
    private Map<String,Object> reviewTimestamp;

    //    private Long ratingCount;
    private Double ratingValue;


    public ReviewModel(float ratingValue2, String propertyID, String review, String name, String uid, Map<String, Object> reviewTimestamp) {
        this.ratingValue2 = ratingValue2;
        this.propertyID = propertyID;
        this.review = review;
        this.name = name;
        this.uid = uid;
        this.reviewTimestamp = reviewTimestamp;
    }

    public ReviewModel() {
    }

    public float getRatingValue2() {
        return ratingValue2;
    }

    public void setRatingValue2(float ratingValue2) {
        this.ratingValue2 = ratingValue2;
    }
    public String getPropertyID() {
        return propertyID;
    }

    public void setPropertyID(String propertyID) {
        this.propertyID = propertyID;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Map<String, Object> getReviewTimestamp() {
        return reviewTimestamp;
    }

    public void setReviewTimestamp(Map<String, Object> reviewTimestamp) {
        this.reviewTimestamp = reviewTimestamp;
    }

//    public Long getRatingCount() {
//        return ratingCount;
//    }
//
//    public void setRatingCount(Long ratingCount) {
//        this.ratingCount = ratingCount;
//    }
//
//    public Double getRatingValue() {
//        return ratingValue;
//    }
//
//    public void setRatingValue(Double ratingValue) {
//        this.ratingValue = ratingValue;
//    }

}

package com.unihomefyp.models;

public class EnquiryModel {

    String propertyKey, tiimestamp, subject, message, sName, sEmail, sPhone, key;

    public EnquiryModel() {
    }

    public EnquiryModel(String propertyKey, String tiimestamp, String subject, String message, String sName, String sEmail, String sPhone) {
        this.propertyKey = propertyKey;
        this.tiimestamp = tiimestamp;
        this.subject = subject;
        this.message = message;
        this.sName = sName;
        this.sEmail = sEmail;
        this.sPhone = sPhone;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPropertyKey() {
        return propertyKey;
    }

    public void setPropertyKey(String propertyKey) {
        this.propertyKey = propertyKey;
    }

    public String getTiimestamp() {
        return tiimestamp;
    }

    public void setTiimestamp(String tiimestamp) {
        this.tiimestamp = tiimestamp;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getsEmail() {
        return sEmail;
    }

    public void setsEmail(String sEmail) {
        this.sEmail = sEmail;
    }

    public String getsPhone() {
        return sPhone;
    }

    public void setsPhone(String sPhone) {
        this.sPhone = sPhone;
    }
}

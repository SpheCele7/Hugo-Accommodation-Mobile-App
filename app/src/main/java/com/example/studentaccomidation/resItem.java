package com.example.studentaccomidation;

public class resItem {
    String name;
    String address;
    String email;
    String telephone;
    String picture;
    String documentId;

    public resItem(){}
    public resItem(String resName, String resAddress, String resEmail, String resPhone, String picture, String documentId) {
        this.name = resName;
        this.address = resAddress;
        this.email = resEmail;
        this.telephone = resPhone;
        this.picture = picture;
        this.documentId = documentId;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getPicture() {
        return picture;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}

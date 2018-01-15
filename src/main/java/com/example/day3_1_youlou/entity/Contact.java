package com.example.day3_1_youlou.entity;

/**
 * Created by mwg on 2017/12/1.
 * 用来封装联系人基本信息的实体类
 */

public class Contact {
    private int _id;
    private String name;
    private String phone;
    private String address;
    private String email;
    private int phoneId;

    public Contact() {
    }

    public Contact(int _id, String name, String phone, String address, String email, int phoneId) {
        this._id = _id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.email = email;
        this.phoneId = phoneId;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(int phoneId) {
        this.phoneId = phoneId;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "_id=" + _id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", phoneId=" + phoneId +
                '}';
    }
}

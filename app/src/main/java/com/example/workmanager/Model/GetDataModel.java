package com.example.workmanager.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class GetDataModel {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String roll;
    private String reg;
    private String sub;
    private String phone;
    private String image;
    private String address;

    public GetDataModel() {
    }

    public GetDataModel(int id, String name, String roll, String reg, String sub, String phone, String image, String address) {
        this.id = id;
        this.name = name;
        this.roll = roll;
        this.reg = reg;
        this.sub = sub;
        this.phone = phone;
        this.image = image;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public String getReg() {
        return reg;
    }

    public void setReg(String reg) {
        this.reg = reg;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

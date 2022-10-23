package com.matias.domuapp.models;

import java.util.Date;
import java.util.List;

public class Persona {
    private Integer id;
    private String name;
    private String lastname;
    private String secondname;
    private String phone;
    private Date birthDate;
    private Direccion address;
    private List<String> data;

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Persona{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", secondname='" + secondname + '\'' +
                ", phone='" + phone + '\'' +
                ", birthDate=" + birthDate +
                ", address=" + address +
                ", data=" + data +
                '}';
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getSecondname() {
        return secondname;
    }

    public void setSecondname(String secondname) {
        this.secondname = secondname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Direccion getAddress() {
        return address;
    }

    public void setAddress(Direccion address) {
        this.address = address;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}

package com.matias.domuapp.models;

public class Profesionista {
    String id;
    String name;
    String email;
    String service;
    String address;

    public Profesionista(String id, String name, String email, String service, String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.service = service;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

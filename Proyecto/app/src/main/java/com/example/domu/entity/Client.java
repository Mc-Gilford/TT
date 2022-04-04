package com.example.domu.entity;

import java.util.List;

public class Client extends User{
    public List<Professional> getProfessionals() {
        return professionals;
    }

    public void setProfessionals(List<Professional> professionals) {
        this.professionals = professionals;
    }

    private List<Professional> professionals; /*Este sirve para guardar a los profesionales que le gustaron su servicio*/
}

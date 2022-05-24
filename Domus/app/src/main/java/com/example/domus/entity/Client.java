package com.example.domus.entity;

import java.util.List;

public class Client extends User{
    
    private List<Professional> professionals; /*Este sirve para guardar a los profesionales que le gustaron su servicio*/
    
    public List<Professional> getProfessionals() {
        return professionals;
    }

    public void setProfessionals(List<Professional> professionals) {
        this.professionals = professionals;
    }
}

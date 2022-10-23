package com.matias.domuapp.models;

import java.util.List;

public class Cliente extends Usuario {

    @Override
    public String toString() {
        return super.toString()+"Cliente{" +
                "professionals=" + professionals +
                '}';
    }

    private List<Profesional> professionals; /*Este sirve para guardar a los profesionales que le gustaron su servicio*/
    public  Cliente(){
        super();
    }
    public Cliente(String email, String password) {
        super(email, password);
    }


    public List<Profesional> getProfessionals() {
        return professionals;
    }

    public void setProfessionals(List<Profesional> professionals) {
        this.professionals = professionals;
    }

}

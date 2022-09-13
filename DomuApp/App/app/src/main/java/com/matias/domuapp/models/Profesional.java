package com.matias.domuapp.models;

import java.util.List;

public class Profesional extends Usuario {
    private List<Servicio> services;
    private String idJob;/*La cedula profesional del profesional*/
    private Float score;
    private List<Integer> qualification; /*Calificaciones por usuarios*/

    @Override
    public String toString() {
        return "Profesional{" +
                "services=" + services +
                ", idJob='" + idJob + '\'' +
                ", score=" + score +
                ", qualification=" + qualification +
                '}';
    }

    public Profesional(){

    }
    public Profesional(String email, String password) {
        super(email, password);
    }
    public List<Servicio> getServices() {
        return services;
    }

    public void setServices(List<Servicio> services) {
        this.services = services;
    }

    public String getIdJob() {
        return idJob;
    }

    public void setIdJob(String idJob) {
        this.idJob = idJob;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public List<Integer> getQualification() {
        return qualification;
    }

    public void setQualification(List<Integer> qualification) {
        this.qualification = qualification;
    }
}

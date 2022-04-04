package com.example.domu.entity;

import java.util.List;

public class Professional extends User{
    private List<Service> services;
    private String idJob;/*La cedula profesional del profesional*/
    private Float score;
    private List<Integer> qualification; /*Calificaciones por usuarios*/

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
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

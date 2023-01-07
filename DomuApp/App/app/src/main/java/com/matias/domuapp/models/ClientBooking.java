package com.matias.domuapp.models;

public class ClientBooking {
    String idHistoryBooking;
    String idClient;
    String idProfesionist;
    String destination;
    String origin;
    String time;
    String km;
    String status;
    double originLat;
    double originLng;
    double destinationLat;
    double destinationLng;

    public ClientBooking() {

    }

    public ClientBooking(String idClient, String idProfesionist, String destination, String origin, String time, String km, String status, double originLat, double originLng, double destinationLat, double destinationLng) {
        this.idClient = idClient;
        this.idProfesionist = idProfesionist;
        this.destination = destination;
        this.origin = origin;
        this.time = time;
        this.km = km;
        this.status = status;
        this.originLat = originLat;
        this.originLng = originLng;
        this.destinationLat = destinationLat;
        this.destinationLng = destinationLng;
    }

    public ClientBooking(String idHistoryBooking, String idClient, String idProfesionist, String destination, String origin, String time, String km, String status, double originLat, double originLng, double destinationLat, double destinationLng) {
        this.idHistoryBooking = idHistoryBooking;
        this.idClient = idClient;
        this.idProfesionist = idProfesionist;
        this.destination = destination;
        this.origin = origin;
        this.time = time;
        this.km = km;
        this.status = status;
        this.originLat = originLat;
        this.originLng = originLng;
        this.destinationLat = destinationLat;
        this.destinationLng = destinationLng;
    }

    public String getIdHistoryBooking() {
        return idHistoryBooking;
    }

    public void setIdHistoryBooking(String idHistoryBooking) {
        this.idHistoryBooking = idHistoryBooking;
    }

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    public String getIdProfesionist() {
        return idProfesionist;
    }

    public void setIdProfesionist(String idProfesionist) {
        this.idProfesionist = idProfesionist;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getOriginLat() {
        return originLat;
    }

    public void setOriginLat(double originLat) {
        this.originLat = originLat;
    }

    public double getOriginLng() {
        return originLng;
    }

    public void setOriginLng(double originLng) {
        this.originLng = originLng;
    }

    public double getDestinationLat() {
        return destinationLat;
    }

    public void setDestinationLat(double destinationLat) {
        this.destinationLat = destinationLat;
    }

    public double getDestinationLng() {
        return destinationLng;
    }

    public void setDestinationLng(double destinationLng) {
        this.destinationLng = destinationLng;
    }
}

package com.matias.domuapp.models;

public class Usuario {
    private String id;
    private String email;
    private String password;
    private Persona person;
    private Cuenta account;
    private Boolean idActive;/*Indica si el usuario tiene su perfil activo */
    private Double coordX;
    private Double coordY;
    private String typeUser;

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", person=" + person +
                ", account=" + account +
                ", idActive=" + idActive +
                ", coordX=" + coordX +
                ", coordY=" + coordY +
                ", typeUser='" + typeUser + '\'' +
                '}';
    }


    public Usuario(){

    }
    public Usuario(String email, String password)
    {
        this.email=email;
        this.password=password;
    }
    public String getTypeUser() {
        return typeUser;
    }

    public void setTypeUser(String typeUser) {
        this.typeUser = typeUser;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Persona getPerson() {
        return person;
    }

    public void setPerson(Persona person) {
        this.person = person;
    }

    public Cuenta getAccount() {
        return account;
    }

    public void setAccount(Cuenta account) {
        this.account = account;
    }

    public Boolean getIdActive() {
        return idActive;
    }

    public void setIdActive(Boolean idActive) {
        this.idActive = idActive;
    }

    public Double getCoordX() {
        return coordX;
    }

    public void setCoordX(Double coordX) {
        this.coordX = coordX;
    }

    public Double getCoordY() {
        return coordY;
    }

    public void setCoordY(Double coordY) {
        this.coordY = coordY;
    }
}

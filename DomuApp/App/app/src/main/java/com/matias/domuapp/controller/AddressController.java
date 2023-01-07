package com.matias.domuapp.controller;

import com.matias.domuapp.models.Direccion;

public class AddressController {
    public int gettingNumberofHouse(String number){
        if(number =="NA"){
            return 0;
        }
        if(!number.isEmpty()){
            number=number.replaceAll("\\s","");
            int num = Integer.parseInt(number);
            return num;
        }
        else{
            return 0;
        }
    }
    public Direccion getDireccionString(String domicilio){
        Direccion direccion = new Direccion();
        String datos[] = domicilio.split(",");
        System.out.println("Datos "+datos.toString());
        System.out.println(datos[0]);
        System.out.println(datos[1]);
        System.out.println(datos[2]);
        System.out.println(datos[3]);
        System.out.println(datos[4]);
        System.out.println(datos[5]);
        direccion.setCountry("Mexico");
        direccion.setState(datos[0]);
        direccion.setCity(datos[1]);
        direccion.setColony(datos[2]);
        direccion.setStreet(datos[3]);
        direccion.setPostalCode(datos[4]);
        int num = gettingNumberofHouse(datos[5]);
        direccion.setNumber(num);
        System.out.println(direccion.toString());
        return direccion;
    }
}

package com.matias.domuapp.controller;

public class AddressController {
    public int gettingNumberofHouse(String number){
        if(number =="NA"){
            return 0;
        }
        if(!number.isEmpty()){
            int num = Integer.parseInt(number);
            return num;
        }
        else{
            return 0;
        }
    }
}

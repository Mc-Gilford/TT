package com.matias.domuapp.controller;

import com.google.firebase.database.DatabaseReference;
import com.matias.domuapp.models.dao.TokenDao;

public class TokenController {
    private DatabaseReference mDatabaseReference;
    private TokenDao tokenDao;
    public TokenController(String AuthId){
        generateToken(AuthId);
    }
    private void generateToken(String AuthId){
        tokenDao = new TokenDao(mDatabaseReference);
        System.out.println("Generando Token");
        tokenDao.create(AuthId);
    }

    public DatabaseReference getmDatabaseReference(String idUser) {
        return tokenDao.getToken(idUser);
    }
}

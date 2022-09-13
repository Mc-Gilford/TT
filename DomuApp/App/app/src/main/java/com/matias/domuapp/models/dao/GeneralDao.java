package com.matias.domuapp.models.dao;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GeneralDao {
    private static String uri = "https://domu-1-default-rtdb.firebaseio.com/";
    public Boolean conexion( DatabaseReference mDatabase)
    {
        mDatabase = FirebaseDatabase.getInstance(this.uri).getReference();
        System.out.println("Conexion a la DB "+mDatabase);
        return !mDatabase.toString().isEmpty();
    }

}

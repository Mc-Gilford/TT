package com.matias.domuapp.models.dao;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GeneralDao {
    private static String uri = "https://domu-1-default-rtdb.firebaseio.com/";
    private DatabaseReference mDatabase;
    public GeneralDao(DatabaseReference mDatabase)
    {
    this.mDatabase=mDatabase;
    }

    public Boolean conexion()
    {
        this.mDatabase = FirebaseDatabase.getInstance(this.uri).getReference();
        System.out.println("Conexion a la DB "+mDatabase);
        return !mDatabase.toString().isEmpty();
    }

    public DatabaseReference easyConexion(DatabaseReference mDatabase){

        System.out.println("Easy Conexion a DB"+mDatabase);
        this.mDatabase = FirebaseDatabase.getInstance(this.uri).getReference();
        return this.mDatabase;
    }

    public DatabaseReference getmDatabase() {
        return mDatabase;
    }
}

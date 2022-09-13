package com.matias.domuapp.models.dao;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class generalDao {

    public Boolean conexion(FirebaseAuth mAuth, DatabaseReference mDatabase, String uri)
    {

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance(uri).getReference();
        return false;
    }

}

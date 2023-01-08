package com.matias.domuapp.models.dao;

import com.google.firebase.database.DatabaseReference;

public class ClientDao {
    public DatabaseReference getmDatabaseCliente(DatabaseReference mDatabase) {
        GeneralDao generalDao = new GeneralDao(mDatabase);
        mDatabase=generalDao.easyConexion(mDatabase);
        mDatabase = mDatabase.child("Users").child("Clients");;
        return mDatabase;
    }


}

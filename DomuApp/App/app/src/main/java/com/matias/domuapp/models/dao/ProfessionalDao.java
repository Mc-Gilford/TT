package com.matias.domuapp.models.dao;

import com.google.firebase.database.DatabaseReference;

public class ProfessionalDao {
    public DatabaseReference getmDatabaseProfesionista(DatabaseReference mDatabase) {
        GeneralDao generalDao = new GeneralDao(mDatabase);
        mDatabase=generalDao.easyConexion(mDatabase);
        mDatabase = mDatabase.child("Users").child("Profesionista");
        return mDatabase;
    }
}

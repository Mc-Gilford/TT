package com.matias.domuapp.models.dao;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ActiveUsersDao {
    public DatabaseReference getmDatabaseActiveUser(DatabaseReference mDatabase) {
        GeneralDao generalDao = new GeneralDao(mDatabase);
        mDatabase = generalDao.easyConexion(mDatabase);
        mDatabase = mDatabase.child("Users").child("active_profesionista");
        //mDatabase = FirebaseDatabase.getInstance("https://domu-1-default-rtdb.firebaseio.com/").getReference().child("Users").child("active_profesionistas");
        return mDatabase;
    }
}

package com.matias.domuapp.models.dao;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.matias.domuapp.models.Persona;

public class ProfessionalDao {
    private DatabaseReference databaseReference;
    private Persona person;
    public DatabaseReference getmDatabaseProfesionista(DatabaseReference mDatabase) {
        GeneralDao generalDao = new GeneralDao(mDatabase);
        mDatabase=generalDao.easyConexion(mDatabase);
        mDatabase = mDatabase.child("Users").child("Profesionista");
        return mDatabase;
    }
    public DatabaseReference getmDatabaseProfesionistaIsWorking(DatabaseReference mDatabase,String idProfesionist) {
        GeneralDao generalDao = new GeneralDao(mDatabase);
        mDatabase=generalDao.easyConexion(mDatabase);
        mDatabase = mDatabase.child("profesionist_working").child(idProfesionist);
        return mDatabase;
    }
    public ProfessionalDao(){
        databaseReference = getmDatabaseProfesionista(databaseReference);
        person = new Persona();
    }
    public String getNameClient(String id){
        databaseReference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    person.setName(dataSnapshot.child("name").getValue().toString());
                    person.setLastname(dataSnapshot.child("lastname").getValue().toString());
                    person.setSecondname(dataSnapshot.child("secondname").getValue().toString());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
        return person.getFullName();
    }
}

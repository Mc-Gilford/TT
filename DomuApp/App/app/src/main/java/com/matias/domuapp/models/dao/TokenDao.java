package com.matias.domuapp.models.dao;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.matias.domuapp.models.Token;

public class TokenDao {
    private DatabaseReference mDatabase;
    public TokenDao(DatabaseReference mDatabase){
        this.mDatabase=getmDatabaseTokenDao(mDatabase);
    }
    public DatabaseReference getmDatabaseTokenDao(DatabaseReference mDatabase) {
        GeneralDao generalDao = new GeneralDao(mDatabase);
        mDatabase = generalDao.easyConexion(mDatabase);
        mDatabase = mDatabase.child("Tokens");
        return mDatabase;
    }
    public void create(final String idUser) {
        System.out.println(mDatabase.toString());
        if (idUser == null) return;
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                Token token = new Token(instanceIdResult.getToken());
                mDatabase.child(idUser).setValue(token);
            }
        });
    }

    public DatabaseReference getToken(String idUser) {
        return mDatabase.child(idUser);
    }
}

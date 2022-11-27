package com.matias.domuapp.controller;

import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.maps.model.Marker;
import com.matias.domuapp.activities.MainActivity;
import com.matias.domuapp.activities.profesionista.MapProfesionistaActivity;
import com.matias.domuapp.providers.AuthProvider;

public class ProfessionalController {
    public Boolean disconnectProfesional(FusedLocationProviderClient mFusedLocation, Button mButtonConnect, Boolean mIsConnect,
                                         Marker mMarker, LocationCallback mLocationCallback, AuthProvider mAuthProvider, Context context){
        if (mFusedLocation != null) {
            mButtonConnect.setText("Conectarse");
            mIsConnect = false;
            if (mMarker != null) {
                mMarker.remove();
            }
            mFusedLocation.removeLocationUpdates(mLocationCallback);
            if (mAuthProvider.existSession()) {
                //mGeofireProvider.removeLocation(mAuthProvider.getId());
            }

            return mIsConnect;
        }
        else {
            Toast.makeText(context, "No te puedes desconectar", Toast.LENGTH_SHORT).show();
            return mIsConnect;
        }
    }
    public Boolean logoutProfesional(FusedLocationProviderClient mFusedLocation, Button mButtonConnect, Boolean mIsConnect,
                                  Marker mMarker, LocationCallback mLocationCallback, AuthProvider mAuthProvider, Context context) {
        mIsConnect=disconnectProfesional(mFusedLocation,mButtonConnect,mIsConnect,mMarker,mLocationCallback,mAuthProvider, context);
        mAuthProvider.logout();
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
        return mIsConnect;
        //finish();
    }
}

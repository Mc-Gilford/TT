package com.matias.domuapp.controller;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.matias.domuapp.activities.MainActivity;
import com.matias.domuapp.activities.cliente.UpdateProfileActivity;
import com.matias.domuapp.activities.cliente.RequestProfesionistActivity;
import com.matias.domuapp.activities.profesionista.MapProfesionistBookingActivity;
import com.matias.domuapp.activities.profesionista.MapProfesionistaActivity;
import com.matias.domuapp.activities.profesionista.NotificationBookingActivity;
import com.matias.domuapp.models.Persona;
import com.matias.domuapp.models.Profesional;
import com.matias.domuapp.providers.AuthProvider;
import com.matias.domuapp.providers.ClientBookingProvider;
import com.matias.domuapp.providers.ClienteProvider;
import com.matias.domuapp.providers.GeofireProvider;
import com.matias.domuapp.providers.ProfesionistaProvider;

import android.os.Handler;


public class ProfessionalController {
    private GeofireProvider mGeofireProvider;
    Persona persona;
    public ProfessionalController(){
        mGeofireProvider = new GeofireProvider();
        persona = new Persona();
    }
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
                mGeofireProvider.removeLocation(mAuthProvider.getId());
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
    public void updateLocation(AuthProvider mAuthProvider, LatLng mCurrentLatLng) {
        if (mAuthProvider.existSession() && mCurrentLatLng != null) {
            ProfesionistaProvider profesionistaProvider = new ProfesionistaProvider();
            mGeofireProvider.saveLocation(mAuthProvider.getId(), mCurrentLatLng);
        }
    }

    public void goToRequestProfesionist(Context context, LatLng mOriginLatLng,String mExtraOrigin, String mExtraDestination, LatLng mDestinationLatLng) {
        Intent intent = new Intent(context, RequestProfesionistActivity.class);
        intent.putExtra("origin_lat", mOriginLatLng.latitude);
        intent.putExtra("origin_lng", mOriginLatLng.longitude);
        intent.putExtra("origin", mExtraOrigin);
        intent.putExtra("destination", mExtraDestination);
        intent.putExtra("destination_lat", mDestinationLatLng.latitude);
        intent.putExtra("destination_lng", mDestinationLatLng.longitude);
        context.startActivity(intent);
    }

    public void checkIfClientCancelBooking(final Context context, ValueEventListener mListener, String mExtraIdClient, ClientBookingProvider clientBookingProvider,
                                            final Handler mHandler, final Runnable runnable) {
        mListener = clientBookingProvider.getClientBooking(mExtraIdClient).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    Toast.makeText(context, "El cliente cancelo el servicio", Toast.LENGTH_LONG).show();
                    if (mHandler != null) mHandler.removeCallbacks(runnable);
                    Intent intent = new Intent(context, MapProfesionistaActivity.class);
                    context.startActivity(intent);
                    ((Activity)context).finish();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
    public void acceptBooking(Context context, Handler mHandler, Runnable runnable, AuthProvider mAuthProvider, ClientBookingProvider mClientBookingProvider,
                              String mExtraIdClient) {
        if (mHandler != null) mHandler.removeCallbacks(runnable);
        mAuthProvider = new AuthProvider();
        mGeofireProvider = new GeofireProvider();
        mGeofireProvider.removeLocation(mAuthProvider.getId());

        mClientBookingProvider = new ClientBookingProvider();
        mClientBookingProvider.updateStatus(mExtraIdClient, "accept");
        System.out.println("Profesional controller "+mExtraIdClient);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(2);

        Intent intent = new Intent(context, MapProfesionistBookingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setAction(Intent.ACTION_RUN);
        intent.putExtra("idClient", mExtraIdClient);
        context.startActivity(intent);
    }
    public void cancelBooking(Context context, Handler mHandler, Runnable runnable, ClientBookingProvider mClientBookingProvider, String mExtraIdClient) {
        if (mHandler != null) mHandler.removeCallbacks(runnable);
        mClientBookingProvider.updateStatus(mExtraIdClient, "cancel");

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(2);
        Intent intent = new Intent(context, MapProfesionistaActivity.class);
        context.startActivity(intent);
        ((Activity)context).finish();
    }
    public String getProfesional(String idCliente){
        ProfesionistaProvider profesionistaProvider = new ProfesionistaProvider();

        profesionistaProvider.getProfesionist(idCliente).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    persona.setName(dataSnapshot.child("name").getValue().toString());
                    persona.setLastname(dataSnapshot.child("lastname").getValue().toString());
                    persona.setSecondname(dataSnapshot.child("secondname").getValue().toString());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return persona.getFullName();
    }
}

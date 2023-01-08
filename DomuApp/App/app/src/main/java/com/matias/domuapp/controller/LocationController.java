package com.matias.domuapp.controller;

import android.content.Context;
import android.location.Location;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.matias.domuapp.R;
import com.matias.domuapp.providers.AuthProvider;
import com.matias.domuapp.providers.GeofireProvider;

public class LocationController {

    private Marker mMarker;
    private LatLng mCurrentLatLng;
    private GoogleMap mMap;

    public LocationCallback getLocationCallback(final Context context, final int opCode){
        System.out.println("LocationController:locationCallback");
        LocationCallback mLocationCallback =new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {

                for (Location location : locationResult.getLocations()) {
                    if (context.getApplicationContext() != null) {
                        mCurrentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                        System.out.println("Localizacion "+mCurrentLatLng);

                        if(mMarker != null){
                            mMarker.remove();
                        }
                        if (opCode==1) {
                            mMarker = mMap.addMarker(new MarkerOptions().position(
                                                    new LatLng(location.getLatitude(), location.getLongitude())
                                            )
                                            .title("tu posición")
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.veterinario_icon))
                            );
                        }
                        else{
                            mMarker = mMap.addMarker(new MarkerOptions().position(
                                                    new LatLng(location.getLatitude(), location.getLongitude())
                                            )
                                            .title("tu posición")
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_user))
                            );
                        }
                        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(
                                new CameraPosition.Builder()
                                        .target(new LatLng(location.getLatitude(), location.getLongitude()))
                                        .zoom(15f)
                                        .build()
                        ));
                    }
                }
            }
        };

        return mLocationCallback;
    }


}

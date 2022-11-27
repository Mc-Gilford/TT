package com.matias.domuapp.providers;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.matias.domuapp.models.dao.ClientDao;
import com.matias.domuapp.models.dao.GeneralDao;

public class GeofireProvider {
    private DatabaseReference mDatabase;
    private GeoFire mGeofire;
    public GeofireProvider () {
        mDatabase = FirebaseDatabase.getInstance("https://domu-1-default-rtdb.firebaseio.com/").getReference().child("Users").child("active_profesionistas");
        mGeofire = new GeoFire(mDatabase);
    }

    public void saveLocation(String idProfesionista, LatLng latLng) {
        mGeofire.setLocation(idProfesionista, new GeoLocation(latLng.latitude, latLng.longitude));
    }

    public void removeLocation(String idProfesionista) {
        mGeofire.removeLocation(idProfesionista);
    }

    public GeoQuery getActiveProfesionist(LatLng latLng) {
        GeoQuery geoQuery = mGeofire.queryAtLocation(new GeoLocation(latLng.latitude, latLng.longitude), 10);
        geoQuery.removeAllListeners();
        return geoQuery;
    }
}

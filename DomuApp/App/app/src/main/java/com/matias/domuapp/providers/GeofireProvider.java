package com.matias.domuapp.providers;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.matias.domuapp.models.dao.ActiveUsersDao;
import com.matias.domuapp.models.dao.ProfessionalDao;

public class GeofireProvider {
    private DatabaseReference mDatabase;//Active users
    private DatabaseReference mDatabaseProfesionistWorking;
    private GeoFire mGeofire;

    public DatabaseReference getmDatabase() {
        return mDatabase;
    }

    public GeofireProvider () {
        ActiveUsersDao professionalDao = new ActiveUsersDao();
        mDatabase=professionalDao.getmDatabaseActiveUser(mDatabase);
        mGeofire = new GeoFire(mDatabase);
    }

    public void saveLocation(String idProfesionista, LatLng latLng) {
        mGeofire.setLocation(idProfesionista, new GeoLocation(latLng.latitude, latLng.longitude));
        //return mGeofire;
    }

    public void removeLocation(String idProfesionista) {
        mGeofire.removeLocation(idProfesionista);
    }

    public GeoQuery getActiveProfesionist(LatLng latLng, double i) {
        GeoQuery geoQuery = mGeofire.queryAtLocation(new GeoLocation(latLng.latitude, latLng.longitude), 10);
        geoQuery.removeAllListeners();
        return geoQuery;
    }
    public DatabaseReference getProfesionistLocation(String idProfesionist) {
        return mDatabase.child(idProfesionist).child("l");
    }
    public DatabaseReference isProfesionistWorking(String idProfesionist) {
        ProfessionalDao professionalDao = new ProfessionalDao();
        mDatabaseProfesionistWorking = professionalDao.getmDatabaseProfesionistaIsWorking(mDatabaseProfesionistWorking,idProfesionist);
        return mDatabaseProfesionistWorking;
    }
}

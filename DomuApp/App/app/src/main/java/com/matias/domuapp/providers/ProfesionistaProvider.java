package com.matias.domuapp.providers;

import com.firebase.geofire.GeoFire;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.matias.domuapp.models.Cliente;
import com.matias.domuapp.models.Profesional;
import com.matias.domuapp.models.Servicio;
import com.matias.domuapp.models.dao.ProfessionalDao;
import com.matias.domuapp.models.dao.UserDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfesionistaProvider {
    private DatabaseReference mDatabase;
    private GeoFire mGeofire;

    public ProfesionistaProvider() {
        //mDatabase = FirebaseDatabase.getInstance("https://domu-1-default-rtdb.firebaseio.com/").getReference().child("Users").child("Profesionista");
        ProfessionalDao professionalDao = new ProfessionalDao();
        mDatabase=professionalDao.getmDatabaseProfesionista(mDatabase);
        //mGeofire = new GeoFire(mDatabase);
    }

    public Task<Void> create(Object object) {
        Profesional profesionista = (Profesional) object;
        Map<String, Object> map = new HashMap<>();
        map.put("id",profesionista.getId());
        map.put("email",profesionista.getEmail());
        map.put("password",profesionista.getPassword());
        map.put("person", profesionista.getPerson());
        map.put("account",profesionista.getAccount());
        map.put("idActive",profesionista.getIdActive());
        map.put("coordX",profesionista.getCoordX());
        map.put("coordY",profesionista.getCoordX());
        map.put("services",profesionista.getServices());
        map.put("idJob",profesionista.getIdJob());
        map.put("score",profesionista.getScore());
        map.put("qualification",profesionista.getQualification());
        map.put("typeUser",profesionista.getTypeUser());
        map.put("servicio",profesionista.getServicio());
        map.put("image",profesionista.getImage());
        return mDatabase.child(profesionista.getId()).setValue(map);
    }

    public DatabaseReference getmDatabase() {
        return mDatabase;
    }
    public DatabaseReference getProfesionist(String idProfesionist) {
        return mDatabase.child(idProfesionist);
    }

    public Task<Void> update(Profesional profesionist) {
        Map<String, Object> map = new HashMap<>();
        map.put("person", profesionist.getPerson());
        map.put("image", profesionist.getImage());
        map.put("servicio", profesionist.getServicio());
        return mDatabase.child(profesionist.getId()).updateChildren(map);
    }
}

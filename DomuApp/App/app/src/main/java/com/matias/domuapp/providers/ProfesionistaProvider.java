package com.matias.domuapp.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.matias.domuapp.models.Cliente;
import com.matias.domuapp.models.Profesional;
import com.matias.domuapp.models.Servicio;
import com.matias.domuapp.models.dao.ClientDao;
import com.matias.domuapp.models.dao.ProfessionalDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfesionistaProvider {
    DatabaseReference mDatabase;

    public ProfesionistaProvider() {
        //mDatabase = FirebaseDatabase.getInstance("https://domu-1-default-rtdb.firebaseio.com/").getReference().child("Users").child("Profesionista");
        ProfessionalDao clientDao = new ProfessionalDao();
        mDatabase=clientDao.getmDatabaseProfesionista(mDatabase);
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
        return mDatabase.child(profesionista.getId()).setValue(map);
    }


}

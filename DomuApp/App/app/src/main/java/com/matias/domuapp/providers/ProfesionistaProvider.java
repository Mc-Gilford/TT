package com.matias.domuapp.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.matias.domuapp.models.Cliente;
import com.matias.domuapp.models.Profesionista;

import java.util.HashMap;
import java.util.Map;

public class ProfesionistaProvider {
    DatabaseReference mDatabase;

    public ProfesionistaProvider() {
        mDatabase = FirebaseDatabase.getInstance("https://domu-1-default-rtdb.firebaseio.com/").getReference().child("Users").child("Profesionista");
    }

    public Task<Void> create(Profesionista profesionista) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", profesionista.getName());
        map.put("email", profesionista.getEmail());
        map.put("service", profesionista.getService());
        map.put("address", profesionista.getAddress());
        return mDatabase.child(profesionista.getId()).setValue(map);
    }


}

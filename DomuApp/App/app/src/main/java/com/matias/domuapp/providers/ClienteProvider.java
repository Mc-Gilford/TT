package com.matias.domuapp.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.matias.domuapp.models.Cliente;
import com.matias.domuapp.models.dao.ClientDao;
import com.matias.domuapp.models.dao.GeneralDao;

import java.util.HashMap;
import java.util.Map;

public class ClienteProvider {
    DatabaseReference mDatabase;

    public ClienteProvider() {
        //mDatabase = FirebaseDatabase.getInstance("https://domu-1-default-rtdb.firebaseio.com/").getReference().child("Users").child("Clients");
        ClientDao clientDao = new ClientDao();
        mDatabase=clientDao.getmDatabaseCliente(mDatabase);
    }



    public Task<Void> create(Object object) {
        Cliente cliente = (Cliente) object;
        Map<String, Object> map = new HashMap<>();
        map.put("id",cliente.getId());
        map.put("email",cliente.getEmail());
        map.put("password",cliente.getPassword());
        map.put("person", cliente.getPerson());
        map.put("account",cliente.getAccount());
        map.put("idActive",cliente.getIdActive());
        map.put("coordX",cliente.getCoordX());
        map.put("coordY",cliente.getCoordX());
        map.put("professionals",cliente.getProfessionals());
        map.put("typeUser",cliente.getTypeUser());
        return mDatabase.child(cliente.getId()).setValue(map);
    }


}

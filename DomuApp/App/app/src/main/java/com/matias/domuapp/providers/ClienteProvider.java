package com.matias.domuapp.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.matias.domuapp.models.Cliente;

import java.util.HashMap;
import java.util.Map;

public class ClienteProvider {
    DatabaseReference mDatabase;

    public ClienteProvider() {
        mDatabase = FirebaseDatabase.getInstance("https://domu-1-default-rtdb.firebaseio.com/").getReference().child("Users").child("Clients");
    }

    public Task<Void> create(Cliente client) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", client.getPerson().getName());
        map.put("email", client.getEmail());
        map.put("address", client.getPerson().getAddress());
        return null;
       // return mDatabase.child(client.getId()).setValue(map);
    }


}

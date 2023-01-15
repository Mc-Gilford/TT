package com.matias.domuapp.activities.cliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.matias.domuapp.R;
import com.matias.domuapp.adapters.ServiceAdapter;
import com.matias.domuapp.models.Persona;
import com.matias.domuapp.models.Profesional;
import com.matias.domuapp.models.Servicio;
import com.matias.domuapp.providers.ProfesionistaProvider;

import java.util.ArrayList;
import java.util.List;

public class ServiciosProfesionistaActivity extends AppCompatActivity {
    private String id;
    private ProfesionistaProvider profesionistaProvider;
    private ServiceAdapter serviceAdapter;
    private RecyclerView mReciclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicios_profesionista);
        Intent intentBefore= getIntent();
        Bundle bundle = intentBefore.getExtras();
        profesionistaProvider = new ProfesionistaProvider();
        if(bundle!=null)
        {
            id =(String) bundle.get("id");
        }
        mReciclerView = findViewById(R.id.recyclerViewHistoryBooking);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mReciclerView.setLayoutManager(linearLayoutManager);

        profesionistaProvider.getProfesionist(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    /*List<Object> objects = (List<Object>)dataSnapshot.child("servicios").getValue();
                    for (Object servic: objects){
                        Servicio ser = (Servicio) servic;
                        System.out.println("Barney "+ser.getName());
                    }*/
                    ArrayList<Servicio> servicios = new ArrayList<Servicio>();
                    Profesional profesional = new Profesional();
                    profesional.setServicio(dataSnapshot.child("servicio").getValue().toString());
                    Persona persona = new Persona();
                    persona.setName(dataSnapshot.child("person").child("name").getValue().toString());
                    persona.setLastname(dataSnapshot.child("person").child("lastname").getValue().toString());
                    persona.setSecondname(dataSnapshot.child("person").child("secondname").getValue().toString());
                    profesional.setPerson(persona);
                    profesional.setImage(dataSnapshot.child("image").getValue().toString());
                    for (DataSnapshot snapshot : dataSnapshot.child("servicios").getChildren()){
                        Servicio servicio = new Servicio();
                        String Nombre=snapshot.child("NameService").getValue().toString();
                        String Descripcion=snapshot.child("Description").getValue().toString();
                        String Price=snapshot.child("Price").getValue().toString();
                        servicio.setName(Nombre);
                        servicio.setDetails(Descripcion);
                        servicio.setCost(Double.valueOf(Price));
                        servicios.add(servicio);
                    }
                    profesional.setServices(servicios);
                    System.out.println("Bob toronja"+servicios.toString());
                    serviceAdapter = new ServiceAdapter(servicios,ServiciosProfesionistaActivity.this,profesional);
                    mReciclerView.setAdapter(serviceAdapter);

                    //System.out.println("Hola "+dataSnapshot.getChildren().toString());
                    //System.out.println("Servicios "+dataSnapshot.child("servicios").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
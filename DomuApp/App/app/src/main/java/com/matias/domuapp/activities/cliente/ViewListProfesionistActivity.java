package com.matias.domuapp.activities.cliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.location.LocationCallback;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;
import com.matias.domuapp.R;
import com.matias.domuapp.adapters.HistoryBookingClientAdapter;
import com.matias.domuapp.adapters.ProfesionistasActiveAdapter;
import com.matias.domuapp.includes.MyToolbar;
import com.matias.domuapp.models.HistoryBooking;
import com.matias.domuapp.models.Persona;
import com.matias.domuapp.models.Profesional;
import com.matias.domuapp.models.dao.ActiveUsersDao;
import com.matias.domuapp.providers.AuthProvider;
import com.matias.domuapp.providers.GeofireProvider;
import com.matias.domuapp.providers.ProfesionistaProvider;

import java.util.ArrayList;


public class ViewListProfesionistActivity extends AppCompatActivity {
    private RecyclerView mReciclerView;
    private ProfesionistasActiveAdapter mAdapter;
    private AuthProvider mAuthProvider;
    private GoogleMap mMap;
    private Marker mMarker;
    private LatLng mCurrentLatLng;
    private GeofireProvider mGeofireProvider;
    private AutocompleteSupportFragment mAutocomplete;
    private AutocompleteSupportFragment mAutocompleteDestination;
    private ArrayList<Profesional>arrayList = new ArrayList<Profesional>();
    private ProfesionistasActiveAdapter profesionistasActiveAdapter;
    private ProfesionistaProvider profesionistaProvider;
    private Button button;

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {

            for(Location location: locationResult.getLocations()) {
                if (getApplicationContext() != null) {
                    mCurrentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list_profesionist);
        MyToolbar.show(this, "Profesionistas Activos", true);
        arrayList = new ArrayList<Profesional>();
        mGeofireProvider = new GeofireProvider();
        mReciclerView = findViewById(R.id.recyclerViewHistoryBooking);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mReciclerView.setLayoutManager(linearLayoutManager);
        button = findViewById(R.id.btnSolicitar);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  intent = new Intent(ViewListProfesionistActivity.this, MapClienteActivity.class);
                startActivity(intent);
            }
        });
        profesionistaProvider = new ProfesionistaProvider();
        ActiveUsersDao activeUsersDao = new ActiveUsersDao();
        activeUsersDao.getDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                /*if (dataSnapshot.exists()){
                    Profesional profesional= new Profesional();
                    Persona persona = new Persona();
                    persona.setName(dataSnapshot.child("person").child("name").getValue().toString());
                    System.out.println("Mams "+persona.getName());
                }*/
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    String idActive=dsp.getKey(); //add result into array list
                    System.out.println("Kuriboh "+idActive);
                    //Profesional profesional= new Profesional();
                    //profesional.setId(dsp.getKey());
                    //arrayList.add(profesional);
                    profesionistaProvider = new ProfesionistaProvider();
                    profesionistaProvider.getProfesionist(idActive).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                Profesional profesional = new Profesional();
                                Persona person= new Persona();
                                person.setName(dataSnapshot.child("person").child("name").getValue().toString());
                                person.setLastname(dataSnapshot.child("person").child("lastname").getValue().toString());
                                person.setSecondname(dataSnapshot.child("person").child("secondname").getValue().toString());
                                profesional.setServicio(dataSnapshot.child("servicio").getValue().toString());
                                profesional.setScore(5F);
                                if(dataSnapshot.child("image").exists()){
                                profesional.setImage(dataSnapshot.child("image").getValue().toString());
                                }
                                profesional.setPerson(person);
                                profesional.setId(dataSnapshot.child("id").getValue().toString());
                                arrayList.add(profesional);
                                profesionistasActiveAdapter = new ProfesionistasActiveAdapter(arrayList,profesionistaProvider,ViewListProfesionistActivity.this);
                                mReciclerView.setAdapter(profesionistasActiveAdapter);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {}

                    });

                }

                System.out.println("Soy "+arrayList.toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
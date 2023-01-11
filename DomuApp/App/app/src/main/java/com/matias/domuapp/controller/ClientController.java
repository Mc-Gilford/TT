package com.matias.domuapp.controller;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.matias.domuapp.activities.MainActivity;
import com.matias.domuapp.activities.cliente.DetailRequestActivity;
import com.matias.domuapp.activities.cliente.MapClienteActivity;
import com.matias.domuapp.activities.cliente.UpdateProfileActivity;
import com.matias.domuapp.models.Cliente;
import com.matias.domuapp.models.Persona;
import com.matias.domuapp.models.dao.ClientDao;
import com.matias.domuapp.models.dao.UserDao;
import com.matias.domuapp.providers.AuthProvider;
import com.matias.domuapp.providers.ClienteProvider;
import com.squareup.picasso.Picasso;

public class ClientController {
    private DatabaseReference databaseReference;
    private ClientDao clientDao;
    private Persona persona;
    public void logout(AuthProvider mAuthProvider, Context context) {
        mAuthProvider.logout();
        Intent intent = new Intent(context,MainActivity.class);
        context.startActivity(intent);
    }
    public void requestProfesionist(Context context,LatLng mOriginLatLng, LatLng mDestinationLatLng, String mPrice, String mOrigin, String mDestination) {
        System.out.println("Request Profesionist: "+mOriginLatLng+" "+mDestinationLatLng);
        if (mOriginLatLng != null && mDestinationLatLng != null) {
            Intent intent = new Intent(context, DetailRequestActivity.class);
            intent.putExtra("origin_lat", mOriginLatLng.latitude);
            intent.putExtra("origin_lng", mOriginLatLng.longitude);
            intent.putExtra("destination_lat", mDestinationLatLng.latitude);
            intent.putExtra("destination_lng", mDestinationLatLng.longitude);
            intent.putExtra("price", mPrice);
            intent.putExtra("origin", mOrigin);
            intent.putExtra("destino", mDestination);
            context.startActivity(intent);
        }
        else {
            Toast.makeText(context, "Debe seleccionar el lugar donde requieres el servicio", Toast.LENGTH_SHORT).show();
        }
    }
    public String getClient(String idCliente){
        ClienteProvider clienteProvider = new ClienteProvider();
        persona = new Persona();
        clienteProvider.getClient(idCliente).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    persona.setName(dataSnapshot.child("name").getValue().toString());
                    persona.setLastname(dataSnapshot.child("lastname").getValue().toString());
                    persona.setSecondname(dataSnapshot.child("secondname").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return persona.getFullName();
    }

    public void getProfileInformation(ClienteProvider clienteProvider, AuthProvider authProvider, final TextView textView,
                                      final Context contex, final ImageView mImageViewProfile, final String moreText){
        clienteProvider.getClient(authProvider.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String name = dataSnapshot.child("person").child("name").getValue().toString();
                    String lastname = dataSnapshot.child("person").child("lastname").getValue().toString();
                    String secondname = dataSnapshot.child("person").child("secondname").getValue().toString();
                    textView.setText(moreText.toUpperCase()+" "+name.toUpperCase()+" "+lastname.toUpperCase()+" "+secondname.toUpperCase());
                    if (dataSnapshot.hasChild("image")) {
                        String image = dataSnapshot.child("image").getValue().toString();
                        Picasso.with(contex).load(image).into(mImageViewProfile);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

}

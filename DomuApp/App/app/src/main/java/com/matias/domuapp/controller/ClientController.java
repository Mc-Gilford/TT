package com.matias.domuapp.controller;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.matias.domuapp.activities.MainActivity;
import com.matias.domuapp.activities.cliente.DetailRequestActivity;
import com.matias.domuapp.activities.cliente.MapClienteActivity;
import com.matias.domuapp.models.dao.ClientDao;
import com.matias.domuapp.models.dao.UserDao;
import com.matias.domuapp.providers.AuthProvider;

public class ClientController {
    private DatabaseReference databaseReference;
    private ClientDao clientDao;
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
    public DatabaseReference getClient(String idCliente){
        return clientDao.getmDatabaseCliente(databaseReference.child(idCliente));
    }
}

package com.matias.domuapp.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.matias.domuapp.activities.cliente.MapClientBookingActivity;
import com.matias.domuapp.activities.cliente.MapClienteActivity;
import com.matias.domuapp.models.ClientBooking;
import com.matias.domuapp.models.FCMBody;
import com.matias.domuapp.models.FCMResponse;
import com.matias.domuapp.models.dao.TokenDao;
import com.matias.domuapp.providers.AuthProvider;
import com.matias.domuapp.providers.ClientBookingProvider;
import com.matias.domuapp.providers.NotificationProvider;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationController {
    private NotificationProvider mNotificationProvider;
    private ValueEventListener mListener;
    public void sendNotification(final String time, final String km, TokenController tokenController, final String mIdProfesionistFound, final Context context
    , final AuthProvider mAuthProvider, final String mExtraOrigin, final String mExtraDestination, final double mExtraOriginLat, final double mExtraOriginLng,
                                 final double mExtraDestinationLat, final double mExtraDestinationLng, final ClientBookingProvider mClientBookingProvider) {
        System.out.println("Sending Notification");
        System.out.println("Id User reciever "+mIdProfesionistFound);
        System.out.println(tokenController.getmDatabaseReference(mIdProfesionistFound).toString());
        tokenController.getmDatabaseReference(mIdProfesionistFound).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String token = dataSnapshot.child("token").getValue().toString();
                    System.out.println("Token notification"+token);
                    Map<String, String> map = new HashMap<>();
                    //map.put("title", "SOLICITUD DE SERVICIO A " + time + " DE TU POSICION");
                    map.put("title", "SOLICITUD DE SERVICIO");
                    map.put("body",
                            "Un cliente esta solicitando un servicio a una distancia de " + km + "\n" +
                                    "Costo: " + "$120.00" + "\n" +
                                    "Ubicación: " + mExtraDestination
                    );
                    map.put("idClient", mAuthProvider.getId());
                    map.put("origin", mExtraOrigin);
                    map.put("destination", mExtraDestination);
                    map.put("min", time);
                    map.put("distance", km);
                    FCMBody fcmBody = new FCMBody(token, "high","4500s", map);
                    mNotificationProvider = new NotificationProvider();
                    mNotificationProvider.sendNotification(fcmBody).enqueue(new Callback<FCMResponse>() {
                        @Override
                        public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                            if (response.body() != null) {
                                if (response.body().getSuccess() == 1) {
                                    ClientBooking clientBooking = new ClientBooking(
                                            mAuthProvider.getId(),
                                            mIdProfesionistFound,
                                            mExtraDestination,
                                            mExtraOrigin,
                                            time,
                                            km,
                                            "create",
                                            mExtraOriginLat,
                                            mExtraOriginLng,
                                            mExtraDestinationLat,
                                            mExtraDestinationLng
                                    );
                                    mClientBookingProvider.create(clientBooking).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            checkStatusClientBooking(context,mAuthProvider, mClientBookingProvider);
                                        }
                                    });
                                    //Toast.makeText(RequestProfesionistActivity.this, "La notificacion se ha enviado correctamente", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(context, "No se pudo enviar la notificacion", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(context, "No se pudo enviar la notificacion", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<FCMResponse> call, Throwable t) {
                            Log.d("Error", "Error " + t.getMessage());
                        }
                    });
                }
                else {
                    Toast.makeText(context, "No se pudo enviar la notificacion porque el profesionista no tiene un token de sesion", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
    private void checkStatusClientBooking(final Context context, AuthProvider mAuthProvider, ClientBookingProvider mClientBookingProvider) {
        Log.d("checkStatus","checkStatusClientBooking");
        mListener = mClientBookingProvider.getStatus(mAuthProvider.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    String status = dataSnapshot.getValue().toString();
                    if (status.equals("accept")) {
                        Intent intent = new Intent(context, MapClientBookingActivity.class);
                        context.startActivity(intent);
                        ((Activity)context).finish();
                    } else if (status.equals("cancel")) {
                        Toast.makeText(context, "El profesionista no acepto el servicio", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, MapClienteActivity.class);
                        context.startActivity(intent);
                        ((Activity)context).finish();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
    public void sendNotificationCancel(final Context context, String mIdProfesionistFound, TokenController tokenController) {
        tokenController.getmDatabaseReference(mIdProfesionistFound).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String token = dataSnapshot.child("token").getValue().toString();
                    Map<String, String> map = new HashMap<>();
                    map.put("title", "SERVICIO CANCELADO");
                    map.put("body",
                            "El cliente cancelo la solicitud"
                    );
                    FCMBody fcmBody = new FCMBody(token, "high", "4500s", map);
                    mNotificationProvider.sendNotification(fcmBody).enqueue(new Callback<FCMResponse>() {
                        @Override
                        public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                            if (response.body() != null) {
                                if (response.body().getSuccess() == 1) {
                                    Toast.makeText(context, "La solicitud se cancelo correctamente", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(context, MapClienteActivity.class);
                                    context.startActivity(intent);
                                    ((Activity)context).finish();
                                    //Toast.makeText(RequestDriverActivity.this, "La notificacion se ha enviado correctamente", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(context, "No se pudo enviar la notificacion", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(context, "No se pudo enviar la notificacion", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<FCMResponse> call, Throwable t) {
                            Log.d("Error", "Error " + t.getMessage());
                        }
                    });
                }
                else {
                    Toast.makeText(context, "No se pudo enviar la notificacion porque el profesionista no tiene un token de sesion", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

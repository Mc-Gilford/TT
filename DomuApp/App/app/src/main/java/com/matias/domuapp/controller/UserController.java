package com.matias.domuapp.controller;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.matias.domuapp.activities.RegisterActivity;
import com.matias.domuapp.activities.Services;
import com.matias.domuapp.activities.cliente.MapClienteActivity;
import com.matias.domuapp.activities.profesionista.MapProfesionistaActivity;
import com.matias.domuapp.models.Cliente;
import com.matias.domuapp.models.Profesional;
import com.matias.domuapp.models.Usuario;
import com.matias.domuapp.models.dao.GeneralDao;
import com.matias.domuapp.models.dao.UserDao;
import com.matias.domuapp.providers.AuthProvider;
import com.matias.domuapp.providers.ClienteProvider;
import com.matias.domuapp.providers.ProfesionistaProvider;
import com.squareup.picasso.Picasso;

import java.util.logging.Level;
import java.util.logging.Logger;

public class UserController {
    private final static java.util.logging.Logger LOGGER = Logger.getLogger("dao.usercontroller");
    private final static int LOCATION_REQUEST_CODE = 1;
    private final static int SETTINGS_REQUEST_CODE = 2;

    public Boolean validateEmailandPassword(Usuario user, Context context)
    {
        if (!user.getEmail().isEmpty() && !user.getPassword().isEmpty()) {
            Toast.makeText(context, "La contraseña y el email son obligatorios", Toast.LENGTH_SHORT).show();
        }
        return !user.getEmail().isEmpty() && !user.getPassword().isEmpty()&&validateFields(user.getPassword(),user.getEmail(),context);
    }
    public Boolean validateInformPerson(Usuario user, Context context){
        Boolean userValidationData = !user.getPerson().getName().isEmpty() && !user.getPerson().getLastname().isEmpty() && !user.getPerson().getBirthDate().toString().isEmpty();
        //System.out.println("Validando  logica"+user.getPerson().getName().isEmpty() +" "+ user.getPerson().getLastname().isEmpty() +" "+ user.getPerson().getBirthDate().toString().isEmpty());
        System.out.println("User datos "+user);
        if(!userValidationData){
            Toast.makeText(context, "Por favor verifique que sus datos esten llenos", Toast.LENGTH_SHORT).show();
        }
        return userValidationData && validateEmailandPassword(user,context);
    }

    public Boolean validateUser(Usuario user, Context context){
        Toast.makeText(context, "Validando usuario", Toast.LENGTH_SHORT).show();
        System.out.println("Validando "+ validateEmailandPassword(user,context) +" "+ validateInformPerson(user,context));
        return validateEmailandPassword(user,context) && validateInformPerson(user,context);
    }

    public Boolean validateFields(String password, String email,Context context)
    {
        if(password.length()>6 && email.contains("@")) {
            return true;
        }
        else {
            Toast.makeText(context, "La contraseña debe tener mas de 6 caracteres", Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    public Boolean validarTipoDeUsuario(Usuario user, Context context)
    {
        Boolean status =false;
        System.out.println("Validador de tipo de Usuario");
        if(user.getTypeUser().equals("Cliente")){
            Intent intent = new Intent(context, MapClienteActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
            status=true;
            return status;
        }
        else {
            Intent intent = new Intent(context, MapProfesionistaActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
            return status;
        }
    }

    public Boolean createUser(Usuario user, Context context, String servicio){
        UserDao userDao = new UserDao();
        System.out.println("Creando Usuario por tipo");
        if(user.getTypeUser()=="Cliente"){
            ClienteProvider mClienteProvider = new ClienteProvider();
            Usuario obj =  (Cliente) user;
            Cliente cliente = (Cliente) obj;
            userDao.createUser(cliente,mClienteProvider,context);
            return true;
        }else if(user.getTypeUser()=="Profesional"){
            ProfesionistaProvider profesionistaProvider = new ProfesionistaProvider();
            Profesional profesional = (Profesional) user;
            profesional.setServicio(servicio);
            userDao.createUser(profesional,profesionistaProvider,context);
            return true;
        }
        return false;
    }

    public Boolean createUser(Usuario user, Context context){
        UserDao userDao = new UserDao();
        System.out.println("Creando Usuario por tipo");
        if(user.getTypeUser()=="Cliente"){
            ClienteProvider mClienteProvider = new ClienteProvider();
            Usuario obj =  (Cliente) user;
            Cliente cliente = (Cliente) obj;
            userDao.createUser(cliente,mClienteProvider,context);
            return true;
        }else if(user.getTypeUser()=="Profesional"){
            ProfesionistaProvider profesionistaProvider = new ProfesionistaProvider();
            Profesional profesional = (Profesional) user;
            userDao.createUser(profesional,profesionistaProvider,context);
            return true;
        }
        return false;
    }

    public Usuario typeUser(String type){
        if(type == "Cliente"){
            return new Cliente();
        }else if (type=="Profesional"){
            return new Profesional();
        }
        return null;
    }

    public Usuario getUserInterface(Usuario user, FirebaseAuth mAuth, Context context){
        DatabaseReference mDatabase;
        GeneralDao generalDao = new GeneralDao();
        UserDao userDao = new UserDao();
        generalDao.conexion();
        mDatabase=generalDao.getmDatabase().child("Users");
        String id= mAuth.getCurrentUser().getUid();
        System.out.println("Current user: "+id);
        System.out.println("Obteniendo User Interface "+generalDao.toString());
        if(user!=null) {
            userDao.getUsuarioForTravel(user, mDatabase, id, "Profesionista", mAuth, context);
        }
        System.out.println("Salimos "+user);
        return user;
    }

    /*This is the fucntion that function*/
    public void movingToUserInterface(Usuario user, Context context){
        LOGGER.log(Level.INFO,"Typer of screen "+user.getTypeUser());
        if(user.getTypeUser().equals("Profesional")){
            Intent intent = new Intent(context, Services.class);
            LOGGER.log(Level.INFO, "Map profesional");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
        }else if(user.getTypeUser().equals("Cliente")){
            Intent intent = new Intent(context, MapClienteActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            LOGGER.log(Level.INFO, "Map cliente");
            context.startActivity(intent);
        }
    }

    public void checkLocationPermissions(final Context context){
        System.out.println("Verificando permiso GPS");
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(context)
                        .setTitle("Proporciona los permisos para continuar")
                        .setMessage("Esta aplicación requiere de los permisos de ubicación para poder utilizarse")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions((Activity) context, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
                            }
                        })
                        .create()
                        .show();
            }
            else {
                ActivityCompat.requestPermissions((Activity) context, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            }
        }
    }

    public void getUser(String Identifier, String type, final Context context, final ImageView mImageViewBooking, final TextView mTextViewUserBooking,
                          final TextView mTextViewEmailUserBooking, final TextView mTextViewDestinationUserBooking) {
        UserDao userDao = new UserDao();
        System.out.println("MapBookingActivity");
        if(type.compareTo("profesionista")==0){
            System.out.println("Cliente consulta");
            ClienteProvider clienteProvider = new ClienteProvider();
            mTextViewDestinationUserBooking.setText("Servicio: " + "Cliente");
            userDao.getUser(Identifier, clienteProvider.getmDatabase()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        System.out.println("Get profesionist");
                        getUserInformation(dataSnapshot,context,mImageViewBooking,mTextViewUserBooking,mTextViewEmailUserBooking);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });
        }else if(type.compareTo("cliente")==0){
            System.out.println("Profesionista consulta");
            ProfesionistaProvider profesionistaProvider = new ProfesionistaProvider();
            mTextViewDestinationUserBooking.setText("Servicio: " + "Veterinario");
            userDao.getUser(Identifier,profesionistaProvider.getmDatabase());
            userDao.getUser(Identifier, profesionistaProvider.getmDatabase()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        System.out.println("Get client");
                        getUserInformation(dataSnapshot,context,mImageViewBooking,mTextViewUserBooking,mTextViewEmailUserBooking);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });
        }else{
            Toast.makeText(context, "Llenar el tipo de usuario", Toast.LENGTH_SHORT).show();
            //return null;
        }
    }
    public void getUserInformation(DataSnapshot dataSnapshot, Context context, ImageView mImageViewBooking, TextView mTextViewUserBooking, TextView mTextViewEmailUserBooking){
        String name = dataSnapshot.child("person").child("name").getValue().toString();
        String lastname = dataSnapshot.child("person").child("lastname").getValue().toString();
        String secondname = dataSnapshot.child("person").child("secondname").getValue().toString();
        String email = dataSnapshot.child("email").getValue().toString();
        String image = "";
        if (dataSnapshot.hasChild("image")) {
            image = dataSnapshot.child("image").getValue().toString();
            Picasso.with(context).load(image).into(mImageViewBooking);
        }
        mTextViewUserBooking.setText(name+" "+lastname+" "+secondname);
        mTextViewEmailUserBooking.setText(email);
        System.out.println("Get information "+name+" "+email);
    }


}

package com.matias.domuapp.models.dao;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

//import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.matias.domuapp.activities.cliente.MapClienteActivity;
import com.matias.domuapp.activities.profesionista.MapProfesionistaActivity;
import com.matias.domuapp.controller.UserController;
import com.matias.domuapp.models.Cliente;
import com.matias.domuapp.models.Profesional;
import com.matias.domuapp.models.Usuario;
import com.matias.domuapp.providers.ClienteProvider;
import com.matias.domuapp.providers.ProfesionistaProvider;

public class UserDao {
    DatabaseReference mDatabase;
    Boolean status;
    public Boolean signIn(FirebaseAuth mAuth,final Usuario user,final AlertDialog mDialog, final Context context)
    {
        status=false;
        System.out.println(user.toString());
        System.out.println("Inicio de sesion");
        mAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    UserController userController = new UserController();
                    Usuario newUser =getUsuario(user);
                    System.out.println(user.toString());
                    System.out.println(newUser.toString());
                    // Debemos de obtener el usuario para validar que tipo es y pasarnos a la siguiente transicion
                    userController.validarTipoDeUsuario(user,context);
                    status=true;
                }
                else {
                    System.out.println("Error al validar usuario");
                    Toast.makeText(context, "La contraseña o el password son incorrectos", Toast.LENGTH_SHORT).show();
                }
                mDialog.dismiss();
            }
        });
    return status;
    }


    public Usuario getUsuario(Usuario user) {
        GeneralDao generalDao = new GeneralDao(mDatabase);
        System.out.println(mDatabase);
        generalDao.getmDatabase().child("Users");
        generalDao.getmDatabase().child("Users").toString();

        //ObjectMapper objectMapper = new ObjectMapper();
        //generalDao.getmDatabase().child("Users").equalTo(user.getEmail());*/
/*
        generalDao.getmDatabase().child("Users").equalTo(25).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                System.out.println(dataSnapshot.getValue());
            }

            // ...
        });*/
        //objectMapper.readValue();
        //mDatabase = FirebaseDatabase.getInstance("https://domu-1-default-rtdb.firebaseio.com/").getReference().child("Users").child("Clients");
    return user;
    }

    public void createUser(Object object, ClienteProvider mClienteProvider, final Context context)
    {
        mClienteProvider.create(object).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(context,"Registro Exitoso",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, MapClienteActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                }
                else{
                   Toast.makeText(context,"Error: no se pudo crear el cliente",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void createUser(Object object, ProfesionistaProvider mProfesionistaProvider, final Context context)
    {
        mProfesionistaProvider.create(object).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    System.out.println("Registro exitoso");
                    Toast.makeText(context,"Registro Exitoso",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, MapProfesionistaActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                }
                else{
                    Toast.makeText(context,"Error: no se pudo crear el cliente",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}

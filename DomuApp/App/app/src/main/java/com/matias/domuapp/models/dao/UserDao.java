package com.matias.domuapp.models.dao;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.matias.domuapp.activities.cliente.MapClienteActivity;
import com.matias.domuapp.activities.profesionista.MapProfesionistaActivity;
import com.matias.domuapp.controller.UserController;
import com.matias.domuapp.models.Usuario;

public class UserDao {
    //DatabaseReference mDatabase;
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

    public Boolean getUsuario() {
        //mDatabase = FirebaseDatabase.getInstance("https://domu-1-default-rtdb.firebaseio.com/").getReference().child("Users").child("Clients");
    return false;
    }
}

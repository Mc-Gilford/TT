package com.matias.domuapp.controller;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.matias.domuapp.activities.RegisterActivity;
import com.matias.domuapp.models.Direccion;
import com.matias.domuapp.models.Usuario;
import com.matias.domuapp.providers.AuthProvider;
import com.matias.domuapp.providers.ClienteProvider;

public class RegisterController {
    public void goToRegister(Context context) {

            Intent intent = new Intent(context, RegisterActivity.class);
            context.startActivity(intent);

    }

    public Boolean clickRegister(Usuario user,AuthProvider mAuthProvider, Context context){
        UserController userController = new UserController();
        if(userController.validateUser(user,context)){
            Toast.makeText(context, "El usario fue validado", Toast.LENGTH_SHORT).show();
            System.out.println("El usuario fue validado "+user.toString());
            registerUser(user,mAuthProvider,context,userController);
            return true;
        }
        else{
            return false;
        }
    }
    public Boolean clickRegister(Usuario user,String servicio,AuthProvider mAuthProvider, Context context){
        UserController userController = new UserController();
        if(userController.validateUser(user,context)){
            Toast.makeText(context, "El usario fue validado", Toast.LENGTH_SHORT).show();
            System.out.println("El usuario fue validado "+user.toString());
            registerUser(user,mAuthProvider,context,userController,servicio);
            return true;
        }
        else{
            return false;
        }
    }
    public void registerUser(final Usuario user, final AuthProvider mAuthProvider, final Context context, final UserController userController){
        mAuthProvider.register(user.getEmail(),user.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    System.out.println("Creando identificador");
                    String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    user.setId(id);
                    System.out.println("Usuario con id "+user.toString());
                    userController.createUser(user,context);
                }
                else{
                    Toast.makeText(context, "Hubo un error al registrar usuario", Toast.LENGTH_SHORT).show();
                    System.out.println("Fallo en el registro");
                }
            }
        });
    }
    public void registerUser(final Usuario user, final AuthProvider mAuthProvider, final Context context, final UserController userController, final String servicio){
        mAuthProvider.register(user.getEmail(),user.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    System.out.println("Creando identificador");
                    String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    user.setId(id);
                    System.out.println("Usuario con id "+user.toString());
                    userController.createUser(user,context,servicio);
                }
                else{
                    Toast.makeText(context, "Hubo un error al registrar usuario", Toast.LENGTH_SHORT).show();
                    System.out.println("Fallo en el registro");
                }
            }
        });
    }

}

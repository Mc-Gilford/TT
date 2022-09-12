package com.matias.domuapp.models.dao;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.matias.domuapp.activities.LoginActivity;
import com.matias.domuapp.activities.cliente.MapClienteActivity;
import com.matias.domuapp.activities.profesional.MapProfesionistaActivity;
import com.matias.domuapp.models.Usuario;

public class UserDao {
    Boolean status;
    public Boolean signIn(FirebaseAuth mAuth, Usuario user, AlertDialog mDialog, Context context)
    {
        status=false;
        mAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    if(user.getTypeUser().equals("cliente")){
                        Intent intent = new Intent(context, MapClienteActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(intent);

                    }
                    else {
                        Intent intent = new Intent(context, MapProfesionistaActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(intent);
                    }
                    status=true;
                }
                else {
                    Toast.makeText(context, "La contraseña o el password son incorrectos", Toast.LENGTH_SHORT).show();
                }
                mDialog.dismiss();
            }
        });
    return status;
    }
}

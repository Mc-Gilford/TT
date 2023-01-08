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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.matias.domuapp.activities.cliente.MapClienteActivity;
import com.matias.domuapp.activities.profesionista.MapProfesionistaActivity;
import com.matias.domuapp.controller.UserController;
import com.matias.domuapp.models.Usuario;
import com.matias.domuapp.providers.ClienteProvider;
import com.matias.domuapp.providers.ProfesionistaProvider;

import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDao {
    private DatabaseReference mDatabase;
    private Boolean status;
    private final static Logger LOGGER = Logger.getLogger("dao.userdao");

    public Boolean signIn(final FirebaseAuth mAuth, final Usuario user, final AlertDialog mDialog, final Context context)
    {
        status=false;
        System.out.println(user.toString());
        System.out.println("Inicio de sesion");
        mAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    UserController userController = new UserController();
                    Usuario newUser = userController.getUserInterface(user,mAuth,context);
                    System.out.println("Usuario final :"+user.toString());
                    System.out.println("Nuevo Usuario :"+newUser.toString());
                    // Debemos de obtener el usuario para validar que tipo es y pasarnos a la siguiente transicion
                    //userController.validarTipoDeUsuario(user,context);
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


    public Usuario getUsuarioForTravel(final Usuario user, final DatabaseReference mDatabase, final String id, final String type, final FirebaseAuth mAuth, final Context context) {
        LOGGER.log(Level.INFO,"Obteniendo datos usuario por tipo:");
        mDatabase.child(type).child(id).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario userObtained = dataSnapshot.getValue(Usuario.class);
                UserController userController = new UserController();
                if (userObtained!=null) {
                    LOGGER.log(Level.INFO,"Travel to correct screen:" +userObtained);
                    userController.movingToUserInterface(userObtained,context);

                }else{
                    getUsuarioForTravel(user,mDatabase,id,"Clients",mAuth,context);
                    LOGGER.log(Level.INFO,"Go to validate if is Client");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }

        });
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

    public DatabaseReference getUser(String IdentifierUser, DatabaseReference databaseReference) {
        return databaseReference.child(IdentifierUser);
    }
}

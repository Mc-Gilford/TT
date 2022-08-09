package com.matias.domuapp.activities.cliente;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.matias.domuapp.R;
import com.matias.domuapp.activities.profesionista.MapProfesionistaActivity;
import com.matias.domuapp.activities.profesionista.RegisterProfesionistaActivity;
import com.matias.domuapp.includes.MyToolbar;
import com.matias.domuapp.models.Cliente;
import com.matias.domuapp.providers.AuthProvider;
import com.matias.domuapp.providers.ClienteProvider;

import dmax.dialog.SpotsDialog;

public class RegisterActivity extends AppCompatActivity {

    AuthProvider mAuthProvider;
    ClienteProvider mClienteProvider;
    AlertDialog mDialog;

    Button mButtonRegister;
    TextInputEditText mTextInputEmail;
    TextInputEditText mTextInputName;
    TextInputEditText mTextInputPassword;
    TextInputEditText mTextInputAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mDialog = new SpotsDialog.Builder().setContext(RegisterActivity.this).setMessage("Espere un momento").build();


        MyToolbar.show(this, "Registro de usuario", true);
        mAuthProvider = new AuthProvider();
        mClienteProvider = new ClienteProvider();


        mButtonRegister = findViewById(R.id.btnRegister);
        mTextInputEmail = findViewById(R.id.textInputEmail);
        mTextInputName = findViewById(R.id.textInputName);
        mTextInputPassword = findViewById(R.id.textInputPassword);
        mTextInputAddress = findViewById(R.id.textInputAddress);

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickRegister();
            }
        });

    }

    void clickRegister() {
        final String name = mTextInputName.getText().toString();
        final String email = mTextInputEmail.getText().toString();
        final String password = mTextInputPassword.getText().toString();
        final String address = mTextInputAddress.getText().toString();

        if(!name.isEmpty() && !email.isEmpty() && !password.isEmpty() && !address.isEmpty()){
            if (password.length() >= 6){
                mDialog.show();
                register(name, email, password, address);

            }
            else{
                Toast.makeText(this, "La contraseña debe de tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "Ingrese todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    void register(final String name, final String email, String password, final String domicilio){
        mAuthProvider.register(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mDialog.hide();
                if (task.isSuccessful()){
                    String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Cliente cliente = new Cliente(id, name, email,domicilio);
                    create(cliente);
                }
                else{
                    Toast.makeText(RegisterActivity.this, "Hubo un error al registrar usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void create(Cliente cliente){
        mClienteProvider.create(cliente).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    //Toast.makeText(RegisterActivity.this,"Registro Exitoso",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, MapClienteActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(RegisterActivity.this,"Error: no se pudo crear el cliente",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /*void saveUser(String id, String name, String email) {
        String selectedUser = mPref.getString("user", "");
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        if (selectedUser.equals("profesionista")){
            mDatabase.child("Users").child("Profesionistas").child(id).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this,"Registro Exitoso",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(RegisterActivity.this,"Fallo el registro",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else if (selectedUser.equals("cliente")){
            mDatabase.child("Users").child("Clientes").child(id).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this,"Registro Exitoso",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(RegisterActivity.this,"Fallo el registro",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }
     */
}
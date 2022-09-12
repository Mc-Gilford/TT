package com.matias.domuapp.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.matias.domuapp.R;
import com.matias.domuapp.activities.cliente.MapClienteActivity;
import com.matias.domuapp.activities.profesional.MapProfesionistaActivity;
import com.matias.domuapp.controller.loginController;
import com.matias.domuapp.models.Usuario;
import com.matias.domuapp.models.dao.generalDao;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {
    Button mButtonLogin;
    Button mButtonRegister;
    TextInputEditText mTextInputEmail;
    TextInputEditText mTextInputPassword;
    Usuario user;
    SharedPreferences mPref;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        String email = mTextInputEmail.getText().toString();
        String password = mTextInputPassword.getText().toString();
        mButtonLogin = findViewById(R.id.btnLogin);
        mButtonRegister = findViewById(R.id.btnRegister);
        loginController  login= new loginController();
        user = new Usuario(email,password);
        generalDao generalDao = new generalDao();
        generalDao.conexion(mAuth,mDatabase,"https://domu-1-default-rtdb.firebaseio.com/");
        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login.login(user,mDialog,mAuth,MainActivity.this);
            }
        });
        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
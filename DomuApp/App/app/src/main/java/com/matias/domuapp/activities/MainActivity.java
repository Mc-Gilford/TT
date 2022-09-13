package com.matias.domuapp.activities;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.matias.domuapp.R;
import com.matias.domuapp.activities.profesionista.MapProfesionistaActivity;
import com.matias.domuapp.controller.LoginController;
import com.matias.domuapp.models.Usuario;
import com.matias.domuapp.models.dao.generalDao;

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
    LoginController login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButtonLogin = findViewById(R.id.btnLogin);
        mButtonRegister = findViewById(R.id.btnRegistrar);
        generalDao generalDao = new generalDao();
        generalDao.conexion(mAuth,mDatabase,"https://domu-1-default-rtdb.firebaseio.com/");
        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = mTextInputEmail.getText().toString();
                String password = mTextInputPassword.getText().toString();
                login= new LoginController();
                user = new Usuario(email,password);
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
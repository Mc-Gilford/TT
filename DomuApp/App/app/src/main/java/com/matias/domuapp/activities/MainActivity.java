package com.matias.domuapp.activities;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.matias.domuapp.R;
import com.matias.domuapp.controller.LoginController;
import com.matias.domuapp.models.Usuario;
import com.matias.domuapp.models.dao.GeneralDao;

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
        mTextInputEmail = (TextInputEditText) findViewById(R.id.textInputEmail);
        mTextInputPassword = (TextInputEditText) findViewById(R.id.textInputPassword);
        GeneralDao generalDao = new GeneralDao();
        if(generalDao.conexion(mDatabase)) {
            mButtonLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String email = mTextInputEmail.getText().toString();
                    String password = mTextInputPassword.getText().toString();
                    login = new LoginController();
                    user = new Usuario(email, password);
                    mAuth = FirebaseAuth.getInstance();
                    login.login(user, mDialog, mAuth, MainActivity.this);
                }
            });
            mButtonRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
        else{
            Toast.makeText(MainActivity.this, "Fallo en la conexion", Toast.LENGTH_SHORT).show();
        }
    }
}
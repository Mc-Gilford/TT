package com.matias.domuapp.activities;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.matias.domuapp.R;
import com.matias.domuapp.activities.cliente.MapClienteActivity;
import com.matias.domuapp.activities.cliente.RegisterActivity;
import com.matias.domuapp.activities.profesionista.MapProfesionistaActivity;
import com.matias.domuapp.includes.MyToolbar;

import dmax.dialog.SpotsDialog;


public class LoginActivity extends AppCompatActivity {

    TextInputEditText mTextInputEmail;
    TextInputEditText mTextInputPassword;
    Button mButtonLogin;

    SharedPreferences mPref;

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    AlertDialog mDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        MyToolbar.show(this, "login de usuario", true);

        mTextInputEmail    = findViewById(R.id.textInputEmail);
        mTextInputPassword = findViewById(R.id.textInputPassword);
        mButtonLogin       = findViewById(R.id.btnLogin);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://domu-1-default-rtdb.firebaseio.com/").getReference();

        mDialog = new SpotsDialog.Builder().setContext(LoginActivity.this).setMessage("Espere un momento").build();

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        mPref = getApplicationContext().getSharedPreferences("typeUser", MODE_PRIVATE);
    }

    private void login() {
        String email = mTextInputEmail.getText().toString();
        String password = mTextInputPassword.getText().toString();

        if (!email.isEmpty() && !password.isEmpty()) {
            if (password.length() >= 6) {
                mDialog.show();
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String typeUser = mPref.getString("user", "");
                            if(typeUser.equals("cliente")){
                                Intent intent = new Intent(LoginActivity.this, MapClienteActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

                            }
                            else if(typeUser.equals("profesionista")){
                                Intent intent = new Intent(LoginActivity.this, MapProfesionistaActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }

                        }
                        else {
                            Toast.makeText(LoginActivity.this, "La contraseña o el password son incorrectos", Toast.LENGTH_SHORT).show();
                        }
                        mDialog.dismiss();
                    }
                });
            }
            else {
                Toast.makeText(this, "La contraseña debe tener mas de 6 caracteres", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "La contraseña y el email son obligatorios", Toast.LENGTH_SHORT).show();
        }
    }
}

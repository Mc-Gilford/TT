package com.matias.domuapp.activities.profesional;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.matias.domuapp.R;
import com.matias.domuapp.includes.MyToolbar;
import com.matias.domuapp.models.Profesional;
import com.matias.domuapp.providers.AuthProvider;
import com.matias.domuapp.providers.ProfesionistaProvider;

import dmax.dialog.SpotsDialog;

public class RegisterProfesionistaActivity extends AppCompatActivity {


    AuthProvider mAuthProvider;
    ProfesionistaProvider mProfesionistaProvider;
    AlertDialog mDialog;

    Button mButtonRegister;
    TextInputEditText mTextInputEmail;
    TextInputEditText mTextInputName;
    TextInputEditText mTextInputPassword;
    TextInputEditText mTextInputService;
    TextInputEditText mTextInputAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_profesionista);

        mDialog = new SpotsDialog.Builder().setContext(RegisterProfesionistaActivity.this).setMessage("Espere un momento").build();


        MyToolbar.show(this, "Registro de usuario", true);
        mAuthProvider = new AuthProvider();
        mProfesionistaProvider = new ProfesionistaProvider();


        mButtonRegister = findViewById(R.id.btnRegister);
        mTextInputEmail = findViewById(R.id.textInputEmail);
        mTextInputName = findViewById(R.id.textInputName);
        mTextInputPassword = findViewById(R.id.textInputPassword);
        mTextInputService = findViewById(R.id.textInputService);
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
        final String service = mTextInputService.getText().toString();
        final String address = mTextInputAddress.getText().toString();

        if(!name.isEmpty() && !email.isEmpty() && !password.isEmpty() && !service.isEmpty() && !address.isEmpty()){
            if (password.length() >= 6){
                mDialog.show();
                register(name, email, password, service, address);

            }
            else{
                Toast.makeText(this, "La contraseña debe de tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "Ingrese todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    void register(final String name, final String email, String password, final String service, final String address){
        mAuthProvider.register(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mDialog.hide();
                if (task.isSuccessful()){
                    String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Profesional profesionista = new Profesional();
                    //create(profesionista);
                }
                else{
                    Toast.makeText(RegisterProfesionistaActivity.this, "Hubo un error al registrar usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

   /* void create(Profesional profesionista){
        mProfesionistaProvider.create(profesionista).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    //Toast.makeText(RegisterProfesionistaActivity.this,"Registro Exitoso",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterProfesionistaActivity.this, MapProfesionistaActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(RegisterProfesionistaActivity.this,"Error: no se pudo crear el cliente",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }*/
}
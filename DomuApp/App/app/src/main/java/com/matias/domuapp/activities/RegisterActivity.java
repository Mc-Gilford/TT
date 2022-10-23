package com.matias.domuapp.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.matias.domuapp.R;
import com.matias.domuapp.controller.AddressController;
import com.matias.domuapp.controller.RegisterController;
import com.matias.domuapp.controller.UserController;
import com.matias.domuapp.includes.MyToolbar;
import com.matias.domuapp.models.Cliente;
import com.matias.domuapp.models.Direccion;
import com.matias.domuapp.models.Persona;
import com.matias.domuapp.models.Usuario;
import com.matias.domuapp.providers.AuthProvider;
import com.matias.domuapp.providers.ClienteProvider;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import dmax.dialog.SpotsDialog;

public class RegisterActivity extends AppCompatActivity {

    AuthProvider mAuthProvider;
    ClienteProvider mClienteProvider;
    AlertDialog mDialog;

    Button mButtonRegisterCliente;
    Button mButtonRegisterProfesional;

    TextInputEditText mTextInputName;
    TextInputEditText mTextInputLastName;
    TextInputEditText mTextInputSecondName;
    TextInputEditText mTextInputBirthday;
    TextInputEditText mTextInputPhone;
    TextInputEditText mTextInputCountry;
    TextInputEditText mTextInputState;
    TextInputEditText mTextInputColony;
    TextInputEditText mTextInputCity;
    TextInputEditText mTextInputStreet;
    TextInputEditText mTextPostalCode;
    TextInputEditText mTextNumber;
    TextInputEditText mTextInputEmail;
    TextInputEditText mTextInputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mDialog = new SpotsDialog.Builder().setContext(RegisterActivity.this).setMessage("Espere un momento").build();
        mAuthProvider = new AuthProvider();
        mButtonRegisterCliente = findViewById(R.id.btnRegister);
        mButtonRegisterProfesional = findViewById(R.id.btnRegisterProfessional);
        mTextInputName = findViewById(R.id.textInputName);
        mTextInputLastName = findViewById(R.id.textInputLastName);
        mTextInputSecondName = findViewById(R.id.textInputSecondName);
        mTextInputBirthday = findViewById(R.id.textInputDate);
        mTextInputPhone = findViewById(R.id.textInputPhone);
        mTextInputCountry = findViewById(R.id.textInputCountry);
        mTextInputState = findViewById(R.id.textInputState);
        mTextInputColony = findViewById(R.id.textInputColony);
        mTextInputCity = findViewById(R.id.textInputCity);
        mTextInputStreet = findViewById(R.id.textInputStreet);
        mTextPostalCode = findViewById(R.id.textInputPostalCode);
        mTextNumber = findViewById(R.id.textInputNumber);
        mTextInputEmail = findViewById(R.id.textInputEmail);
        mTextInputPassword = findViewById(R.id.textInputPassword);

        mButtonRegisterCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterController registerController = new RegisterController();
                final Usuario finalUser=settingUser("Cliente");
                System.out.println("Creando Cliente "+ finalUser.toString());
                registerController.clickRegister(finalUser, mAuthProvider,RegisterActivity.this);
            }
        });
        mButtonRegisterProfesional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterController registerController = new RegisterController();
                final Usuario finalUser=settingUser("Profesional");
                System.out.println("Creando Profesional "+ finalUser.toString());
                registerController.clickRegister(finalUser, mAuthProvider,RegisterActivity.this);
            }
        });
    }


    Usuario settingUser(String type) {
        UserController userController = new UserController();
        Usuario user = userController.typeUser(type);
        Persona person = new Persona();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        Direccion direccion = new Direccion();
        person.setName(mTextInputName.getText().toString());
        person.setLastname(mTextInputLastName.getText().toString());
        person.setSecondname(mTextInputSecondName.getText().toString());
        try {
            date = dateFormat.parse(mTextInputBirthday.getText().toString());
        } catch (ParseException e) {
            mDialog.setMessage("Error con el formato de fecha de nacimiento");
            mDialog.show();
            mDialog.hide();
            e.printStackTrace();
        }
        person.setBirthDate(date);
        person.setPhone(mTextInputPhone.getText().toString());
        direccion.setCountry(mTextInputCountry.getText().toString());
        direccion.setState(mTextInputState.getText().toString());
        direccion.setCity(mTextInputCity.getText().toString());
        direccion.setColony(mTextInputColony.getText().toString());
        direccion.setStreet(mTextInputStreet.getText().toString());
        direccion.setPostalCode(mTextPostalCode.getText().toString());
        AddressController addressController = new AddressController();
        int num = addressController.gettingNumberofHouse(mTextNumber.getText().toString());
        direccion.setNumber(num);
        person.setAddress(direccion);
        user.setPerson(person);
        user.setEmail(mTextInputEmail.getText().toString());
        user.setPassword(mTextInputPassword.getText().toString());
        user.setTypeUser(type);
        System.out.println(user.toString());
        return user;
    }


   /* void clickRegister() {
        /*final String name = mTextInputName.getText().toString();
        final String email = mTextInputEmail.getText().toString();
        final String password = mTextInputPassword.getText().toString();
        final String address = mTextInputAddress.getText().toString();

        if(!name.isEmpty() && !email.isEmpty() && !password.isEmpty() && !address.isEmpty()){
            if (password.length() >= 6){
                mDialog.show();
                //register(name, email, password, address);

            }
            else{
                Toast.makeText(this, "La contraseña debe de tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "Ingrese todos los campos", Toast.LENGTH_SHORT).show();
        }
    }
    /*
    void register(final String name, final String email, String password, final String domicilio){
        mAuthProvider.register(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mDialog.hide();
                if (task.isSuccessful()){
                    String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Cliente cliente = new Cliente(id, name, email,domicilio);
                    create(cliente);gi
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

    void saveUser(String id, String name, String email) {
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
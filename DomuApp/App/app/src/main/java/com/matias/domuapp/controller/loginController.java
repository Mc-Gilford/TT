package com.matias.domuapp.controller;


import static androidx.core.content.ContextCompat.startActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.matias.domuapp.activities.LoginActivity;
import com.matias.domuapp.activities.MainActivity;
import com.matias.domuapp.models.Usuario;
import com.matias.domuapp.models.dao.UserDao;

import dmax.dialog.SpotsDialog;

public class loginController {
    /*private void goToSelectAuth() {
        Intent intent = new Intent(MainActivity.this, SelectOptionAuthActivity.class);
        ContextCompat.startActivity(intent);
    }*/
    public void login(Usuario user, AlertDialog mDialog, FirebaseAuth mAuth, Context context)
    {
        userController userController= new userController();
        if(userController.validateEmailandPassword(user,context))
        {
            mDialog = new SpotsDialog(context,"Espere un momento");
                    //.setContext(context).setMessage("Espere un momento").build();
            mDialog.show();
            signIn(mAuth,user,mDialog,context);
        }
    }
    private Boolean signIn(FirebaseAuth mAuth, Usuario user,AlertDialog mDialog, Context context){
        UserDao userDao = new UserDao();
        userDao.signIn(mAuth,user,mDialog,context);
     return false;
    }
}

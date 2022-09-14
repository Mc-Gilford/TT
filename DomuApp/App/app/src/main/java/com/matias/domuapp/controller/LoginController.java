package com.matias.domuapp.controller;


import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.matias.domuapp.models.Usuario;
import com.matias.domuapp.models.dao.UserDao;

import dmax.dialog.SpotsDialog;

public class LoginController {

    public void login(Usuario user, AlertDialog mDialog, FirebaseAuth mAuth, Context context)
    {
        UserController userController= new UserController();
        if(userController.validateEmailandPassword(user,context))
        {
            mDialog = new SpotsDialog.Builder().setContext(context).setMessage("Espere un momento").build();
            mDialog.show();
            signIn(mAuth,user,mDialog,context);
        }
    }


    @NonNull
    private Boolean signIn(FirebaseAuth mAuth, Usuario user,AlertDialog mDialog, Context context){
        UserDao userDao = new UserDao();
        userDao.signIn(mAuth,user,mDialog,context);
        return false;
    }
}
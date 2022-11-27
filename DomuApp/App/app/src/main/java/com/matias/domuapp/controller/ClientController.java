package com.matias.domuapp.controller;

import android.content.Context;
import android.content.Intent;

import com.matias.domuapp.activities.MainActivity;
import com.matias.domuapp.activities.cliente.MapClienteActivity;
import com.matias.domuapp.providers.AuthProvider;

public class ClientController {
    public void logout(AuthProvider mAuthProvider, Context context) {
        mAuthProvider.logout();
        Intent intent = new Intent(context,MainActivity.class);
        context.startActivity(intent);
    }
}

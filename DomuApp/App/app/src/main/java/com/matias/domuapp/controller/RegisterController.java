package com.matias.domuapp.controller;

import android.content.Context;
import android.content.Intent;

import com.matias.domuapp.activities.RegisterActivity;

public class RegisterController {
    public void goToRegister(Context context) {

            Intent intent = new Intent(context, RegisterActivity.class);
            context.startActivity(intent);

    }
}

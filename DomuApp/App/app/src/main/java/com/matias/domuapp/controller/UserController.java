package com.matias.domuapp.controller;

import android.content.Context;
import android.widget.Toast;

import com.matias.domuapp.models.Usuario;

public class UserController {
    public Boolean validateEmailandPassword(Usuario user, Context context)
    {
        if (!user.getEmail().isEmpty() && !user.getPassword().isEmpty()) {
            Toast.makeText(context, "La contraseña y el email son obligatorios", Toast.LENGTH_SHORT).show();
        }
        return !user.getEmail().isEmpty() && !user.getPassword().isEmpty()&&validateFields(user.getPassword(),user.getEmail(),context);
    }
    public Boolean validateFields(String password, String email,Context context)
    {
        if(password.length()>6 && email.contains("@")) {
            return true;
        }
        else {
            Toast.makeText(context, "La contraseña debe tener mas de 6 caracteres", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}

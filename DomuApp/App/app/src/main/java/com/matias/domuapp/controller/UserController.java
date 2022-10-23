package com.matias.domuapp.controller;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.matias.domuapp.activities.cliente.MapClienteActivity;
import com.matias.domuapp.activities.profesionista.MapProfesionistaActivity;
import com.matias.domuapp.models.Cliente;
import com.matias.domuapp.models.Profesional;
import com.matias.domuapp.models.Usuario;
import com.matias.domuapp.models.dao.UserDao;
import com.matias.domuapp.providers.ClienteProvider;
import com.matias.domuapp.providers.ProfesionistaProvider;

public class UserController {


    public Boolean validateEmailandPassword(Usuario user, Context context)
    {
        if (!user.getEmail().isEmpty() && !user.getPassword().isEmpty()) {
            Toast.makeText(context, "La contraseña y el email son obligatorios", Toast.LENGTH_SHORT).show();
        }
        return !user.getEmail().isEmpty() && !user.getPassword().isEmpty()&&validateFields(user.getPassword(),user.getEmail(),context);
    }
    public Boolean validateInformPerson(Usuario user, Context context){
        Boolean userValidationData = !user.getPerson().getName().isEmpty() && !user.getPerson().getLastname().isEmpty() && !user.getPerson().getBirthDate().toString().isEmpty();
        //System.out.println("Validando  logica"+user.getPerson().getName().isEmpty() +" "+ user.getPerson().getLastname().isEmpty() +" "+ user.getPerson().getBirthDate().toString().isEmpty());
        System.out.println("User datos "+user);
        if(!userValidationData){
            Toast.makeText(context, "Por favor verifique que sus datos esten llenos", Toast.LENGTH_SHORT).show();
        }
        return userValidationData && validateEmailandPassword(user,context);
    }

    public Boolean validateUser(Usuario user, Context context){
        Toast.makeText(context, "Validando usuario", Toast.LENGTH_SHORT).show();
        System.out.println("Validando "+ validateEmailandPassword(user,context) +" "+ validateInformPerson(user,context));
        return validateEmailandPassword(user,context) && validateInformPerson(user,context);
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


    public Boolean validarTipoDeUsuario(Usuario user, Context context)
    {
        Boolean status =false;
        System.out.println("Validador de tipo de Usuario");
        if(user.getTypeUser().equals("Cliente")){
            Intent intent = new Intent(context, MapClienteActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
            status=true;
            return status;
        }
        else {
            Intent intent = new Intent(context, MapProfesionistaActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
            return status;
        }
    }

    public Boolean createUser(Usuario user, Context context){
        UserDao userDao = new UserDao();
        System.out.println("Creando Usuario por tipo");
        if(user.getTypeUser()=="Cliente"){
            ClienteProvider mClienteProvider = new ClienteProvider();
            Usuario obj =  (Cliente) user;
            Cliente cliente = (Cliente) obj;
            userDao.createUser(cliente,mClienteProvider,context);
            return true;
        }else if(user.getTypeUser()=="Profesional"){
            ProfesionistaProvider profesionistaProvider = new ProfesionistaProvider();
            Profesional profesional = (Profesional) user;
            userDao.createUser(profesional,profesionistaProvider,context);
            return true;
        }
        return false;
    }

    public Usuario typeUser(String type){
        if(type == "Cliente"){
            return new Cliente();
        }else if (type=="Profesional"){
            return new Profesional();
        }
        return null;
    }
}

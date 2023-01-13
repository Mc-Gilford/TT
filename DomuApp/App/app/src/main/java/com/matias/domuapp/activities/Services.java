package com.matias.domuapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.matias.domuapp.R;
import com.matias.domuapp.activities.cliente.MapClienteActivity;
import com.matias.domuapp.activities.cliente.UpdateProfileActivity;
import com.matias.domuapp.activities.cliente.ViewListProfesionistActivity;
import com.matias.domuapp.activities.profesionista.MapProfesionistaActivity;
import com.matias.domuapp.adapters.ProfesionistasActiveAdapter;
import com.matias.domuapp.controller.ClientController;
import com.matias.domuapp.models.Cliente;
import com.matias.domuapp.models.dao.ClientDao;
import com.matias.domuapp.providers.AuthProvider;
import com.matias.domuapp.providers.ClienteProvider;
import com.squareup.picasso.Picasso;

public class Services extends AppCompatActivity {

    private Button btnSelectVetetinario;
    private Button btnSelectEstilista;
    private Button btnSeeProfile;
    private AuthProvider authProvider;
    private ClienteProvider clienteProvider;
    private TextView textView;
    private ImageView mImageViewProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
        btnSelectVetetinario = findViewById(R.id.btnSelectVeterinario);
        btnSelectEstilista = findViewById(R.id.btnSelectEstilista);
        mImageViewProfile = findViewById(R.id.imageViewProfile);
        textView = findViewById(R.id.text_view_id);
        authProvider = new AuthProvider();
        Cliente cliente = new Cliente();
        clienteProvider = new ClienteProvider();
        System.out.println("Id "+authProvider.getId());
        ClientController clientController = new ClientController();
        clientController.getProfileInformation(clienteProvider,authProvider,textView,Services.this,mImageViewProfile,
                "Bienvenido!\n");

        btnSelectVetetinario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Services.this, ViewListProfesionistActivity.class);
                intent.putExtra("Servicio","Veterinario");
                startActivity(intent);
            }
        });
        btnSelectEstilista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(Services.this, ViewListProfesionistActivity.class);
               intent.putExtra("Servicio","Estilista");
               startActivity(intent);
            }
        });



    }
}
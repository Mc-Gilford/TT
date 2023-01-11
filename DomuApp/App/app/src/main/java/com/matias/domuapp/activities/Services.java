package com.matias.domuapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.matias.domuapp.R;
import com.matias.domuapp.activities.profesionista.MapProfesionistaActivity;

public class Services extends AppCompatActivity {

    Button btnSelectVetetinario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
        btnSelectVetetinario = findViewById(R.id.btnSelectVeterinario);

        btnSelectVetetinario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Services.this, MapProfesionistaActivity.class);
                startActivity(intent);
            }
        });

    }
}
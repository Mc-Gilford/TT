package com.matias.domuapp.activities.cliente;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.matias.domuapp.R;
import com.matias.domuapp.includes.MyToolbar;

public class MaintenancePage extends AppCompatActivity {
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_page);
        // MyToolbar.show(this, "Mantenimiento", true);
    }
}

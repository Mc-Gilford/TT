package com.matias.domuapp.activities.profesionista;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.matias.domuapp.R;

import android.content.Intent;
import android.os.Bundle;

import com.matias.domuapp.adapters.HistoryBookingProfesionistAdapter;
import com.matias.domuapp.includes.MyToolbar;
import com.matias.domuapp.models.HistoryBooking;
import com.matias.domuapp.providers.AuthProvider;

public class HistoryBookingProfesionistActivity extends AppCompatActivity {

    private RecyclerView mReciclerView;
    private HistoryBookingProfesionistAdapter mAdapter;
    private AuthProvider mAuthProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_booking_profesionist);
        MyToolbar.show(this, "Historial de servicios", true);

        mReciclerView = findViewById(R.id.recyclerViewHistoryBooking);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mReciclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuthProvider = new AuthProvider();
        Intent intentBefore= getIntent();
        Bundle bundle = intentBefore.getExtras();
        if(bundle!=null){
            String id =(String) bundle.get("idProfesionista");
            System.out.println("Id Recibida "+id);
            Query query = FirebaseDatabase.getInstance().getReference()
                    .child("HistoryBooking")
                    .orderByChild("idProfesionist")
                    .equalTo(id);
            FirebaseRecyclerOptions<HistoryBooking> options = new FirebaseRecyclerOptions.Builder<HistoryBooking>()
                    .setQuery(query, HistoryBooking.class)
                    .build();
            mAdapter = new HistoryBookingProfesionistAdapter(options, HistoryBookingProfesionistActivity.this);
        }else{
        Query query = FirebaseDatabase.getInstance().getReference()
                .child("HistoryBooking")
                .orderByChild("idProfesionist")
                .equalTo(mAuthProvider.getId());
        FirebaseRecyclerOptions<HistoryBooking> options = new FirebaseRecyclerOptions.Builder<HistoryBooking>()
                .setQuery(query, HistoryBooking.class)
                .build();
        mAdapter = new HistoryBookingProfesionistAdapter(options, HistoryBookingProfesionistActivity.this);
        }
        mReciclerView.setAdapter(mAdapter);
        mAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
}

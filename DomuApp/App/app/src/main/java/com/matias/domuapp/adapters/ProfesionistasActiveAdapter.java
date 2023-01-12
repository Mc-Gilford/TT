package com.matias.domuapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.matias.domuapp.R;
import com.matias.domuapp.activities.cliente.HistoryBookingDetailClientActivity;
import com.matias.domuapp.activities.profesionista.HistoryBookingDetailProfesionistActivity;
import com.matias.domuapp.activities.profesionista.HistoryBookingProfesionistActivity;
import com.matias.domuapp.models.Profesional;
import com.matias.domuapp.providers.ProfesionistaProvider;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfesionistasActiveAdapter extends RecyclerView.Adapter<ProfesionistasActiveAdapter.ViewHolder> {
    ArrayList<Profesional> arrayList;
    private ProfesionistaProvider mProfesionistProvider;
    private Context mContext;
    public ProfesionistasActiveAdapter(ArrayList<Profesional> arrayList, ProfesionistaProvider mProfesionistProvider, Context mContext) {
        this.arrayList = arrayList;
        this.mProfesionistProvider = mProfesionistProvider;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ProfesionistasActiveAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_history_booking, parent, false);
        return new ProfesionistasActiveAdapter.ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.createData(arrayList.get(position),holder);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewName;
        private TextView textViewOrigin;
        private TextView textViewDestination;
        private TextView textViewCalification;
        private ImageView imageViewHistoryBooking;
        private View mView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            textViewName = view.findViewById(R.id.textViewName);
            textViewOrigin = view.findViewById(R.id.textViewOrigin);
            textViewDestination = view.findViewById(R.id.textViewDestination);
            textViewCalification = view.findViewById(R.id.textViewCalification);
            imageViewHistoryBooking = view.findViewById(R.id.imageViewHistoryBooking);
        }



        public void createData(final Profesional profesional, @NonNull final ViewHolder holder) {
            textViewName.setText(profesional.getPerson().getName()+" "+profesional.getPerson().getLastname()+" "+profesional.getPerson().getSecondname());
            textViewOrigin.setText(profesional.getServicio());
            //textViewDestination.setText(s);
            textViewCalification.setText(profesional.getScore().toString());
            Picasso.with(mContext).load(profesional.getImage()).into(holder.imageViewHistoryBooking);
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, HistoryBookingProfesionistActivity.class);
                    System.out.println("Profesional information "+profesional.getId());
                    intent.putExtra("idProfesionista", profesional.getId());
                    mContext.startActivity(intent);
                }
            });
        }
    }
}

package com.matias.domuapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.matias.domuapp.R;
import com.matias.domuapp.activities.cliente.MapClienteActivity;
import com.matias.domuapp.activities.profesionista.HistoryBookingProfesionistActivity;
import com.matias.domuapp.activities.profesionista.MapProfesionistaActivity;
import com.matias.domuapp.models.Profesional;
import com.matias.domuapp.models.Servicio;
import com.matias.domuapp.providers.ProfesionistaProvider;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder>{
    private ArrayList<Servicio> servicios ;
    private Context context;
    private Profesional profesional;
    public ServiceAdapter(ArrayList<Servicio> arrayList, Context mContext, Profesional profesional) {
        this.servicios = arrayList;
        this.context = mContext;
        this.profesional = profesional;
    }
    @NonNull
    @Override
    public ServiceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_servicio, parent, false);
        return new ServiceAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceAdapter.ViewHolder holder, int position) {
        holder.createData(profesional,servicios.get(position),holder);
    }

    @Override
    public int getItemCount() {
        return servicios.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nombreText;
        private TextView nombreServicioText;
        private TextView detallesText;
        private TextView costText;
        private ImageView imageView;
        private Button button;
        private View mView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
            nombreText = itemView.findViewById(R.id.textViewName);
            nombreServicioText = itemView.findViewById(R.id.textViewNameService);
            detallesText = itemView.findViewById(R.id.textViewDetalle);
            costText = itemView.findViewById(R.id.textViewCosto);
            imageView = itemView.findViewById(R.id.imageViewHistoryBooking);
            button = itemView.findViewById(R.id.btnSolicitar);
        }
        public void createData(final Profesional profesional, final Servicio servicio, @NonNull final ServiceAdapter.ViewHolder holder) {
            System.out.println("Creando mano");
            nombreText.setText(profesional.getPerson().getName()+" "+profesional.getPerson().getLastname()+" "+profesional.getPerson().getSecondname());
            nombreServicioText.setText(servicio.getName());
            detallesText.setText(servicio.getDetails());
            costText.setText(servicio.getCost().toString());
            System.out.println("Dinamita "+profesional.getImage());
            Picasso.with(context).load(profesional.getImage()).into(holder.imageView);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, MapClienteActivity.class);
                    intent.putExtra("Servicio", profesional.getServicio());
                    context.startActivity(intent);
                }
            });

        }
    }
}

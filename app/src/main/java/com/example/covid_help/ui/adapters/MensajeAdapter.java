package com.example.covid_help.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid_help.R;
import com.example.covid_help.ui.mensajes.Mensaje;

import java.util.List;

public class MensajeAdapter extends RecyclerView.Adapter<MensajeAdapter.MensajeHolder > {

    private List<Mensaje> listMensaje;
    private int resource;

    public MensajeAdapter (List<Mensaje> listMensaje, int resource){
        this.listMensaje = listMensaje;
        this.resource = resource;
    }

    @NonNull
    @Override
    public MensajeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new MensajeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MensajeHolder holder, int position) {
        //Obtenemos el mensaje
        holder.tvMensaje.setText(listMensaje.get(position).getMensaje());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class MensajeHolder extends RecyclerView.ViewHolder {

        private TextView tvNombre, tvMensaje;

        public MensajeHolder (View itemView){
            super(itemView);
            tvNombre = itemView.findViewById(R.id.textViewNombre);
            tvMensaje = itemView.findViewById(R.id.textViewMensaje);
        }
    }
}

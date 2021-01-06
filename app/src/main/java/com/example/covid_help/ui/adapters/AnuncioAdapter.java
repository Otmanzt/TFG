package com.example.covid_help.ui.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid_help.IniciarSesion;
import com.example.covid_help.Mensajeria;
import com.example.covid_help.R;
import com.example.covid_help.ui.models.Anuncio;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AnuncioAdapter extends RecyclerView.Adapter<AnuncioAdapter.ViewHolder> {

    private int resource;
    private ArrayList<Anuncio> anunciosList;

    public AnuncioAdapter (ArrayList<Anuncio> anunciosList, int resource){
        this.anunciosList = anunciosList;
        this.resource = resource;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Anuncio anuncio = anunciosList.get(position);
        final String id = anuncio.getId(); // Obtenemos el id del usuario del anuncio.

        FirebaseUser user;
        String idCur="";
        boolean log = false;
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            user = FirebaseAuth.getInstance().getCurrentUser();
            idCur = user.getUid();
            log=true;
        }
        final String idCurrent = idCur;
        final boolean log1 = log;

        holder.tituloAnuncio.setText(anuncio.getTitulo());
        holder.descripcionAnuncio.setText(anuncio.getDescripcion());
        holder.provinciaAnuncio.setText("Provincia: " + anuncio.getProvincia());
        holder.contactoAnuncio.setText("Teléfono: " + anuncio.getContacto());

        holder.btnEnviarMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (log1) {
                    if (!id.equals(idCurrent)) {
                        Intent intent = new Intent(v.getContext(), Mensajeria.class); //Abrir conversacion
                        intent.putExtra("userId", id); //LLevar id del propietario del anuncio
                        v.getContext().startActivity(intent);
                    } else {
                        Toast.makeText(v.getContext(), "Estás pulsando en tu anuncio.",
                                Toast.LENGTH_SHORT).show();
                    }
                }else {
                        Toast.makeText(v.getContext(), "Inicia sesión.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {

        return anunciosList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tituloAnuncio, descripcionAnuncio, provinciaAnuncio, contactoAnuncio;
        private Button btnEnviarMensaje;
        public View view;
        private DatabaseReference mDatabase;

        public ViewHolder (View view){
            super(view);

            this.view = view;
            tituloAnuncio = view.findViewById(R.id.textViewTitulo);
            descripcionAnuncio = view.findViewById(R.id.textViewDescripcion);
            provinciaAnuncio = view.findViewById(R.id.textViewProvincia);
            contactoAnuncio = view.findViewById(R.id.textViewContacto);
            btnEnviarMensaje = view.findViewById(R.id.botonEnviarMensaje);

            mDatabase = FirebaseDatabase.getInstance().getReference();
        }

        public View getView(){
            return view;
        }
    }
    public void filtrar (ArrayList<Anuncio> filtroAnuncio){
        this.anunciosList = filtroAnuncio;
        notifyDataSetChanged();
    }
}

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

import com.example.covid_help.Mensajeria;
import com.example.covid_help.R;
import com.example.covid_help.ui.models.Anuncio;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MisAnunciosAdapter extends RecyclerView.Adapter<MisAnunciosAdapter.ViewHolder>{

    private int resource;
    private ArrayList<Anuncio> anunciosList;

    public MisAnunciosAdapter (ArrayList<Anuncio> anunciosList, int resource){
        this.anunciosList = anunciosList;
        this.resource = resource;
    }

    @NonNull
    @Override
    public MisAnunciosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);

        return new MisAnunciosAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

            final Anuncio anuncio = anunciosList.get(position);
            holder.tituloAnuncio.setText(anuncio.getTitulo());

            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


            holder.btnBorrarAnuncio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.mDatabase.child(user.getUid()).child("Anuncios").child(anuncio.getId()).removeValue();
                    anunciosList.clear();
                    Toast.makeText(v.getContext(), "Anuncio borrado",
                            Toast.LENGTH_SHORT).show();
                }
            });


    }


    @Override
    public int getItemCount() {

        return anunciosList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tituloAnuncio;
        private Button btnBorrarAnuncio;
        public View view;
        private DatabaseReference mDatabase;

        public ViewHolder (View view){
            super(view);

            this.view = view;
            tituloAnuncio = view.findViewById(R.id.tituloAnuncioCard);
            btnBorrarAnuncio = view.findViewById(R.id.botonBorrarAnuncio);

            mDatabase = FirebaseDatabase.getInstance().getReference("Usuarios");
        }

        public View getView(){
            return view;
        }
    }
}

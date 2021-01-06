package com.example.covid_help.ui.anuncios;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid_help.R;
import com.example.covid_help.ui.adapters.AnuncioAdapter;
import com.example.covid_help.ui.adapters.MisAnunciosAdapter;
import com.example.covid_help.ui.iniciarSesion.IniciarSesionViewModel;
import com.example.covid_help.ui.models.Anuncio;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AnuncioFragment extends Fragment {

    private AnuncioViewModel anuncioViewModel;
    private DatabaseReference mDatabase;
    private FirebaseUser user;

    private MisAnunciosAdapter misAnunciosAdapter;
    private RecyclerView anunciosRecyclerView;
    private ArrayList<Anuncio> anuncioArrayList = new ArrayList<>();
    private final static int NUM_GRIDS=1;

    private Button btnBorrarAnuncio;

    private boolean estaLista;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        anuncioViewModel =
                ViewModelProviders.of(this).get(AnuncioViewModel.class);
        View root = inflater.inflate(R.layout.fragment_anuncios, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

        btnBorrarAnuncio = root.findViewById(R.id.botonBorrarAnuncio);

        anunciosRecyclerView = root.findViewById(R.id.recyclerViewAnuncios);
        anunciosRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), NUM_GRIDS));
        anunciosRecyclerView.setHasFixedSize(true);

        getMisAnunciosFromFirebase(user.getUid());

        anuncioArrayList.clear();

        return root;
    }

    private void getMisAnunciosFromFirebase(String myId) {
        mDatabase.child("Usuarios").child(myId).addValueEventListener(new ValueEventListener() { //Nodo del usuario
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    if (snapshot.child("Anuncios").getChildren() != null) { //Nodo anuncios del usuario (si existe)
                        for (DataSnapshot ds : snapshot.child("Anuncios").getChildren()) {
                            Anuncio anuncio = new Anuncio();
                            String titulo = ds.child("titulo").getValue().toString();
                            anuncio.setTitulo(titulo);
                            anuncio.setId(ds.getKey());
                            anuncio.setContacto("");
                            anuncio.setDescripcion("");
                            anuncio.setProvincia("");

                            estaEnLista(anuncioArrayList, ds.getKey());
                            if (!estaLista) {
                                anuncioArrayList.add(anuncio);
                            }

                            }
                    }
                }
                estaLista = true;
                misAnunciosAdapter = new MisAnunciosAdapter(anuncioArrayList, R.layout.card_view_anuncios);
                anunciosRecyclerView.setAdapter(misAnunciosAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void estaEnLista(ArrayList<Anuncio> anuncioArrayList, String id) {
        for (Anuncio anuncio : anuncioArrayList){
            if (anuncio.getId() == id){
                estaLista = true;
            }
        }
        estaLista = false;
    }
}

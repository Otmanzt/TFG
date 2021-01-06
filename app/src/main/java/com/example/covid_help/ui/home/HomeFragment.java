package com.example.covid_help.ui.home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid_help.R;
import com.example.covid_help.ui.adapters.AnuncioAdapter;
import com.example.covid_help.ui.models.Anuncio;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private DatabaseReference mDatabase;

    private AnuncioAdapter anuncioAdapter;
    private RecyclerView anuncioRecyclerView;
    private ArrayList<Anuncio> anuncioArrayList = new ArrayList<>();

    private final static int NUM_GRIDS=1;

    private EditText editTextBuscador;

    private boolean estaLista;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        editTextBuscador = root.findViewById(R.id.editTextBuscador);

        editTextBuscador.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filtrar(s.toString());
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();

        anuncioRecyclerView = root.findViewById(R.id.recyclerViewHome);
        anuncioRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), NUM_GRIDS));
        anuncioRecyclerView.setHasFixedSize(true);

        getAnunciosFromFirebase();


        anuncioArrayList.clear();
        return root;
    }

    private void filtrar(String texto) {
        ArrayList<Anuncio> listFiltrada = new ArrayList<>();

        for (Anuncio anuncio: anuncioArrayList){
            if (anuncio.getTitulo().toLowerCase().contains(texto) || anuncio.getDescripcion().toLowerCase().contains(texto) ||
                anuncio.getProvincia().toLowerCase().contains(texto)){
                listFiltrada.add(anuncio);
            }
        }
        anuncioAdapter.filtrar(listFiltrada);
    }

    private void getAnunciosFromFirebase (){
        mDatabase.child("Usuarios").addValueEventListener(new ValueEventListener() { //Todos los usuarios
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String id = "";
                    for (DataSnapshot ds: snapshot.getChildren()){
                        id = ds.getKey();
                           for (DataSnapshot ds2 : ds.child("Anuncios").getChildren()){
                               String titulo = ds2.child("titulo").getValue().toString();
                               String descripcion = ds2.child("descripcion").getValue().toString();
                               String provincia = ds2.child("provincia").getValue().toString();
                               String contacto = ds2.child("contacto").getValue().toString();
                               estaEnLista(anuncioArrayList, ds2.getKey());
                               if (!estaLista) {
                                   //Añadimos el anuncio a la lista
                                   anuncioArrayList.add(new Anuncio(id, titulo, descripcion, provincia, contacto));
                               }
                           }

                        }

                    }

                    estaLista = true;
                    anuncioAdapter = new AnuncioAdapter(anuncioArrayList, R.layout.home_view); // Nuevo adaptador
                    anuncioRecyclerView.setAdapter(anuncioAdapter); // Asignamos adaptador al RecyclerView



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

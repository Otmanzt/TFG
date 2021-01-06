package com.example.covid_help.ui.publicarAnuncio;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.covid_help.MainActivity;
import com.example.covid_help.PublicarAnuncio;
import com.example.covid_help.R;
import com.example.covid_help.ui.publicarAnuncio.PublicarAnuncioViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PublicarAnuncioFragment extends Fragment {
    private PublicarAnuncioViewModel publicarAnuncioViewModel;
    private Spinner spinner;
    private Button botonPublicarAnuncio;
    EditText titulo, descripcion, contacto;
    DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        publicarAnuncioViewModel =
                ViewModelProviders.of(this).get(PublicarAnuncioViewModel.class);
        View root = inflater.inflate(R.layout.activity_publicar_anuncio, container, false);

        titulo = root.findViewById(R.id.tituloAnuncio);

        descripcion = root.findViewById(R.id.descripcionAnuncio);

        spinner = root.findViewById(R.id.spinnerProvincia);

        ArrayList<String> provincias = new ArrayList<>();

        provincias.add("Provincia");
        provincias.add("A. Coruña");
        provincias.add("Álava");
        provincias.add("Albacete");
        provincias.add("Alicante");
        provincias.add("Almería");
        provincias.add("Asturias");
        provincias.add("Ávila");
        provincias.add("Badajoz");
        provincias.add("Baleares");
        provincias.add("Barcelona");
        provincias.add("Burgos");
        provincias.add("Cáceres");
        provincias.add("Cádiz");
        provincias.add("Cantabria");
        provincias.add("Castellón");
        provincias.add("Ciudad Real");
        provincias.add("Córdoba");
        provincias.add("Cuenca");
        provincias.add("Girona");
        provincias.add("Granada");
        provincias.add("Guadalajara");
        provincias.add("Gipuzkoa");
        provincias.add("Huelva");
        provincias.add("Huesca");
        provincias.add("Jaén");
        provincias.add("La Rioja");
        provincias.add("Las Palmas");
        provincias.add("León");
        provincias.add("Lérida");
        provincias.add("Lugo");
        provincias.add("Madrid");
        provincias.add("Málaga");
        provincias.add("Murcia");
        provincias.add("Navarra");
        provincias.add("Ourense");
        provincias.add("Palencia");
        provincias.add("Pontevedra");
        provincias.add("Salamanca");
        provincias.add("Segovia");
        provincias.add("Sevilla");
        provincias.add("Soria");
        provincias.add("Tarragona");
        provincias.add("Tenerife");
        provincias.add("Teruel");
        provincias.add("Toledo");
        provincias.add("Valencia");
        provincias.add("Valladolid");
        provincias.add("Vizcaya");
        provincias.add("Zamora");
        provincias.add("Zaragoza");
        provincias.add("Ceuta");
        provincias.add("Melilla");

        ArrayAdapter arrayAdp = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, provincias){
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textview = (TextView) view;
                if (position == 0) {
                    textview.setTextColor(Color.BLACK);
                } else {
                    textview.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        spinner.setAdapter(arrayAdp);

        contacto = root.findViewById(R.id.contactoAnuncio);

        botonPublicarAnuncio = root.findViewById(R.id.botonPublicarAnuncio);

        botonPublicarAnuncio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tituloAnuncio = titulo.getText().toString();
                String descripcionAnuncio = descripcion.getText().toString();
                String provinciaAnuncio = "";
                if (spinner.getSelectedItem() != null){
                    provinciaAnuncio = spinner.getSelectedItem().toString();
                }
                String contactoAnuncio = contacto.getText().toString();

                if (!tituloAnuncio.equals("") && !descripcionAnuncio.equals("") && !provinciaAnuncio.equals("")
                    && !contactoAnuncio.equals("")){ //Ningun campo vacío
                    mAuth = FirebaseAuth.getInstance();
                    mDatabase = FirebaseDatabase.getInstance().getReference();

                    // Base de datos
                    final Map<String, Object> map = new HashMap<>();
                    map.put("titulo", tituloAnuncio);
                    map.put("descripcion", descripcionAnuncio);
                    map.put("provincia", provinciaAnuncio);
                    map.put("contacto", contactoAnuncio);

                    String id = mAuth.getCurrentUser().getUid();

                    mDatabase.child("Usuarios").child(id).child("Anuncios").push().setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if (task2.isSuccessful()) {
                                Toast.makeText(getActivity(), "Anuncio subido correctamente.",
                                        Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getActivity(), MainActivity.class);
                                startActivity(i);
                            } else {
                                Toast.makeText(getActivity(), "Error al crear los datos en la base de datos.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(getActivity(), "No debe haber ningún campo nulo.",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

        return root;
    }
}
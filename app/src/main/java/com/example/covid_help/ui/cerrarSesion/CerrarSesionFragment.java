package com.example.covid_help.ui.cerrarSesion;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.covid_help.MainActivity;
import com.example.covid_help.R;
import com.example.covid_help.ui.iniciarSesion.IniciarSesionViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class CerrarSesionFragment extends Fragment {
    private CerrarSesionViewModel cerrarSesionViewModel;
    Button cerrarSesion, volverInicio;
    FirebaseAuth mAuth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        cerrarSesionViewModel = ViewModelProviders.of(this).get(CerrarSesionViewModel.class);
        View root = inflater.inflate(R.layout.fragment_cerrarsesion, container, false);

        // Inicializar instancia de Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        cerrarSesion = root.findViewById(R.id.botonCerrarSesion);
        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut(); // Cerrar sesión al pulsar el botón
                startActivity(new Intent(getActivity(), MainActivity.class)); // Redirigir a la pantalla principal
            }
        });

        volverInicio = root.findViewById(R.id.botonVolverInicio);
        volverInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });

        return root;
    }
}

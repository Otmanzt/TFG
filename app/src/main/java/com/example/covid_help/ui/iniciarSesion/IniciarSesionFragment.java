package com.example.covid_help.ui.iniciarSesion;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.covid_help.IniciarSesion;
import com.example.covid_help.R;
import com.example.covid_help.Registro;

public class IniciarSesionFragment extends Fragment {
    private IniciarSesionViewModel iniciarSesionViewModel;
    Button registrar, iniciarSesion;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        iniciarSesionViewModel =
                ViewModelProviders.of(this).get(IniciarSesionViewModel.class);
        View root = inflater.inflate(R.layout.fragment_iniciarsesion, container, false);

        // Pasar del botón de registrar a formulario de registro
        registrar = root.findViewById(R.id.botonRegistrar);

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentReg = new Intent (getActivity(), Registro.class);
                startActivity(intentReg);
            }
        });

        // Pasar del botón de iniciar sesión a la pantalla principal
        iniciarSesion = root.findViewById(R.id.botonLogin);

        iniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentReg = new Intent (getActivity(), IniciarSesion.class);
                startActivity(intentReg);
            }
        });

        return root;
    }

}

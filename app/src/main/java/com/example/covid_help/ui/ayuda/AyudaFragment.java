package com.example.covid_help.ui.ayuda;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.covid_help.R;

public class AyudaFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;

    private EditText editTextNombre, editTextDescripcion;
    private Button btnEnviar;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);

        editTextNombre = root.findViewById(R.id.ediTextAyudaNombre);
        editTextDescripcion = root.findViewById(R.id.editTextAyudaDescripcion);
        btnEnviar = root.findViewById(R.id.botonEnviarAyuda);


        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = editTextNombre.getText().toString();
                String descripcion = editTextDescripcion.getText().toString();

                if (!nombre.equals("") && !descripcion.equals("")) {
                    String mailto = "mailto:otmanzt@gmail.com" +
                            "?cc=" +
                            "&subject=" + Uri.encode(nombre) +
                            "&body=" + Uri.encode(descripcion);
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    emailIntent.setData(Uri.parse(mailto));

                    try {
                        startActivity(emailIntent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(getContext(), "Error al enviar el mensaje", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getContext(), "Los campos no pueden quedar vacíos", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return root;
    }
}

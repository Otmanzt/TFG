package com.example.covid_help;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.covid_help.ui.iniciarSesion.IniciarSesionFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class PublicarAnuncio extends AppCompatActivity {

    EditText titulo, descripcion, contacto;
    Spinner spinner;
    DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private Button botonPublicarAnuncio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicar_anuncio);

        titulo = findViewById(R.id.tituloAnuncio);
        String tituloAnuncio = titulo.getText().toString();

        descripcion = findViewById(R.id.descripcionAnuncio);
        String descripcionAnuncio = descripcion.getText().toString();

        spinner = findViewById(R.id.spinnerProvincia);
        int o = spinner.getSelectedItemPosition();
        String provinciaAnuncio = spinner.getSelectedItem().toString();

        contacto = findViewById(R.id.contactoAnuncio);
        String contactoAnuncio = contacto.getText().toString();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Base de datos
        final Map<String, Object> map = new HashMap<>();
        map.put("titulo", tituloAnuncio);
        map.put("descripcion", descripcionAnuncio);
        map.put("provincia", provinciaAnuncio);
        map.put("contacto", contactoAnuncio);

        String id = mAuth.getCurrentUser().getUid();

        botonPublicarAnuncio = findViewById(R.id.botonPublicarAnuncio);

        mDatabase.child("Usuarios").child(id).child("Anuncios").setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task2) {
                if (task2.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Anuncio subido correctamente.",
                            Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "Error al crear los datos en la base de datos.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}

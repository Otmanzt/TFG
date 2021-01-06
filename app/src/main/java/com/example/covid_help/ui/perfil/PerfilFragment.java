package com.example.covid_help.ui.perfil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.covid_help.R;
import com.example.covid_help.ui.home.HomeViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class PerfilFragment extends Fragment {

    private PerfilViewModel perfilViewModel;

    private DatabaseReference reference;
    private FirebaseUser user;

    private EditText editTextNombre, editTextEmail, editTextPass;

    private Button btnUpdate;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        perfilViewModel =
                ViewModelProviders.of(this).get(PerfilViewModel.class);
        View root = inflater.inflate(R.layout.fragment_perfil, container, false);

        //Set components
        editTextNombre = root.findViewById(R.id.editTextNombrePerfil);
        editTextEmail = root.findViewById(R.id.editTextEmailPerfil);
        editTextPass = root.findViewById(R.id.editTextPassPerfil);
        btnUpdate = root.findViewById(R.id.botonUpdatePerfil);

        // Inicializar usuario logeado y base de datos
        user = FirebaseAuth.getInstance().getCurrentUser();
        // Se obtiene la referencia del usuario de la base de datos usando su id
        reference = FirebaseDatabase.getInstance().getReference("Usuarios").child(user.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Recuperar los datos de la base de datos
                String nombre = snapshot.child("nombre").getValue().toString();
                String email = snapshot.child("correo").getValue().toString();
                String pass = snapshot.child("password").getValue().toString();
                //Asignar los valores a los campos del formulario
                editTextNombre.setText(nombre);
                editTextEmail.setText(email);
                editTextPass.setText(pass);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editTextNombre.getText().toString().equals("") && !editTextEmail.getText().toString().equals("")
                        && !editTextPass.getText().toString().equals("")){ //campos != nulos
                    if (editTextPass.getText().toString().length() >= 6){ //Contraseña >= 6
                        // Actualizar la parte de autenticacion
                        user.updateEmail(editTextEmail.getText().toString());
                        user.updatePassword(editTextPass.getText().toString());

                        //Actualizar la parte de base de datos
                        reference.child("nombre").setValue(editTextNombre.getText().toString());
                        reference.child("correo").setValue(editTextEmail.getText().toString());
                        reference.child("password").setValue(editTextPass.getText().toString());
                        Toast.makeText(getContext(), "Se han actualizado los datos correctamente",
                                Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getContext(), "La contraseña debe tener al menso 6 caracteres",
                                Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getContext(), "Los campos no pueden quedar vacíos",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;

    }
}

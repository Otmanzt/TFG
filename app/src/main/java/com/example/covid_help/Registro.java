package com.example.covid_help;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.covid_help.ui.iniciarSesion.IniciarSesionFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Registro extends AppCompatActivity {

    private FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    EditText nombre, correo, password;
    Button registro;
    Calendar calendario = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        nombre = findViewById(R.id.nombreRegistro);
        correo = findViewById(R.id.correoRegistro);
        password = findViewById(R.id.passRegistro);


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    public void registrarUsuario (View view){
        if (!nombre.getText().toString().equals("") && !correo.getText().toString().equals("")
            && !password.getText().toString().equals("")) { //Ningun campo vacío
            if (password.getText().toString().length() >= 6) { //Longitud de la contraseña >= 6
                mAuth.createUserWithEmailAndPassword(correo.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(getApplicationContext(), "Usuario creado.",
                                            Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    // Base de datos
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("nombre", nombre.getText().toString());
                                    map.put("correo", correo.getText().toString());
                                    map.put("password", password.getText().toString());

                                    String id = user.getUid();

                                    mDatabase.child("Usuarios").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task2) {
                                            if (task2.isSuccessful()) {
                                                Intent i = new Intent(getApplicationContext(), IniciarSesionFragment.class);
                                                startActivity(i);
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Error al crear los datos en la base de datos.",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });


                                    //updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.

                                    Toast.makeText(getApplicationContext(), "Error al registrarse.",
                                            Toast.LENGTH_SHORT).show();
                                    //updateUI(null);
                                }

                                // ...
                            }
                        });
            }else {
                Toast.makeText(getApplicationContext(), "La contraseña debe tener una longitud mínima de 6 caracteres.",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "No debe haber ningún campo nulo.",
                    Toast.LENGTH_SHORT).show();
        }
    }



}

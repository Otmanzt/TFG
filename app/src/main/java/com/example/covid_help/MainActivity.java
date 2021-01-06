package com.example.covid_help;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private View view1,view2;
    Button registrar;
    TextView textNombre,textCorreo;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    boolean log=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        // Comprobar si el usuario está logeado
        user = mAuth.getCurrentUser();
        if (user != null){
            //logeado
            log = true;
            getUserInfo();
        }else {
            //No logeado
            log=false;
        }

        if (log == false) {
            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.getMenu().findItem(R.id.nav_cerrar_sesion).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_inicar_sesion).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_publicar_anuncio).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_mensajes).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_perfil).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_anuncios).setVisible(false);

            View navHeader = navigationView.getHeaderView(0);
            textNombre = navHeader.findViewById(R.id.textView1);
            textCorreo = navHeader.findViewById(R.id.textView2);
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_inicio, R.id.nav_mensajes, R.id.nav_ayuda)
                    .setDrawerLayout(drawer)
                    .build();
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
            NavigationUI.setupWithNavController(navigationView, navController);


        }else {
            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.getMenu().findItem(R.id.nav_inicar_sesion).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_cerrar_sesion).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_publicar_anuncio).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_mensajes).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_perfil).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_anuncios).setVisible(true);

            View navHeader = navigationView.getHeaderView(0);
            textNombre = navHeader.findViewById(R.id.textView1);
            textCorreo = navHeader.findViewById(R.id.textView2);
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_inicio, R.id.nav_mensajes, R.id.nav_ayuda)
                    .setDrawerLayout(drawer)
                    .build();
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
            NavigationUI.setupWithNavController(navigationView, navController);

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void getUserInfo() {
        String id = mAuth.getCurrentUser().getUid();
        mDatabase.child("Usuarios").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    String nombre = dataSnapshot.child("nombre").getValue().toString();
                    String correo = dataSnapshot.child("correo").getValue().toString();
                    textNombre.setText(nombre);
                    textCorreo.setText(correo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}

package com.example.covid_help;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covid_help.ui.adapters.MensajesAdapter;
import com.example.covid_help.ui.mensajes.APIService;
import com.example.covid_help.ui.mensajes.Mensaje;
import com.example.covid_help.ui.mensajes.MensajeRecibir;
import com.example.covid_help.ui.models.User;
import com.example.covid_help.ui.notificacion.Client;
import com.example.covid_help.ui.notificacion.Data;
import com.example.covid_help.ui.notificacion.Emisor;
import com.example.covid_help.ui.notificacion.MyResponse;
import com.example.covid_help.ui.notificacion.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Mensajeria extends AppCompatActivity {

    public final static String MENSAJE = "MENSAJE";

    private BroadcastReceiver bR;

    private FirebaseUser user;
    private DatabaseReference reference;

    private Intent intent;

    private RecyclerView rvMensajeria;
    private Button btnEnviarMensaje;
    private EditText etEscribirMensaje;
    private TextView nombreUsuario,hora;
    private List<Mensaje> listMensaje;
    private MensajesAdapter mensajesAdapter;
    private int TEXT_LINES = 1;

    private APIService apiService;

    private boolean notify = false;

    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensajeria);

        // Set componentes
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        btnEnviarMensaje = findViewById(R.id.botonChat);
        etEscribirMensaje = findViewById(R.id.mensajeChat);
        rvMensajeria = findViewById(R.id.recyclerViewChat);
        nombreUsuario = findViewById(R.id.nombreChat);

        intent = getIntent();
        userId = intent.getStringExtra("userId");

        user = FirebaseAuth.getInstance().getCurrentUser();


        //rvMensajeria.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setStackFromEnd(true);
        rvMensajeria.setLayoutManager(lm);


        btnEnviarMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify = true;
                String mensaje = etEscribirMensaje.getText().toString();
                String TOKEN = FirebaseInstanceId.getInstance().getToken();
                if (!mensaje.isEmpty()){
                    createMensaje(user.getUid(), userId, mensaje);
                }
                etEscribirMensaje.setText("");
            }
        });

        reference = FirebaseDatabase.getInstance().getReference("Usuarios").child(userId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nombreUsuario.setText(snapshot.child("nombre").getValue().toString());
                readMensaje(user.getUid(), userId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        //setScrollBarChat();

        bR = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(Mensajeria.this, "El boradcast funciona", Toast.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(bR);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(bR, new IntentFilter(MENSAJE));
    }

    public void createMensaje (final String emisor, final String receptor, String mensaje){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("Emisor", emisor);
        hashMap.put("Receptor", receptor);
        hashMap.put("Mensaje", mensaje);
        hashMap.put("Hora", ServerValue.TIMESTAMP);

        reference.child("Chat").push().setValue(hashMap);
        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(emisor)
                .child(receptor);

        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    chatRef.child("id").setValue(receptor);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        final DatabaseReference chatRef2 = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(receptor)
                .child(emisor);

        chatRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    chatRef2.child("id").setValue(emisor);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        final String msg = mensaje;

        reference = FirebaseDatabase.getInstance().getReference("Usuarios").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = new User();
                user.setUsername(snapshot.child("nombre").getValue().toString());
                if (notify) {
                    sendNotificacion(receptor, user.getUsername(), msg);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //UpdateToken();
    }

    private void UpdateToken(){
        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        Token token = new Token(refreshToken);
        FirebaseDatabase.getInstance().getReference("Tokens").child(user.getUid()).setValue(token);
    }

    private void sendNotificacion (final String receptor, final String username, final String mensaje){
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receptor);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    Token token = ds.getValue(Token.class);
                    Data data = new Data (user.getUid(), R.mipmap.ic_launcher, username + ": " + mensaje, "Mensaje nuevo", userId);
                    Emisor emisor = new Emisor(data, token.getToken());

                    apiService.sendNotification(emisor)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200){
                                        if (response.body().success != 1){
                                            Toast.makeText(Mensajeria.this, "¡Fallo!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setScrollBarChat(){
        rvMensajeria.scrollToPosition(mensajesAdapter.getItemCount()-1);
    }

    public void readMensaje (final String myid, final String userid){
        listMensaje = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Chat");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listMensaje.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    Mensaje chat = new Mensaje();
                    chat.setEmisor(ds.child("Emisor").getValue().toString());
                    chat.setReceptor(ds.child("Receptor").getValue().toString());
                    chat.setMensaje(ds.child("Mensaje").getValue().toString());
                    // Hora
                    chat.setHora(ds.child("Hora").getValue().toString());

                    if (chat.getReceptor().equals(myid) && chat.getEmisor().equals(userid) ||
                        chat.getReceptor().equals(userid) && chat.getEmisor().equals(myid)){
                        listMensaje.add(chat);
                    }
                    mensajesAdapter = new MensajesAdapter(listMensaje, Mensajeria.this);
                    rvMensajeria.setAdapter(mensajesAdapter);
                    mensajesAdapter.notifyDataSetChanged();
                    setScrollBarChat();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

package com.example.covid_help.ui.mensajes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid_help.R;
import com.example.covid_help.ui.adapters.MensajesAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatFragment extends Fragment {

    private MensajesViewModel galleryViewModel;


    private CircleImageView fotoPerfilChat;
    private RecyclerView rvMensajes;
    private TextView nombre;
    private EditText mensajeChat;
    private Button botonChat;

    private MensajesAdapter mensajesAdapter;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(MensajesViewModel.class);
        View root = inflater.inflate(R.layout.mensajeria, container, false);

        // Set componentes

        fotoPerfilChat = root.findViewById(R.id.fotoPerfilChat);
        rvMensajes = root.findViewById(R.id.recyclerViewChat);
        nombre = root.findViewById(R.id.nombreChat);
        mensajeChat = root.findViewById(R.id.mensajeChat);
        botonChat = root.findViewById(R.id.botonChat);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Chat"); // Sala de chat.


        mensajesAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                setScrollbar();
            }
        });

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MensajeRecibir m = snapshot.getValue(MensajeRecibir.class);
                mensajesAdapter.addMensaje(m);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return root;
    }

    private void setScrollbar (){
        rvMensajes.scrollToPosition(mensajesAdapter.getItemCount()-1);
    }
}

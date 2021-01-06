package com.example.covid_help.ui.mensajes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.covid_help.R;
import com.example.covid_help.ui.VolleyRP;
import com.example.covid_help.ui.adapters.AmigosAdapter;
import com.example.covid_help.ui.adapters.MensajeAdapter;
import com.example.covid_help.ui.adapters.MensajesAdapter;
import com.example.covid_help.ui.amigos.AmigosAtributos;
import com.example.covid_help.ui.models.Chatlist;
import com.example.covid_help.ui.models.User;
import com.example.covid_help.ui.notificacion.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MensajesFragment extends Fragment {
    private MensajesViewModel galleryViewModel;

    private RecyclerView rvAmigos;
    private List<AmigosAtributos> atributosList;
    private AmigosAdapter amigosAdapter;

    private FirebaseUser user;
    private DatabaseReference reference;
    private List<Chatlist> usersList;

    private boolean isChat;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(MensajesViewModel.class);
        View root = inflater.inflate(R.layout.activity_amigos, container, false);

        // Set componentes

        rvAmigos = root.findViewById(R.id.recyclerViewAmigos);
        rvAmigos.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        rvAmigos.setLayoutManager(lm);

        atributosList = new ArrayList<>();

        user = FirebaseAuth.getInstance().getCurrentUser();

        usersList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference();
        getChatList(user.getUid());

        updateToken(FirebaseInstanceId.getInstance().getToken());

        atributosList.clear();

        return root;
    }

    private void updateToken (String token){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token2 = new Token(token);

        reference.child(user.getUid()).setValue(token2);
    }

    private void getChatList (String id){
        reference.child("Chatlist").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    Chatlist chatlist = ds.getValue(Chatlist.class);
                    usersList.add(chatlist);
                }
                chatList();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void chatList() {

        reference = FirebaseDatabase.getInstance().getReference("Usuarios");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                atributosList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    AmigosAtributos amigosAtributos = new AmigosAtributos();
                    amigosAtributos.setId(ds.getKey());
                    amigosAtributos.setUsername(ds.child("nombre").getValue().toString());

                    for (Chatlist chatlist: usersList){
                        if (amigosAtributos.getId().equals(chatlist.getId())){
                            isChat = true;
                            atributosList.add(amigosAtributos);
                        }
                    }
                }

                amigosAdapter = new AmigosAdapter(atributosList, getContext(), isChat);
                rvAmigos.setAdapter(amigosAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
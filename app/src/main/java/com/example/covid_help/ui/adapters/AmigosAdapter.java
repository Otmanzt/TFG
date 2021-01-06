package com.example.covid_help.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid_help.Mensajeria;
import com.example.covid_help.R;
import com.example.covid_help.ui.amigos.AmigosAtributos;
import com.example.covid_help.ui.mensajes.Mensaje;
import com.example.covid_help.ui.mensajes.MensajesFragment;
import com.example.covid_help.ui.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AmigosAdapter extends RecyclerView.Adapter<AmigosAdapter.HolderAmigos> {

    private List<AmigosAtributos> atributosList;
    private Context context;
    private boolean isChat;
    private FirebaseUser firebaseUser;

    String ultimoMensaje;

    public AmigosAdapter (List<AmigosAtributos> atributosList, Context context, boolean isChat){
        this.atributosList = atributosList;
        this.context = context;
        this.isChat = isChat;
    }

    @NonNull
    @Override
    public HolderAmigos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_amigos, parent, false);
        return new AmigosAdapter.HolderAmigos(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final HolderAmigos holder, final int position) {

        holder.nombre.setText(atributosList.get(position).getUsername());

        //Ultimo mensaje
        final AmigosAtributos user = atributosList.get(position);
        lastMessage(user.getId(), holder.mensaje);

        // Si se pulsa una conver
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Mensajeria.class);
                intent.putExtra("userId", user.getId());
                context.startActivity(intent);
            }
        });

        // Boton borrar conver
        final FirebaseUser userCurrent = FirebaseAuth.getInstance().getCurrentUser();
        holder.btnBorrarConver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Borrar la conver del nodo chatlist

                holder.mDatabase.child("Chatlist").child(userCurrent.getUid()).child(user.getId()).removeValue();
                //atributosList.remove(position);

                Toast.makeText(v.getContext(), "Conversación borrada",
                        Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return atributosList.size();
    }

    class HolderAmigos extends RecyclerView.ViewHolder {

        private CardView cardView;
        private TextView nombre, mensaje, hora;
        private Button btnBorrarConver;
        private DatabaseReference mDatabase;

        public HolderAmigos(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombreAmigo);
            mensaje = itemView.findViewById(R.id.mensajeAmigo);
            hora = itemView.findViewById(R.id.horaAmigo);
            btnBorrarConver = itemView.findViewById(R.id.botonBorrarConver);
            mDatabase = FirebaseDatabase.getInstance().getReference();
        }
    }

    //Comprobar ultimo mensaje
    public void lastMessage(final String userid, final TextView ultMensaje){
        this.ultimoMensaje = "default";
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chat");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    Mensaje msg = new Mensaje();
                    msg.setEmisor(ds.child("Emisor").getValue().toString());
                    msg.setReceptor(ds.child("Receptor").getValue().toString());
                    msg.setMensaje(ds.child("Mensaje").getValue().toString());
                        if (msg.getReceptor().equals(user.getUid()) && msg.getEmisor().equals(userid) ||
                                msg.getReceptor().equals(userid) && msg.getEmisor().equals(user.getUid())) {
                            ultimoMensaje = msg.getMensaje();
                        }

                }


                switch (ultimoMensaje){
                    case "default":
                        ultMensaje.setText("");
                        break;

                    default:
                        ultMensaje.setText(ultimoMensaje);
                        break;
                }

                ultimoMensaje = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

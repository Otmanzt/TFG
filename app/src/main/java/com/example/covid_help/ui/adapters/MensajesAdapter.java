package com.example.covid_help.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid_help.Mensajeria;
import com.example.covid_help.R;
import com.example.covid_help.ui.mensajes.Mensaje;
import com.example.covid_help.ui.mensajes.MensajeRecibir;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MensajesAdapter extends RecyclerView.Adapter<MensajesAdapter.HolderMensaje> {

    private List<Mensaje> listMensaje = new ArrayList<>();
    private FirebaseUser user;
    private Context c;

    public MensajesAdapter(List<Mensaje> listMensaje, Context c){
        this.c = c;
        this.listMensaje = listMensaje;
    }


    public MensajesAdapter(List<Mensaje> listMensaje){
        this.listMensaje = listMensaje;
    }

    public void addMensaje (Mensaje m){
        listMensaje.add(m);
        notifyItemInserted(listMensaje.size());
    }

    @NonNull
    @Override
    public HolderMensaje onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_mensajes, parent, false);
        return new HolderMensaje(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderMensaje holder, int position) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        // Receptor o emisor.
        RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) holder.cardView.getLayoutParams();
        FrameLayout.LayoutParams fl = (FrameLayout.LayoutParams) holder.mensajeBG.getLayoutParams();
        LinearLayout.LayoutParams llHora = (LinearLayout.LayoutParams) holder.hora.getLayoutParams();
        LinearLayout.LayoutParams llMensaje = (LinearLayout.LayoutParams) holder.mensaje.getLayoutParams();
        if (listMensaje.get(position).getEmisor().equals(user.getUid())){ //Emisor
            //Background por defecto
            holder.mensajeBG.setBackgroundColor(Color.parseColor("#e9e9e9"));
            rl.addRule(RelativeLayout.ALIGN_PARENT_LEFT,0);
            rl.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            llHora.gravity = Gravity.RIGHT;
            llMensaje.gravity = Gravity.RIGHT;
            fl.gravity = Gravity.RIGHT;
            holder.mensaje.setGravity(Gravity.RIGHT);
        }else if (listMensaje.get(position).getReceptor().equals(user.getUid())){ //Receptor
            holder.mensajeBG.setBackgroundColor(Color.TRANSPARENT);
            rl.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,0);
            rl.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            llHora.gravity = Gravity.LEFT;
            llMensaje.gravity = Gravity.LEFT;
            fl.gravity = Gravity.LEFT;
            holder.mensaje.setGravity(Gravity.LEFT);
        }

        holder.cardView.setLayoutParams(rl);
        holder.mensajeBG.setLayoutParams(fl);
        holder.hora.setLayoutParams(llHora);
        holder.mensaje.setLayoutParams(llMensaje);

        //holder.getNombre().setText(listMensaje.get(position).getNombre());
        holder.getMensaje().setText(listMensaje.get(position).getMensaje());

        Long codigoHora = Long.parseLong(listMensaje.get(position).getHora());
        Date d = new Date(codigoHora);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
        holder.getHora().setText(sdf.format(d));


        /*Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateTimeInstance().format(calendar.getTime());
        holder.getHora().setText(currentDate);*/
    }

    @Override
    public int getItemCount() {
        return listMensaje.size();
    }

    class HolderMensaje extends RecyclerView.ViewHolder {

        private CardView cardView;
        private LinearLayout mensajeBG;
        private TextView mensaje;
        private TextView hora;
        private CircleImageView fotoMensaje;

            public HolderMensaje(@NonNull View itemView) {
                super(itemView);
                // Set components
                cardView = itemView.findViewById(R.id.cardViewMensaje);
                mensajeBG = itemView.findViewById(R.id.mensajeBG);
                mensaje = itemView.findViewById(R.id.mensajeMensaje);
                hora = itemView.findViewById(R.id.horaMensaje);
                //fotoMensaje = itemView.findViewById(R.id.fotoPerfilMensaje);
            }

        public TextView getMensaje() {
            return mensaje;
        }

        public void setMensaje(TextView mensaje) {
            this.mensaje = mensaje;
        }

        public TextView getHora() {
            return hora;
        }

        public void setHora(TextView hora) {
            this.hora = hora;
        }

        public CircleImageView getFotoMensaje() {
            return fotoMensaje;
        }

        public void setFotoMensaje(CircleImageView fotoMensaje) {
            this.fotoMensaje = fotoMensaje;
        }
    }
}

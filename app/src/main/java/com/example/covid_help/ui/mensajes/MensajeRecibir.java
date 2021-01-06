package com.example.covid_help.ui.mensajes;

public class MensajeRecibir extends Mensaje {

    private String hora;

    public MensajeRecibir() {
    }

    public MensajeRecibir(String hora) {
        this.hora = hora;
    }

    /*public MensajeRecibir(String emisor, String receptor,String mensaje, Long hora) {
        super(emisor, receptor, mensaje);
        this.hora = hora;
    }*/

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}

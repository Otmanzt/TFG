package com.example.covid_help.ui.models;

public class Anuncio {

    private String id;
    private String titulo;
    private String descripcion;
    private String provincia;
    private String contacto;

    public Anuncio (){

    }

    public Anuncio (String id, String titulo, String descripcion, String provincia, String contacto){
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.provincia = provincia;
        this.contacto = contacto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }
}

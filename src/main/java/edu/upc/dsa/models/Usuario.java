package edu.upc.dsa.models;

import edu.upc.dsa.util.RandomUtils;

import java.util.ArrayList;
import java.util.List;

public class Usuario {

    String id;
    String name;
    String surname;
    String fecha;
    static int lastId;
    List<PuntoInteres> puntosInteres = new ArrayList<>();

    public Usuario() {
        this.setId(RandomUtils.getId());
    }
    public Usuario(String name, String surname, String fecha) {
        this(null, name, surname, fecha);
    }

    public Usuario(String id, String name, String surname, String fecha) {
        this();
        if (id != null) this.setId(id);
        this.setSurname(surname);
        this.setName(name);
        this.setFecha(fecha);
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id=id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setFecha (String Fecha){this.fecha = fecha;}
    public String getFecha (){return this.fecha;}

    public void addPuntosInteres(PuntoInteres p){
        puntosInteres.add(p);
    }
    public List<PuntoInteres> getPuntosInteres() {
        return puntosInteres;
    }

    @Override
    public String toString() {
        return "Track [id="+id+", name=" + name + ", surname=" + surname +"fecha"+ fecha+"]";
    }

}
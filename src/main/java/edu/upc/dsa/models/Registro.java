package edu.upc.dsa.models;

import edu.upc.dsa.models.ElementType;
import edu.upc.dsa.models.PuntoInteres;
import edu.upc.dsa.models.Usuario;

import java.util.*;

public class Registro {

    private Map<String, Usuario> usuarios;
    private Set<PuntoInteres> puntosInteres;
    private Map<String, List<PuntoInteres>> visitasUsuarios;
    private Map<PuntoInteres, List<String>> usuariosPorPunto;

    public Registro() {
        this.usuarios = new HashMap<>();
        this.puntosInteres = new HashSet<>();
        this.visitasUsuarios = new HashMap<>();
        this.usuariosPorPunto = new HashMap<>();
    }

    public void addUser(String id, String name, String surname, String fechaNacimiento) {
        Usuario usuario = new Usuario(id, name, surname, fechaNacimiento);
        usuarios.put(id, usuario);
        visitasUsuarios.put(id, new ArrayList<>());
    }

    public Usuario getUser(String id) {
        return usuarios.get(id);
    }

    public List<Usuario> getUsersOrderedAlphabetically() {
        List<Usuario> listaUsuarios = new ArrayList<>(usuarios.values());
        listaUsuarios.sort(Comparator.comparing(Usuario::getSurname).thenComparing(Usuario::getName));
        return listaUsuarios;
    }

    public void addPointOfInterest(int x, int y, ElementType type) {
        PuntoInteres punto = new PuntoInteres(x, y, type);
        puntosInteres.add(punto);
        usuariosPorPunto.put(punto, new ArrayList<>());
    }

    public boolean registerPointVisit(String userId, int x, int y) {
        Usuario usuario = usuarios.get(userId);
        PuntoInteres punto = getPuntoInteres(x, y);

        if (usuario != null && punto != null) {
            visitasUsuarios.get(userId).add(punto);
            usuariosPorPunto.get(punto).add(userId);
            return true;
        }
        else{
        return false;}
    }

    public List<PuntoInteres> getUserVisitedPoints(String userId) {
        return visitasUsuarios.getOrDefault(userId, new ArrayList<>());
    }

    public List<Usuario> getUsersByPoint(int x, int y) {
        PuntoInteres punto = getPuntoInteres(x, y);
        List<String> userIds = usuariosPorPunto.getOrDefault(punto, new ArrayList<>());
        List<Usuario> usuariosQuePasaron = new ArrayList<>();
        for (String id : userIds) {
            usuariosQuePasaron.add(usuarios.get(id));
        }
        return usuariosQuePasaron;
    }

    public List<PuntoInteres> getPointsByType(ElementType type) {
        List<PuntoInteres> puntosDelTipo = new ArrayList<>();
        for (PuntoInteres punto : puntosInteres) {
            if (punto.getType() == type) {
                puntosDelTipo.add(punto);
            }
        }
        return puntosDelTipo;
    }

    private PuntoInteres getPuntoInteres(int x, int y) {
        for (PuntoInteres punto : puntosInteres) {
            if (punto.getX() == x && punto.getY() == y) {
                return punto;
            }
        }
        return null;
    }
}

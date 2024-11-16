package edu.upc.dsa;

import edu.upc.dsa.models.ElementType;
import edu.upc.dsa.models.EmptyPointListException;
import edu.upc.dsa.models.PuntoInteres;
import edu.upc.dsa.models.Usuario;

import java.util.*;

public class GameManagerImpl implements GameManager {
    private static GameManager instance;
    private Map<String, Usuario> usuarios; // Mapa de usuarios por ID
    private Set<PuntoInteres> puntosInteres; // Conjunto de puntos de interés
    private Map<String, List<PuntoInteres>> visitasUsuarios; // Historial de visitas de usuarios
    private Map<PuntoInteres, List<String>> usuariosPorPunto; // Usuarios que han pasado por cada punto

    private GameManagerImpl() {
        this.usuarios = new HashMap<>();
        this.puntosInteres = new HashSet<>();
        this.visitasUsuarios = new HashMap<>();
        this.usuariosPorPunto = new HashMap<>();
    }
    public static GameManager getInstance() {
        if (instance==null) instance = new GameManagerImpl();
        return instance;
    }
    @Override
    public void addUser(String id, String name, String surname, String fechaNacimiento) {
        Usuario usuario = new Usuario(id, name, surname, fechaNacimiento);
        usuarios.put(id, usuario);
        visitasUsuarios.put(id, new ArrayList<>()); // Inicializar lista de visitas
    }

    @Override
    public List<Usuario> getUsersOrderedAlphabetically() {
        List<Usuario> listaUsuarios = new ArrayList<>(usuarios.values());
        listaUsuarios.sort(Comparator.comparing(Usuario::getSurname).thenComparing(Usuario::getName));
        return listaUsuarios;
    }

    @Override
    public Usuario getUser(String id) {
        return usuarios.get(id);
    }

    @Override
    public void addPointOfInterest(int x, int y, ElementType type) {
        PuntoInteres punto = new PuntoInteres(x, y, type);
        puntosInteres.add(punto);
        usuariosPorPunto.put(punto, new ArrayList<>()); // Inicializar lista de usuarios para este punto
    }

    @Override
    public boolean registerPointVisit(String userId, int x, int y) throws EmptyPointListException {
        Usuario usuario = usuarios.get(userId);
        PuntoInteres punto = getPuntoInteres(x, y);

        if (usuario == null) {
            throw new EmptyPointListException("Usuario no encontrado.");
        }
        if (punto == null) {
            throw new EmptyPointListException("Punto de interés no encontrado.");
        }
        else{
            visitasUsuarios.get(userId).add(punto); // Registrar la visita del usuario al punto
            usuariosPorPunto.get(punto).add(userId);
            return true;
        }// Registrar que el usuario ha pasado por este punto
    }

    @Override
    public List<PuntoInteres> getUserVisitedPoints(String userId) {
        return visitasUsuarios.getOrDefault(userId, new ArrayList<>());
    }

    @Override
    public List<Usuario> getUsersByPoint(int x, int y) throws EmptyPointListException {
        PuntoInteres punto = getPuntoInteres(x, y);
        if (punto == null) {
            throw new EmptyPointListException("Punto de interés no encontrado en esas coordenadas.");
        }
        List<String> userIds = usuariosPorPunto.getOrDefault(punto, new ArrayList<>());
        List<Usuario> usuariosQuePasaron = new ArrayList<>();
        for (String id : userIds) {
            usuariosQuePasaron.add(usuarios.get(id));
        }
        return usuariosQuePasaron;
    }

    @Override
    public List<PuntoInteres> getPointsByType(ElementType type) {
        List<PuntoInteres> puntosDelTipo = new ArrayList<>();
        for (PuntoInteres punto : puntosInteres) {
            if (punto.getType() == type) {
                puntosDelTipo.add(punto);
            }
        }
        return puntosDelTipo;
    }

    @Override
    public void clear() {
        usuarios.clear();
        puntosInteres.clear();
        visitasUsuarios.clear();
        usuariosPorPunto.clear();
    }

    @Override
    public int size() {
        return usuarios.size();
    }

    // Metodo auxiliar para encontrar un PuntoInteres por coordenadas
    private PuntoInteres getPuntoInteres(int x, int y) {
        for (PuntoInteres punto : puntosInteres) {
            if (punto.getX() == x && punto.getY() == y) {
                return punto;
            }
        }
        return null;
    }
}

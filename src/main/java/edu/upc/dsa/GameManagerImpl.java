package edu.upc.dsa;

import edu.upc.dsa.models.ElementType;
import edu.upc.dsa.models.EmptyPointListException;
import edu.upc.dsa.models.PuntoInteres;
import edu.upc.dsa.models.Usuario;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.*;

public class GameManagerImpl implements GameManager {
    private static final Logger logger = LogManager.getLogger(GameManagerImpl.class);

    private static GameManager instance;
    private Map<String, Usuario> usuarios; // Mapa de usuarios por ID
    private Set<PuntoInteres> puntosInteres; // Conjunto de puntos de interés
    private Map<String, List<PuntoInteres>> visitasUsuarios; // Historial de visitas de usuarios
    private Map<PuntoInteres, List<String>> usuariosPorPunto; // Usuarios que han pasado por cada punto

    // Constructor privado para implementar el patrón Singleton
    private GameManagerImpl() {
        this.usuarios = new HashMap<>();
        this.puntosInteres = new HashSet<>();
        this.visitasUsuarios = new HashMap<>();
        this.usuariosPorPunto = new HashMap<>();
    }

    // Método para obtener la única instancia de GameManagerImpl
    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManagerImpl();
        }
        return instance;
    }

    @Override
    public void addUser(String id, String name, String surname, String fechaNacimiento) {
        logger.info("addUser - Ingreso: id="+id+", name="+name+", surname="+ surname +", fechaNacimiento="+ fechaNacimiento);
        try {
            Usuario usuario = new Usuario(id, name, surname, fechaNacimiento);
            usuarios.put(id, usuario);
            visitasUsuarios.put(id, new ArrayList<>()); // Inicializa la lista de visitas
            logger.info("addUser - Usuario agregado con éxito: "+ id);
        } catch (Exception e) {
            logger.error("addUser - Error al agregar usuario:"+ e.getMessage());
        }
    }

    @Override
    public List<Usuario> getUsersOrderedAlphabetically() {
        logger.info("getUsersOrderedAlphabetically - Ingreso");
        List<Usuario> listaUsuarios = new ArrayList<>(usuarios.values());
        listaUsuarios.sort(Comparator.comparing(Usuario::getSurname).thenComparing(Usuario::getName));
        logger.info("getUsersOrderedAlphabetically - Resultado:"+ listaUsuarios.size());
        return listaUsuarios;
    }

    @Override
    public Usuario getUser(String id) {
        logger.info("getUser - Ingreso: id="+ id);
        Usuario usuario = usuarios.get(id);
        if (usuario == null) {
            logger.error("getUser - Error: Usuario con id=" +id+"no encontrado");
        } else {
            logger.info("getUser - Usuario encontrado: "+usuario.getId());
        }
        return usuario;
    }

    @Override
    public void addPointOfInterest(int x, int y, ElementType type) {
        logger.info("addPointOfInterest - Ingreso: x="+x+", y="+y+", type="+type);
        try {
            PuntoInteres punto = new PuntoInteres(x, y, type);
            puntosInteres.add(punto);
            usuariosPorPunto.put(punto, new ArrayList<>()); // Inicializa la lista de usuarios para este punto
            logger.info("addPointOfInterest - Punto de interés agregado: ("+x+","+y+")");
        } catch (Exception e) {
            logger.error("addPointOfInterest - Error al agregar punto de interés: "+ e.getMessage());
        }
    }

    @Override
    public boolean registerPointVisit(String userId, int x, int y) throws EmptyPointListException {
        logger.info("registerPointVisit - Ingreso: userId="+userId+", x="+x+", y="+y);
        Usuario usuario = usuarios.get(userId);
        PuntoInteres punto = getPuntoInteres(x, y);

        if (usuario == null) {
            logger.error("registerPointVisit - Error: Usuario no encontrado con ID"+ userId);
            throw new EmptyPointListException("Usuario no encontrado.");
        }
        if (punto == null) {
            logger.error("registerPointVisit - Error: Punto de interés no encontrado en ("+x+", "+y+")");
            throw new EmptyPointListException("Punto de interés no encontrado.");
        } else {
            visitasUsuarios.get(userId).add(punto); // Registrar la visita del usuario al punto
            usuariosPorPunto.get(punto).add(userId);
            logger.info("registerPointVisit - Visita registrada: usuario+"+ userId+ " al punto ("+x+", "+y+")");
            return true;
        }
    }

    @Override
    public List<PuntoInteres> getUserVisitedPoints(String userId) {
        logger.info("getUserVisitedPoints - Ingreso: userId="+ userId);
        List<PuntoInteres> visitedPoints = visitasUsuarios.getOrDefault(userId, new ArrayList<>());
        logger.info("getUserVisitedPoints - Resultado: "+visitedPoints.size()+" puntos visitados");
        return visitedPoints;
    }

    @Override
    public List<Usuario> getUsersByPoint(int x, int y) throws EmptyPointListException {
        logger.info("getUsersByPoint - Ingreso: x="+x+", y="+y);
        PuntoInteres punto = getPuntoInteres(x, y);
        if (punto == null) {
            logger.error("getUsersByPoint - Error: Punto de interés no encontrado en ("+x+", "+y+")");
            throw new EmptyPointListException("Punto de interés no encontrado en esas coordenadas.");
        }
        List<String> userIds = usuariosPorPunto.getOrDefault(punto, new ArrayList<>());
        List<Usuario> usuariosQuePasaron = new ArrayList<>();
        for (String id : userIds) {
            usuariosQuePasaron.add(usuarios.get(id));
        }
        logger.info("getUsersByPoint - Resultado: "+usuariosQuePasaron+" usuarios que han visitado el punto ("+x+", "+y+")");
        return usuariosQuePasaron;
    }

    @Override
    public List<PuntoInteres> getPointsByType(ElementType type) {
        logger.info("getPointsByType - Ingreso: type="+ type);
        List<PuntoInteres> puntosDelTipo = new ArrayList<>();
        try {
            for (PuntoInteres punto : puntosInteres) {
                if (punto.getType() == type) {
                    puntosDelTipo.add(punto);
                }
            }
            logger.info("getPointsByType - Resultado: "+puntosDelTipo.size()+" puntos encontrados");
        } catch (Exception e) {
            logger.error("getPointsByType - Error al obtener puntos:"+ e.getMessage());
        }
        return puntosDelTipo;
    }

    @Override
    public void clear() {
        logger.info("clear - Ingreso");
        usuarios.clear();
        puntosInteres.clear();
        visitasUsuarios.clear();
        usuariosPorPunto.clear();
        logger.info("clear - Datos limpiados");
    }

    @Override
    public int size() {
        logger.info("size - Ingreso");
        return usuarios.size();
    }

    // Método auxiliar para encontrar un PuntoInteres por coordenadas
    private PuntoInteres getPuntoInteres(int x, int y) {
        for (PuntoInteres punto : puntosInteres) {
            if (punto.getX() == x && punto.getY() == y) {
                return punto;
            }
        }
        return null;
    }
}

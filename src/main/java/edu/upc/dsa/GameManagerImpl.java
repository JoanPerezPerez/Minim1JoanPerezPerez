package edu.upc.dsa;

import edu.upc.dsa.models.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.List;

public class GameManagerImpl implements GameManager {

    private static final Logger logger = LogManager.getLogger(GameManagerImpl.class);
    private static GameManager instance;
    private Registro registro;

    private GameManagerImpl() {
        this.registro = new Registro();
    }

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManagerImpl();
        }
        return instance;
    }

    @Override
    public void addUser(String id, String name, String surname, String fechaNacimiento) {
        logger.info("addUser - Ingreso: id=" + id + ", name=" + name + ", surname=" + surname + ", fechaNacimiento=" + fechaNacimiento);
        try {
            registro.addUser(id, name, surname, fechaNacimiento);
            logger.info("addUser - Usuario agregado con éxito: " + id);
        } catch (Exception e) {
            logger.error("addUser - Error al agregar usuario: " + e.getMessage());
        }
    }

    @Override
    public List<Usuario> getUsersOrderedAlphabetically() {
        logger.info("getUsersOrderedAlphabetically - Ingreso");
        return registro.getUsersOrderedAlphabetically();
    }

    @Override
    public Usuario getUser(String id) {
        logger.info("getUser - Ingreso: id=" + id);
        Usuario usuario = registro.getUser(id);
        if (usuario == null) {
            logger.error("getUser - Error: Usuario con id=" + id + " no encontrado");
        } else {
            logger.info("getUser - Usuario encontrado: " + usuario.getId());
        }
        return usuario;
    }

    @Override
    public void addPointOfInterest(int x, int y, ElementType type) {
        logger.info("addPointOfInterest - Ingreso: x=" + x + ", y=" + y + ", type=" + type);
        try {
            registro.addPointOfInterest(x, y, type);
            logger.info("addPointOfInterest - Punto de interés agregado: (" + x + "," + y + ")");
        } catch (Exception e) {
            logger.error("addPointOfInterest - Error al agregar punto de interés: " + e.getMessage());
        }
    }

    @Override
    public boolean registerPointVisit(String userId, int x, int y) throws EmptyPointListException {
        logger.info("registerPointVisit - Ingreso: userId=" + userId + ", x=" + x + ", y=" + y);
        if (registro.registerPointVisit(userId, x, y) == false){
            throw new EmptyPointListException("Registro vacio");
        }
        return registro.registerPointVisit(userId, x, y);
    }

    @Override
    public List<PuntoInteres> getUserVisitedPoints(String userId) {
        logger.info("getUserVisitedPoints - Ingreso: userId=" + userId);
        return registro.getUserVisitedPoints(userId);
    }

    @Override
    public List<Usuario> getUsersByPoint(int x, int y) throws EmptyPointListException {
        logger.info("getUsersByPoint - Ingreso: x=" + x + ", y=" + y);
        if (registro.getUsersByPoint(x, y) == null){
            throw new EmptyPointListException("No hay usuarios en ese punto.");
        }
        return registro.getUsersByPoint(x, y);
    }

    @Override
    public List<PuntoInteres> getPointsByType(ElementType type) {
        logger.info("getPointsByType - Ingreso: type=" + type);
        return registro.getPointsByType(type);
    }

    @Override
    public void clear() {
        logger.info("clear - Ingreso");
        registro = new Registro(); // Reinicia el registro
        logger.info("clear - Datos limpiados");
    }

    @Override
    public int size() {
        logger.info("size - Ingreso");
        return registro.getUsersOrderedAlphabetically().size();
    }
}


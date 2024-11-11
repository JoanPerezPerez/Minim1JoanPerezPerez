package edu.upc.dsa;

import edu.upc.dsa.models.ElementType;
import edu.upc.dsa.models.EmptyPointListException;
import edu.upc.dsa.models.PuntoInteres;
import edu.upc.dsa.models.Usuario;

import java.util.List;

public interface GameManager {

    void addUser(String id, String name, String surname, String fechaNacimiento);
    List<Usuario> getUsersOrderedAlphabetically();
    Usuario getUser(String id);
    void addPointOfInterest(int x, int y, ElementType type);
    void registerPointVisit(String userId, int x, int y) throws EmptyPointListException;
    List<PuntoInteres> getUserVisitedPoints(String userId);
    List<Usuario> getUsersByPoint(int x, int y) throws EmptyPointListException;
    List<PuntoInteres> getPointsByType(ElementType type);

    void clear();
    int size();
}


package edu.upc.dsa.models;
import java.util.*;

public class Registro {
    private Set<PuntoInteres> puntosInteres; // Conjunto de puntos de interés registrados
    private Set<Integer> usuarios; // Conjunto de IDs de usuarios registrados
    private Map<Integer, List<PuntoInteres>> historialVisitas;// Historial de visitas de cada usuario
    private Map<String, List<Integer>> usuariosPorPunto;

    // Constructor que inicializa los conjuntos de usuarios y puntos de interés
    public Registro() {
        this.puntosInteres = new HashSet<>();
        this.usuarios = new HashSet<>();
        this.historialVisitas = new HashMap<>();
        this.usuariosPorPunto = new HashMap<>();
    }

    // Metodo para registrar un nuevo usuario por su ID
    public void registrarUsuario(int userId) {
        usuarios.add(userId);
        historialVisitas.put(userId, new ArrayList<>()); // Inicializa el historial de visitas
    }

    // Metodo para añadir un punto de interés al conjunto
    public void agregarPuntoInteres(PuntoInteres punto) {
        puntosInteres.add(punto);
    }

    // Metodo para registrar el paso de un usuario por un punto de interés
    public String registrarPaso(int userId, int x, int y) {
        // Verificar si el usuario está registrado
        if (!usuarios.contains(userId)) {
            return "Error: Usuario con ID " + userId + " no está registrado.";
        }

        // Verificar si el punto de interés existe en esas coordenadas
        PuntoInteres punto = buscarPuntoInteres(x, y);
        if (punto == null) {
            return "Error: No existe un punto de interés en las coordenadas (" + x + ", " + y + ").";
        }

        // Registrar el paso en el historial del usuario
        historialVisitas.get(userId).add(punto);
        return "Usuario con ID " + userId + " ha pasado por el punto de interés en (" + x + ", " + y + ").";
    }

    // Metodo para consultar los puntos de interés visitados por un usuario en el orden registrado
    public List<PuntoInteres> consultarHistorial(int userId) throws EmptyPointListException {
        if (!usuarios.contains(userId)) {
            throw new EmptyPointListException("Empty product list");
        }
        return historialVisitas.get(userId);
    }

    public List<Integer> consultarUsuariosPorPunto(int x, int y) {
        String coordenadas = x + "," + y;

        // Verificar si el punto de interés existe
        if (!usuariosPorPunto.containsKey(coordenadas)) {
            System.out.println("Error: No existe un punto de interés en las coordenadas (" + x + ", " + y + ").");
            return Collections.emptyList();
        }

        return usuariosPorPunto.get(coordenadas);
    }
    // Metodo privado para buscar un punto de interés por coordenadas
    private PuntoInteres buscarPuntoInteres(int x, int y) {
        for (PuntoInteres punto : puntosInteres) {
            if (punto.getX() == x && punto.getY() == y) {
                return punto;
            }
        }
        return null;
    }
    public List<PuntoInteres> consultarPuntosPorTipo(ElementType tipo) {
        List<PuntoInteres> puntosDelTipo = new ArrayList<>();
        for (PuntoInteres punto : puntosInteres) {
            if (punto.getType() == tipo) {
                puntosDelTipo.add(punto);
            }
        }
        return puntosDelTipo;
    }
}

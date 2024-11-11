package edu.upc.dsa.models;


public class PuntoInteres {
    private int x; // Coordenada horizontal
    private int y; // Coordenada vertical
    private ElementType type; // Tipo de punto de interés

    // Constructor
    public PuntoInteres(int x, int y, ElementType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    // Getters y Setters
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public ElementType getType() {
        return type;
    }

    public void setType(ElementType type) {
        this.type = type;
    }


    @Override
    public String toString() {
        return "Puntos de interes{" +
                "x=" + x +
                ", y=" + y +
                ", type=" + type +
                '}';
    }
}

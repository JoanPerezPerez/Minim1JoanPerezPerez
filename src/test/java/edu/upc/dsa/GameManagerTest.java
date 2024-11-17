package edu.upc.dsa;

import edu.upc.dsa.exceptions.TrackNotFoundException;
import edu.upc.dsa.models.ElementType;
import edu.upc.dsa.models.EmptyPointListException;
import edu.upc.dsa.models.PuntoInteres;
import edu.upc.dsa.models.Usuario;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class GameManagerTest {
    GameManager gm;


    @Before
    public void setUp() throws EmptyPointListException {
        this.gm = GameManagerImpl.getInstance();
        this.gm.addUser("s123","Manolo","Lopez","17/12/2023");
        this.gm.addUser("s124","Joan","Lopez","17/12/2023");
        this.gm.addUser("s125","Alex","Lopez","17/12/2023");
        this.gm.addPointOfInterest(22,25, ElementType.DOOR);
        this.gm.addPointOfInterest(15,33, ElementType.GRASS);
        this.gm.addPointOfInterest(22,25, ElementType.BRIDGE);



        this.gm.registerPointVisit("s123", 22, 25);
    }

    @After
    public void tearDown() {
        // És un Singleton
        this.gm.clear();
    }

    @Test
    public void ordenarAlfabtico() {
        Assert.assertEquals(gm.getUsersOrderedAlphabetically().get(0).getName(),"Alex" );
        Assert.assertEquals(gm.getUsersOrderedAlphabetically().get(1).getName(),"Joan" );
        Assert.assertEquals(gm.getUsersOrderedAlphabetically().get(2).getName(),"Manolo" );

    }
    @Test
    public void consultarInfoUser(){
        Assert.assertEquals(gm.getUser("s123").getName(),"Manolo" );
        Assert.assertEquals(gm.getUser("s124").getName(),"Joan" );
        Assert.assertEquals(gm.getUser("s125").getName(),"Alex" );
    }
    @Test
    public void registrarPunto(){

        Assert.assertThrows(EmptyPointListException.class, () ->
                this.gm.registerPointVisit("s128", 22, 25));
        Assert.assertThrows(EmptyPointListException.class, () ->
                this.gm.registerPointVisit("s123", 23, 25));
        Assert.assertThrows(EmptyPointListException.class, () ->
                this.gm.registerPointVisit("s123", 24, 26));
    }
    @Test
    public void puntosInteresVisitados() {
        // Primero, asegurarse de que el usuario ha visitado el punto 22, 25
        List<PuntoInteres> puntosVisitados = gm.getUserVisitedPoints("s123");

        // Asegurarse de que el usuario ha visitado el punto de interés correcto
        Assert.assertEquals(2, puntosVisitados.size()); // Debería haber solo un punto visitado
        Assert.assertEquals(22, puntosVisitados.get(0).getX());
        Assert.assertEquals(25, puntosVisitados.get(0).getY());
        Assert.assertEquals(ElementType.BRIDGE, puntosVisitados.get(0).getType());
    }
    @Test
    public void getUserPoints() throws EmptyPointListException {
        Assert.assertEquals(gm.getUsersByPoint(22,25).get(0).getName(),"Manolo" );
    }
    @Test
    public void puntosInteresElementType() {
        this.gm.addPointOfInterest(0, 0, ElementType.GRASS);
        this.gm.addPointOfInterest(1, 1, ElementType.DOOR);
        this.gm.addPointOfInterest(2, 2, ElementType.POTION);
        this.gm.addPointOfInterest(3, 3, ElementType.COIN);

        // Invocar el método para obtener puntos del tipo GRASS
        List<PuntoInteres> puntosGrass = gm.getPointsByType(ElementType.GRASS);

        List<PuntoInteres> puntosBridge = gm.getPointsByType(ElementType.BRIDGE);
        // Verificar que la lista no sea nula
        Assert.assertNotNull("La lista de puntos tipo GRASS no debería ser nula", puntosGrass);
        Assert.assertNotNull("La lista de puntos tipo GRASS no debería ser nula", puntosBridge);
        // Verificar el tamaño de la lista
        Assert.assertEquals("El número de puntos tipo GRASS no es el esperado", 2, puntosGrass.size());
        Assert.assertEquals("El número de puntos tipo GRASS no es el esperado", 1, puntosBridge.size());
        // Verificar que todos los puntos sean del tipo GRASS
        for (PuntoInteres punto : puntosGrass) {
            Assert.assertEquals("El tipo del punto no es GRASS", ElementType.GRASS, punto.getType());
        }
        for (PuntoInteres punto : puntosBridge) {
            Assert.assertEquals("El tipo del punto no es Bridge", ElementType.BRIDGE, punto.getType());
        }
    }
}

package edu.upc.dsa.services;

import edu.upc.dsa.GameManager;
import edu.upc.dsa.GameManagerImpl;
import edu.upc.dsa.models.ElementType;
import edu.upc.dsa.models.EmptyPointListException;
import edu.upc.dsa.models.PuntoInteres;
import edu.upc.dsa.models.Usuario;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Api(value = "/game", description = "Endpoint to Game Service")
@Path("/game")
public class GameService {

    private GameManager gm;

    public GameService() {
        this.gm = GameManagerImpl.getInstance();
        if (gm.size() == 0) {
            this.gm.addUser("s123", "Manolo", "Lopez", "17/12/2023");
            this.gm.addUser("s124", "Joan", "Lopez", "17/12/2023");
            this.gm.addUser("s125", "Alex", "Lopez", "17/12/2023");
            this.gm.addPointOfInterest(0, 0, ElementType.GRASS);
            this.gm.addPointOfInterest(1, 1, ElementType.DOOR);
            this.gm.addPointOfInterest(2, 2, ElementType.POTION);
            this.gm.addPointOfInterest(3, 3, ElementType.COIN);
        }
    }

    @GET
    @ApiOperation(value = "Obtener todos los usuarios por orden alfabético", notes = "Retorna la lista de usuarios ordenada por nombre")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Correcto", response = Usuario.class, responseContainer = "Lista"),
    })
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers() {
        List<Usuario> usuarios = this.gm.getUsersOrderedAlphabetically();
        GenericEntity<List<Usuario>> entity = new GenericEntity<List<Usuario>>(usuarios) {};
        return Response.status(200).entity(entity).build();
    }

    @GET
    @ApiOperation(value = "Obtener un usuario específico", notes = "Devuelve detalles de un usuario a partir del ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Correcto", response = Usuario.class),
            @ApiResponse(code = 404, message = "Usuario no encontrado")
    })
    @Path("/users/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("id") String id) {
        Usuario user = this.gm.getUser(id);
        if (user == null) {
            return Response.status(404).entity("Usuario no encontrado").build();
        }
        return Response.status(200).entity(user).build();
    }

    @POST
    @ApiOperation(value = "Añadir un nuevo usuario", notes = "Crear un nuevo usuario con sus detalles específicos")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Usuario creado de forma correcta"),
            @ApiResponse(code = 400, message = "Parametros invalidos")
    })
    @Path("/users")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addUser(Usuario user) {
        try {
            this.gm.addUser(user.getId(), user.getName(), user.getSurname(), user.getFecha());
            return Response.status(201).entity("Usuario creado de forma correcta").build();
        } catch (Exception e) {
            return Response.status(400).entity("Parametros invalidos").build();
        }
    }

    @POST
    @ApiOperation(value = "Añadir punto de interés", notes = "Crea un nuevo punto de interés en unas coordenadas específicas")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Punto de interés agregado de forma correcta"),
            @ApiResponse(code = 400, message = "Parámetros inválidos"),
            @ApiResponse(code = 500, message = "Error interno del servidor")
    })
    @Path("/points")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPoint(PuntoInteres point) {
        try {
            if (point == null || point.getType() == null) {
                return Response.status(400).entity("Faltan parámetros o el tipo es inválido").build();
            }

            this.gm.addPointOfInterest(point.getX(), point.getY(), point.getType());
            return Response.status(201).entity("Punto de interés agregado de forma correcta").build();
        } catch (IllegalArgumentException e) {
            return Response.status(400).entity("Parámetros inválidos: " + e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(500).entity("Error interno del servidor").build();
        }
    }


    @GET
    @ApiOperation(value = "Obtener puntos por tipo", notes = "Retorna todos los puntos de interés para un tipo específico")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Correcto", response = PuntoInteres.class, responseContainer = "Lista"),
            @ApiResponse(code = 404, message = "No se han encontrado puntos")
    })
    @Path("/points/{type}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPointsByType(@PathParam("type") ElementType type) {
        List<PuntoInteres> points = this.gm.getPointsByType(type);
        if (points == null || points.isEmpty()) {
            return Response.status(404).entity("No se han encontrado puntos").build();
        }
        GenericEntity<List<PuntoInteres>> entity = new GenericEntity<List<PuntoInteres>>(points) {};
        return Response.status(200).entity(entity).build();
    }

    @GET
    @ApiOperation(value = "get Puntos Pasados User", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = PuntoInteres.class,responseContainer="List"),
            @ApiResponse(code = 404, message = "User Not Found")
    })
    @Path("/puntosInteres/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPuntosUsuario(@PathParam("id") String id) throws EmptyPointListException {
        List<PuntoInteres> puntos;
        puntos = gm.getUserVisitedPoints(id);
        if (puntos == null){
            throw new EmptyPointListException("No hay puntos para este usuario");
        }
        else{



        GenericEntity<List<PuntoInteres>> entity = new GenericEntity<List<PuntoInteres>>(puntos) {
        };
        return Response.status(201).entity(entity).build();}
    }
    @PUT
    @ApiOperation(value = "Registrar usuario a un punto de interés", notes = "Registrar que un usuario ha pasado por un punto de interés")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Visira registrada de forma correcta", response = PuntoInteres.class),
            @ApiResponse(code = 404, message = "Usuario o punto de interés no encontrado"),
            @ApiResponse(code = 500, message = "Punto No Existente")
    })
    @Path("/users/{id}/points/{x}/{y}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerUserVisit(@PathParam("id") String userId, @PathParam("x") int x, @PathParam("y") int y) {
        try {
            boolean success = this.gm.registerPointVisit(userId,x,y);
            if (!success) {
                return Response.status(404).entity("Usuario o punto de interés no encontrado").build();
            }
            return Response.status(201).entity("Visita registrada de forma corracta").build();
        } catch (Exception e) {
            return Response.status(500).entity("Error registrando la visita").build();
        } catch (EmptyPointListException e) {
            throw new RuntimeException(e);
        }
    }
    @GET
    @ApiOperation(value = "Ver usuarios que han visitado un punto de interés", notes = "Lista de usuarios que han pasado por un punto de interés")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Correcto", response = Usuario.class, responseContainer = "Lista"),
            @ApiResponse(code = 404, message = "Punto de interés no encontrado")
    })
    @Path("/points/{x}/{y}/users")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsersByPoint(@PathParam("x") int x, @PathParam("y") int y) throws EmptyPointListException {
        List<Usuario> users = this.gm.getUsersByPoint(x, y);
        if (users == null || users.isEmpty()) {
            return Response.status(404).entity("No hay usuarios en este punto").build();
        }
        GenericEntity<List<Usuario>> entity = new GenericEntity<List<Usuario>>(users) {};
        return Response.status(200).entity(entity).build();
    }
}

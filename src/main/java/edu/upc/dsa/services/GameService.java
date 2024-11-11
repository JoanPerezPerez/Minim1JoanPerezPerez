package edu.upc.dsa.services;


import edu.upc.dsa.GameManager;
import edu.upc.dsa.GameManagerImpl;
import edu.upc.dsa.models.ElementType;
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
        if (gm.size()==0) {
            this.gm.addUser("s123", "Manolo", "Lopez", "17/12/2023");
            this.gm.addUser("s124", "Joan", "Lopez", "17/12/2023");
            this.gm.addUser("s125", "Alex", "Lopez", "17/12/2023");
        }
    }

    @GET
    @ApiOperation(value = "Listado Usuarios", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Usuario.class, responseContainer="List"),
    })
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTracks() {

        List<Usuario> usuarios = this.gm.getUsersOrderedAlphabetically();

        GenericEntity<List<Usuario>> entity = new GenericEntity<List<Usuario>>(usuarios) {};
        return Response.status(201).entity(entity).build()  ;

    }

    @GET
    @ApiOperation(value = "get a User", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Usuario.class),
            @ApiResponse(code = 404, message = "User not found")
    })
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("id") String id) {
        Usuario t = this.gm.getUser(id);
        if (t == null) return Response.status(404).build();
        else  return Response.status(201).entity(t).build();
    }

    @Path("/{ElementType}/{x}/{y}")
    public Response addPointOfInterest(@PathParam("ElementType") ElementType type, @PathParam("x") int x, @PathParam("y") int y) {
        try {
            this.gm.addPointOfInterest(x, y, type); // Añadir punto de interés
            return Response.status(201).entity("Point of Interest added successfully").build();  // Respuesta de éxito
        } catch (Exception e) {
            // En caso de error, puedes retornar un código 400 (Bad Request)
            return Response.status(400).entity("Invalid parameters or error adding Point of Interest").build();
        }
    }


}
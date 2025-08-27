package org.uma_gym.web_event_booker.controller;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.uma_gym.web_event_booker.controller.dto.LoginRequestDTO;
import org.uma_gym.web_event_booker.controller.dto.UserResponseDTO;
import org.uma_gym.web_event_booker.model.User;
import org.uma_gym.web_event_booker.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Path("/users")
public class UserController {

    @Inject
    private UserService userService;

    @GET
    @Secured
    @RolesAllowed({"ADMIN"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<UserResponseDTO> users = userService.getAllUsers().stream()
                .map(UserResponseDTO::new)
                .collect(Collectors.toList());
        return Response.ok(users).build();
    }

    @POST
    @Secured // Mora biti ulogovan...
    @RolesAllowed({"ADMIN"}) // ...i mora biti ADMIN
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(@Valid User user) {
        try {
            User newUser = userService.createUser(user);
            return Response.status(Response.Status.CREATED).entity(new UserResponseDTO(newUser)).build();
        } catch (IllegalArgumentException e) {
            // Vraćamo 409 Conflict ako korisnik već postoji
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginRequestDTO loginRequest) {
        return userService.login(loginRequest.getEmail(), loginRequest.getPassword())
                .map(token -> Response.ok().entity("{\"token\": \"" + token + "\"}").build())
                .orElse(Response.status(Response.Status.UNAUTHORIZED).entity("Neispravan email ili lozinka.").build());
    }
}
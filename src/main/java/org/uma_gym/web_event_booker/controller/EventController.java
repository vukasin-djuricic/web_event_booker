package org.uma_gym.web_event_booker.controller;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.uma_gym.web_event_booker.controller.dto.EventCreateDTO;
import org.uma_gym.web_event_booker.controller.dto.EventResponseDTO;
import org.uma_gym.web_event_booker.model.Event;
import org.uma_gym.web_event_booker.service.EventService;
import java.util.List;
import java.util.stream.Collectors;

@Path("/events")
public class EventController {

    @Inject
    private EventService eventService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<EventResponseDTO> events = eventService.getAllEvents().stream()
                .map(EventResponseDTO::new)
                .collect(Collectors.toList());
        return Response.ok(events).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") Long id) {
        return eventService.getEventById(id)
                .map(event -> Response.ok(new EventResponseDTO(event)).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(@Valid EventCreateDTO eventDTO) {
        try {
            Event newEvent = eventService.createEvent(eventDTO);
            return Response.status(Response.Status.CREATED).entity(new EventResponseDTO(newEvent)).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    // --- NOVI ENDPOINT ZA UPDATE ---
    @PUT
    @Path("/{id}")
    @Secured // Zaštiti endpoint
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(
            @PathParam("id") Long id,
            @Valid EventCreateDTO eventDTO,
            @HeaderParam("Authorization") String authorizationHeader // Preuzmi token
    ) {
        try {
            // Ukloni "Bearer " prefiks iz tokena
            String token = authorizationHeader.substring("Bearer ".length()).trim();
            Event updatedEvent = eventService.updateEvent(id, eventDTO, token);
            return Response.ok(new EventResponseDTO(updatedEvent)).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (ForbiddenException e) {
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
        }
    }

    // --- NOVI ENDPOINT ZA DELETE ---
    @DELETE
    @Path("/{id}")
    @Secured // Zaštiti endpoint
    public Response delete(
            @PathParam("id") Long id,
            @HeaderParam("Authorization") String authorizationHeader // Preuzmi token
    ) {
        try {
            String token = authorizationHeader.substring("Bearer ".length()).trim();
            eventService.deleteEvent(id, token);
            return Response.noContent().build(); // Status 204 No Content
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (ForbiddenException e) {
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
        }
    }

    // --- NOVI ENDPOINT ZA PRETRAGU ---
    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchEvents(@QueryParam("query") String query) {
        if (query == null || query.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Query parameter 'query' is required.\"}")
                    .build();
        }

        List<EventResponseDTO> foundEvents = eventService.searchEvents(query).stream()
                .map(EventResponseDTO::new)
                .collect(Collectors.toList());

        return Response.ok(foundEvents).build();
    }
}

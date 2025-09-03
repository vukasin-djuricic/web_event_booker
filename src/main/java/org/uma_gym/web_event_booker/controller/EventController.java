package org.uma_gym.web_event_booker.controller;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.uma_gym.web_event_booker.controller.dto.EventCreateDTO;
import org.uma_gym.web_event_booker.controller.dto.EventResponseDTO;
import org.uma_gym.web_event_booker.controller.dto.PagedResult;
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
    public Response getAll(@QueryParam("page") @DefaultValue("1") int page,
                           @QueryParam("limit") @DefaultValue("10") int limit) {
        // Servis sada vraća PagedResult, koji direktno prosleđujemo kao odgovor
        PagedResult<EventResponseDTO> pagedResult = eventService.getAllEvents(page, limit);
        return Response.ok(pagedResult).build();
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

    @GET
    @Path("/category/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEventsByCategory(@PathParam("id") Long id,
                                        @QueryParam("page") @DefaultValue("1") int page,
                                        @QueryParam("limit") @DefaultValue("9") int limit) {
        PagedResult<EventResponseDTO> pagedResult = eventService.getEventsByCategoryId(id, page, limit);
        return Response.ok(pagedResult).build();
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

    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchEvents(@QueryParam("query") String query,
                                 @QueryParam("page") @DefaultValue("1") int page,
                                 @QueryParam("limit") @DefaultValue("9") int limit) {

        // 1. Proveravamo da li je upit za pretragu prisutan
        if (query == null || query.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Query parameter 'query' is required.\"}")
                    .build();
        }

        // 2. Pozivamo ispravnu metodu servisa sa svim parametrima
        PagedResult<EventResponseDTO> pagedResult = eventService.searchEvents(query, page, limit);

        // 3. Vraćamo ceo PagedResult objekat direktno. Frontend zna kako da ga obradi.
        return Response.ok(pagedResult).build();
    }

    //VIEW LIKE DISLIKE
    @POST
    @Path("/{id}/view")
    public Response incrementView(@PathParam("id") Long id) {
        eventService.incrementViewCount(id);
        return Response.ok().build();
    }

    @GET
    @Path("/top-reacted")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTopReacted() {
        List<EventResponseDTO> events = eventService.getTopReactedEvents().stream()
                .map(EventResponseDTO::new)
                .collect(Collectors.toList());
        return Response.ok(events).build();
    }

    @GET
    @Path("/{id}/related")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRelatedEvents(@PathParam("id") Long eventId) {
        List<EventResponseDTO> events = eventService.getRelatedEvents(eventId).stream()
                .map(EventResponseDTO::new)
                .collect(Collectors.toList());
        return Response.ok(events).build();
    }

    @GET
    @Path("/most-visited")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMostVisited() {
        List<EventResponseDTO> events = eventService.getMostVisitedEvents().stream()
                .map(EventResponseDTO::new)
                .collect(Collectors.toList());
        return Response.ok(events).build();
    }

    @POST
    @Path("/{id}/like")
    @Produces(MediaType.APPLICATION_JSON)
    public Response likeEvent(@PathParam("id") Long id) {
        return Response.ok(new EventResponseDTO(eventService.likeEvent(id))).build();
    }

    @POST
    @Path("/{id}/dislike")
    @Produces(MediaType.APPLICATION_JSON)
    public Response dislikeEvent(@PathParam("id") Long id) {
        return Response.ok(new EventResponseDTO(eventService.dislikeEvent(id))).build();
    }

}

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
}

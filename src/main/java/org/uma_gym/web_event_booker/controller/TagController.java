package org.uma_gym.web_event_booker.controller;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.uma_gym.web_event_booker.controller.dto.EventResponseDTO;
import org.uma_gym.web_event_booker.model.Tag;
import org.uma_gym.web_event_booker.service.EventService;
import org.uma_gym.web_event_booker.service.TagService;

import java.util.List;
import java.util.stream.Collectors;

@Path("/tags")
public class TagController {

    @Inject
    private EventService eventService;

    @Inject
    private TagService tagService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        return Response.ok(tagService.getAllTags()).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(@Valid Tag tag) {
        try {
            Tag newTag = tagService.createTag(tag);
            return Response.status(Response.Status.CREATED).entity(newTag).build();
        } catch (Exception e) {
            // Pretpostavka je da će doći do greške ako je naziv duplikat
            return Response.status(Response.Status.CONFLICT).entity("Tag sa tim nazivom već postoji.").build();
        }
    }

    @GET
    @Path("/{id}/events")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEventsByTag(@PathParam("id") Long id) {
        List<EventResponseDTO> events = eventService.getEventsByTagId(id).stream()
                .map(EventResponseDTO::new)
                .collect(Collectors.toList());
        return Response.ok(events).build();
    }


}
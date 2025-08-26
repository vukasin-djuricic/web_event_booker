package org.uma_gym.web_event_booker.controller;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.uma_gym.web_event_booker.model.Tag;
import org.uma_gym.web_event_booker.service.TagService;

@Path("/tags")
public class TagController {

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
}
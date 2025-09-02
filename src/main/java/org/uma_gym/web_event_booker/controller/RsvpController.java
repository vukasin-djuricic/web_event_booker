// src/main/java/org/uma_gym/web_event_booker/controller/RsvpController.java
package org.uma_gym.web_event_booker.controller;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.uma_gym.web_event_booker.controller.dto.RsvpCreateDTO;
import org.uma_gym.web_event_booker.service.RsvpService;

import java.util.Map;

@Path("/events/{eventId}/rsvps")
public class RsvpController {

    @Inject
    private RsvpService rsvpService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createRsvp(@PathParam("eventId") Long eventId, @Valid RsvpCreateDTO rsvpDto) {
        // Servis će baciti WebApplicationException sa odgovarajućim statusom ako nešto nije u redu
        return Response.status(Response.Status.CREATED).entity(rsvpService.createRsvp(eventId, rsvpDto)).build();
    }

    @GET
    @Path("/count")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRsvpCount(@PathParam("eventId") Long eventId) {
        long count = rsvpService.getRsvpCount(eventId);
        return Response.ok(Map.of("count", count)).build();
    }
}
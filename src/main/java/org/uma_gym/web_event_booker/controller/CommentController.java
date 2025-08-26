package org.uma_gym.web_event_booker.controller;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.uma_gym.web_event_booker.controller.dto.CommentCreateDTO;
import org.uma_gym.web_event_booker.controller.dto.CommentResponseDTO;
import org.uma_gym.web_event_booker.model.Comment;
import org.uma_gym.web_event_booker.service.CommentService;
import java.util.List;
import java.util.stream.Collectors;

@Path("/events/{eventId}/comments") // Ugnježdena putanja!
public class CommentController {

    @Inject
    private CommentService commentService;

    // Parametar {eventId} će biti automatski dostupan svim metodama u ovoj klasi
    @PathParam("eventId")
    private Long eventId;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCommentsForEvent() {
        try {
            List<CommentResponseDTO> comments = commentService.getCommentsForEvent(eventId)
                    .stream()
                    .map(CommentResponseDTO::new)
                    .collect(Collectors.toList());
            return Response.ok(comments).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addCommentToEvent(@Valid CommentCreateDTO commentDTO) {
        try {
            Comment newComment = commentService.createCommentForEvent(eventId, commentDTO);
            return Response.status(Response.Status.CREATED).entity(new CommentResponseDTO(newComment)).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }
}
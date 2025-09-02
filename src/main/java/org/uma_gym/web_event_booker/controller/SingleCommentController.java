// src/main/java/org/uma_gym/web_event_booker/controller/SingleCommentController.java
package org.uma_gym.web_event_booker.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.uma_gym.web_event_booker.controller.dto.CommentResponseDTO;
import org.uma_gym.web_event_booker.service.CommentService;

@Path("/comments/{commentId}")
public class SingleCommentController {

    @Inject
    private CommentService commentService;

    @POST
    @Path("/like")
    @Produces(MediaType.APPLICATION_JSON)
    public Response likeComment(@PathParam("commentId") Long commentId) {
        try {
            return Response.ok(new CommentResponseDTO(commentService.likeComment(commentId))).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/dislike")
    @Produces(MediaType.APPLICATION_JSON)
    public Response dislikeComment(@PathParam("commentId") Long commentId) {
        try {
            return Response.ok(new CommentResponseDTO(commentService.dislikeComment(commentId))).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }
}
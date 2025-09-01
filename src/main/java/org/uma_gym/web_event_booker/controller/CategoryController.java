package org.uma_gym.web_event_booker.controller;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.uma_gym.web_event_booker.model.Category;
import org.uma_gym.web_event_booker.service.CategoryService;

@Path("/categories")
public class CategoryController {

    @Inject
    private CategoryService categoryService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@QueryParam("page") @DefaultValue("1") int page,
                           @QueryParam("limit") @DefaultValue("10") int limit) {
        return Response.ok(categoryService.getAllCategories(page, limit)).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") Long id) {
        return this.categoryService.getCategoryById(id)
                .map(category -> Response.ok(category).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(@Valid Category category) {
        Category newCategory = this.categoryService.createCategory(category);
        return Response.status(Response.Status.CREATED).entity(newCategory).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Long id, @Valid Category category) {
        // Proveravamo da li kategorija sa datim ID-jem uopšte postoji
        return categoryService.getCategoryById(id)
                .map(existingCategory -> {
                    category.setId(id); // Osiguravamo da ID ostane isti
                    Category updatedCategory = categoryService.updateCategory(category);
                    return Response.ok(updatedCategory).build();
                })
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        // Proveravamo da li kategorija sa datim ID-jem postoji pre brisanja
        return categoryService.getCategoryById(id)
                .map(category -> {
                    categoryService.deleteCategory(id);
                    return Response.noContent().build(); // 204 No Content
                })
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }
}
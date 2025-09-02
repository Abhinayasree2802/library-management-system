package com.library.resources;

import com.library.dao.AuthorDAO;
import com.library.models.Author;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Path("/authors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthorResource {
    private final AuthorDAO authorDAO;

    public AuthorResource(AuthorDAO authorDAO) {
        this.authorDAO = authorDAO;
    }

    @POST
    public Response createAuthor(Author author) {
        try {
            Author newAuthor = authorDAO.create(author);
            return Response.status(Response.Status.CREATED).entity(newAuthor).build();
        } catch (SQLException e) {
            return Response.serverError().entity("Error creating author: " + e.getMessage()).build();
        }
    }

    @GET
    public Response listAuthors() {
        try {
            List<Author> authors = authorDAO.findAll();
            return Response.ok(authors).build();
        } catch (SQLException e) {
            return Response.serverError().entity("Error fetching authors: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getAuthor(@PathParam("id") Long id) {
        try {
            Optional<Author> author = authorDAO.findById(id);
            if (author.isPresent()) {
                return Response.ok(author.get()).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Author not found").build();
            }
        } catch (SQLException e) {
            return Response.serverError().entity("Error fetching author: " + e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateAuthor(@PathParam("id") Long id, Author authorDetails) {
        try {
            Optional<Author> existingAuthorOptional = authorDAO.findById(id);
            if (existingAuthorOptional.isPresent()) {
                authorDetails.setId(id); // Ensure the ID is set for the update
                authorDAO.update(authorDetails);
                return Response.ok(authorDetails).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Author not found").build();
            }
        } catch (SQLException e) {
            return Response.serverError().entity("Error updating author: " + e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteAuthor(@PathParam("id") Long id) {
        try {
            Optional<Author> author = authorDAO.findById(id);
            if (author.isPresent()) {
                authorDAO.delete(id);
                return Response.status(Response.Status.NO_CONTENT).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Author not found").build();
            }
        } catch (SQLException e) {
            return Response.serverError().entity("Error deleting author: " + e.getMessage()).build();
        }
    }
}
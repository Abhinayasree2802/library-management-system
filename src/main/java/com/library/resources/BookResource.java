package com.library.resources;

import com.library.dao.BookDAO;
import com.library.models.Book;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {
    private final BookDAO bookDAO;

    public BookResource(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    @POST
    public Response createBook(Book book) {
        try {
            Book newBook = bookDAO.create(book);
            return Response.status(Response.Status.CREATED).entity(newBook).build();
        } catch (SQLException e) {
            return Response.serverError().entity("Error creating book: " + e.getMessage()).build();
        }
    }

    @GET
    public Response listBooks() {
        try {
            List<Book> books = bookDAO.findAll();
            return Response.ok(books).build();
        } catch (SQLException e) {
            return Response.serverError().entity("Error fetching books: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getBook(@PathParam("id") Long id) {
        try {
            Optional<Book> book = bookDAO.findById(id);
            if (book.isPresent()) {
                return Response.ok(book.get()).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Book not found").build();
            }
        } catch (SQLException e) {
            return Response.serverError().entity("Error fetching book: " + e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateBook(@PathParam("id") Long id, Book bookDetails) {
        try {
            Optional<Book> existingBookOptional = bookDAO.findById(id);
            if (existingBookOptional.isPresent()) {
                bookDetails.setId(id); // Ensure the ID is set for the update
                bookDAO.update(bookDetails);
                return Response.ok(bookDetails).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Book not found").build();
            }
        } catch (SQLException e) {
            return Response.serverError().entity("Error updating book: " + e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteBook(@PathParam("id") Long id) {
        try {
            Optional<Book> book = bookDAO.findById(id);
            if (book.isPresent()) {
                bookDAO.delete(id);
                return Response.status(Response.Status.NO_CONTENT).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Book not found").build();
            }
        } catch (SQLException e) {
            return Response.serverError().entity("Error deleting book: " + e.getMessage()).build();
        }
    }
}
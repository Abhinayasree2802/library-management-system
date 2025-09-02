package com.library.resources;

import com.library.dao.AuthorDAO;
import com.library.models.Author;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

class AuthorResourceTest {

    private AuthorResource authorResource;
    private AuthorDAO authorDAO;

    @BeforeEach
    void setUp() {
        authorDAO = Mockito.mock(AuthorDAO.class);
        authorResource = new AuthorResource(authorDAO);
    }

    @Test
    void testCreateAuthorSuccess() throws SQLException {
        Author mockAuthor = new Author();
        mockAuthor.setName("Test Author");

        when(authorDAO.create(any(Author.class))).thenReturn(mockAuthor);

        Response response = authorResource.createAuthor(mockAuthor);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertEquals(mockAuthor, response.getEntity());
    }

    @Test
    void testCreateAuthorFailure() throws SQLException {
        doThrow(new SQLException("Database error")).when(authorDAO).create(any(Author.class));

        Response response = authorResource.createAuthor(new Author());
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    }

    @Test
    void testGetAuthorByIdSuccess() throws SQLException {
        Author mockAuthor = new Author();
        mockAuthor.setId(1L);
        mockAuthor.setName("Test Author");

        when(authorDAO.findById(1L)).thenReturn(Optional.of(mockAuthor));

        Response response = authorResource.getAuthor(1L);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(mockAuthor, response.getEntity());
    }

    @Test
    void testGetAuthorByIdNotFound() throws SQLException {
        when(authorDAO.findById(1L)).thenReturn(Optional.empty());

        Response response = authorResource.getAuthor(1L);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    void testUpdateAuthorSuccess() throws SQLException {
        Author existingAuthor = new Author();
        existingAuthor.setId(1L);

        Author updatedDetails = new Author();
        updatedDetails.setName("Updated Name");

        when(authorDAO.findById(1L)).thenReturn(Optional.of(existingAuthor));

        Response response = authorResource.updateAuthor(1L, updatedDetails);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(updatedDetails.getName(), ((Author) response.getEntity()).getName());
    }

    @Test
    void testUpdateAuthorNotFound() throws SQLException {
        when(authorDAO.findById(1L)).thenReturn(Optional.empty());

        Response response = authorResource.updateAuthor(1L, new Author());
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    void testDeleteAuthorSuccess() throws SQLException {
        Author authorToDelete = new Author();
        authorToDelete.setId(1L);

        when(authorDAO.findById(1L)).thenReturn(Optional.of(authorToDelete));

        Response response = authorResource.deleteAuthor(1L);
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }

    @Test
    void testDeleteAuthorNotFound() throws SQLException {
        when(authorDAO.findById(1L)).thenReturn(Optional.empty());

        Response response = authorResource.deleteAuthor(1L);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }
}
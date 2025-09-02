package com.library.resources;

import com.library.dao.BookDAO;
import com.library.models.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import javax.ws.rs.core.Response;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class BookResourceTest {

    private BookResource bookResource;
    private BookDAO bookDAO;

    @BeforeEach
    void setUp() {
        bookDAO = Mockito.mock(BookDAO.class);
        bookResource = new BookResource(bookDAO);
    }

    @Test
    void testGetBookSuccess() throws Exception {
        Book mockBook = new Book();
        mockBook.setId(1L);
        mockBook.setTitle("The Hitchhiker's Guide to the Galaxy");

        when(bookDAO.findById(1L)).thenReturn(Optional.of(mockBook));

        Response response = bookResource.getBook(1L);
        assertEquals(200, response.getStatus());
        assertEquals(mockBook, response.getEntity());
    }

    @Test
    void testGetBookNotFound() throws Exception {
        when(bookDAO.findById(1L)).thenReturn(Optional.empty());

        Response response = bookResource.getBook(1L);
        assertEquals(404, response.getStatus());
    }

    @Test
    void testCreateBookSuccess() throws Exception {
        Book newBook = new Book();
        newBook.setTitle("Dune");

        when(bookDAO.create(any(Book.class))).thenReturn(newBook);

        Response response = bookResource.createBook(newBook);
        assertEquals(201, response.getStatus());
        assertEquals(newBook, response.getEntity());
    }

    @Test
    void testUpdateBookSuccess() throws Exception {
        Book existingBook = new Book();
        existingBook.setId(1L);
        existingBook.setTitle("Old Title");

        Book updatedBookDetails = new Book();
        updatedBookDetails.setTitle("New Title");

        when(bookDAO.findById(1L)).thenReturn(Optional.of(existingBook));
        doNothing().when(bookDAO).update(any(Book.class));

        Response response = bookResource.updateBook(1L, updatedBookDetails);
        assertEquals(200, response.getStatus());
        assertEquals(updatedBookDetails.getTitle(), ((Book) response.getEntity()).getTitle());
    }

    @Test
    void testUpdateBookNotFound() throws Exception {
        Book updatedBookDetails = new Book();
        updatedBookDetails.setTitle("New Title");

        when(bookDAO.findById(1L)).thenReturn(Optional.empty());

        Response response = bookResource.updateBook(1L, updatedBookDetails);
        assertEquals(404, response.getStatus());
    }

    @Test
    void testDeleteBookSuccess() throws Exception {
        Book bookToDelete = new Book();
        bookToDelete.setId(1L);

        when(bookDAO.findById(1L)).thenReturn(Optional.of(bookToDelete));
        doNothing().when(bookDAO).delete(1L);

        Response response = bookResource.deleteBook(1L);
        assertEquals(204, response.getStatus());
    }

    @Test
    void testDeleteBookNotFound() throws Exception {
        when(bookDAO.findById(1L)).thenReturn(Optional.empty());

        Response response = bookResource.deleteBook(1L);
        assertEquals(404, response.getStatus());
    }
}
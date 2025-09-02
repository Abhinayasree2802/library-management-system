package com.library.dao;

import com.library.models.Book;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDAO {
    private final Connection connection;

    public BookDAO(Connection connection) {
        this.connection = connection;
    }

    public Book create(Book book) throws SQLException {
        String sql = "INSERT INTO books (title, authorId, publishedDate, isbn) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, book.getTitle());
            stmt.setLong(2, book.getAuthorId());
            stmt.setDate(3, new java.sql.Date(book.getPublishedDate().getTime()));
            stmt.setString(4, book.getIsbn());
            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    book.setId(generatedKeys.getLong(1));
                }
            }
        }
        return book;
    }

    public Optional<Book> findById(Long id) throws SQLException {
        String sql = "SELECT id, title, authorId, publishedDate, isbn FROM books WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Book book = new Book();
                    book.setId(rs.getLong("id"));
                    book.setTitle(rs.getString("title"));
                    book.setAuthorId(rs.getLong("authorId"));
                    book.setPublishedDate(rs.getDate("publishedDate"));
                    book.setIsbn(rs.getString("isbn"));
                    return Optional.of(book);
                }
            }
        }
        return Optional.empty();
    }

    public List<Book> findAll() throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT id, title, authorId, publishedDate, isbn FROM books";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Book book = new Book();
                book.setId(rs.getLong("id"));
                book.setTitle(rs.getString("title"));
                book.setAuthorId(rs.getLong("authorId"));
                book.setPublishedDate(rs.getDate("publishedDate"));
                book.setIsbn(rs.getString("isbn"));
                books.add(book);
            }
        }
        return books;
    }

    public void update(Book book) throws SQLException {
        String sql = "UPDATE books SET title = ?, authorId = ?, publishedDate = ?, isbn = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, book.getTitle());
            stmt.setLong(2, book.getAuthorId());
            stmt.setDate(3, new java.sql.Date(book.getPublishedDate().getTime()));
            stmt.setString(4, book.getIsbn());
            stmt.setLong(5, book.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM books WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }
}
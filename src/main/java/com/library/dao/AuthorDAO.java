package com.library.dao;

import com.library.models.Author;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuthorDAO {
    private final Connection connection;

    public AuthorDAO(Connection connection) {
        this.connection = connection;
    }

    public Author create(Author author) throws SQLException {
        String sql = "INSERT INTO authors (name, birthdate, nationality) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, author.getName());
            stmt.setDate(2, new java.sql.Date(author.getBirthdate().getTime()));
            stmt.setString(3, author.getNationality());
            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    author.setId(generatedKeys.getLong(1));
                }
            }
        }
        return author;
    }

    public Optional<Author> findById(Long id) throws SQLException {
        String sql = "SELECT id, name, birthdate, nationality FROM authors WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Author author = new Author();
                    author.setId(rs.getLong("id"));
                    author.setName(rs.getString("name"));
                    author.setBirthdate(rs.getDate("birthdate"));
                    author.setNationality(rs.getString("nationality"));
                    return Optional.of(author);
                }
            }
        }
        return Optional.empty();
    }

    public List<Author> findAll() throws SQLException {
        List<Author> authors = new ArrayList<>();
        String sql = "SELECT id, name, birthdate, nationality FROM authors";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Author author = new Author();
                author.setId(rs.getLong("id"));
                author.setName(rs.getString("name"));
                author.setBirthdate(rs.getDate("birthdate"));
                author.setNationality(rs.getString("nationality"));
                authors.add(author);
            }
        }
        return authors;
    }

    public void update(Author author) throws SQLException {
        String sql = "UPDATE authors SET name = ?, birthdate = ?, nationality = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, author.getName());
            stmt.setDate(2, new java.sql.Date(author.getBirthdate().getTime()));
            stmt.setString(3, author.getNationality());
            stmt.setLong(4, author.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM authors WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }
}